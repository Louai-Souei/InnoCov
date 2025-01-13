package covoiturage.project.InnoCov.service.serviceImplementation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import covoiturage.project.InnoCov.config.JwtServiceImpl;
import covoiturage.project.InnoCov.dto.auth.AuthenticationRequest;
import covoiturage.project.InnoCov.dto.auth.RegisterRequest;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.UserRepository;
import covoiturage.project.InnoCov.service.serviceInterface.auth.AuthenticationService;
import covoiturage.project.InnoCov.tools.tokenTools.Token;
import covoiturage.project.InnoCov.tools.tokenTools.TokenRepository;
import covoiturage.project.InnoCov.tools.tokenTools.TokenType;
import covoiturage.project.InnoCov.util.ApiResponse;
import covoiturage.project.InnoCov.util.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


    @Override
    public ApiResponse<AuthenticationResponse> register(RegisterRequest registerRequest, MultipartFile image) throws IOException {

        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            return new ApiResponse<>(
                    false,
                    "Duplication Error",
                    "A user with this phone number already exists.",
                    null
            );
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ApiResponse<>(
                    false,
                    "Duplication Error",
                    "A user with this email address already exists.",
                    null
            );
        }


        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .phone(registerRequest.getPhone())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .occupation(registerRequest.getOccupation())
                .userImage(image.getBytes())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtServiceImpl.generateToken(user);
        AuthenticationResponse authResponse = getAuthenticationResponse(user, savedUser, jwtToken);
        return new ApiResponse<>(
                true,
                "Registration Successful",
                "User registered successfully.",
                authResponse
        );
    }

    @Override
    public ApiResponse<AuthenticationResponse> login(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        if (!user.isStatus()) {
            return new ApiResponse<>(
                    false,
                    "Compte bloqué",
                    "Your account is blocked. contact administrator.",
                    null
            );
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        var jwtToken = jwtServiceImpl.generateToken(user);
        revokeAllUserTokens(user);

        var authenticationResponse = getAuthenticationResponse(user, user, jwtToken);
        return new ApiResponse<>(
                true,
                "Authentification réussie",
                "Connexion effectuée avec succès.",
                authenticationResponse
        );
    }


    private AuthenticationResponse getAuthenticationResponse(
            User user, User savedUser, String jwtToken
    ) {
        saveUserToken(savedUser, jwtToken, TokenType.ACCESS);
        var refreshToken = jwtServiceImpl.generateRefreshToken(user);
        saveUserToken(savedUser, refreshToken, TokenType.REFRESH);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .role(user.getRole().name())
                .email(user.getEmail())
                .occupation(user.getOccupation().name())
                .build();
    }


    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        log.info("Refresh token");
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtServiceImpl.extractUsername(refreshToken);

        if (userEmail != null) {
            User user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            var isTokenValid = tokenRepository.findByToken(refreshToken)
                    .map(token -> !token.isExpired() && !token.isRevoked())
                    .orElse(false);
            if (jwtServiceImpl.isTokenValid(refreshToken, user) && isTokenValid) {
                var accessToken = jwtServiceImpl.generateToken(user);

                var validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
                validTokens.stream()
                        .filter(token -> token.getTokenType() == TokenType.ACCESS)
                        .forEach(token -> token.setToken(accessToken));
                tokenRepository.saveAll(validTokens);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void revokeAllUserTokens (User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken, TokenType tokenType) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public User getActiveUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

}

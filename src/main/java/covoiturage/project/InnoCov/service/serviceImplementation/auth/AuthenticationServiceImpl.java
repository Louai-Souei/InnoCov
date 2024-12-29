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
import covoiturage.project.InnoCov.util.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtServiceImpl jwtServiceImpl,
                                     AuthenticationManager authenticationManager,
                                     TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtServiceImpl = jwtServiceImpl;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public AuthenticationResponse register (RegisterRequest registerRequest){
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .phone(registerRequest.getPhone())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userRole(registerRequest.getUserRole())
                .role(registerRequest.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtServiceImpl.generateToken(user);
        return getAuthenticationResponse(user, savedUser, jwtToken);

    }

    private AuthenticationResponse getAuthenticationResponse(User user, User savedUser, String jwtToken) {
        saveUserToken(savedUser, jwtToken, TokenType.ACCESS);
        var refreshToken = jwtServiceImpl.generateRefreshToken(user);
        saveUserToken(savedUser, refreshToken, TokenType.REFRESH);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .userRole(user.getUserRole().name())
                .email(user.getEmail())
                .build();
    }


    @Override
    public AuthenticationResponse login (AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(
                authenticationRequest.getEmail())
                .orElseThrow();
        var jwtToken = jwtServiceImpl.generateToken(user);
        revokeAllUserTokens(user);
        return getAuthenticationResponse(user, user, jwtToken);
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

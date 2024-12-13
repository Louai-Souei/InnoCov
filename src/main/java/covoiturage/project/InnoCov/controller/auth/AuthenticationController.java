package covoiturage.project.InnoCov.controller.auth;

import covoiturage.project.InnoCov.dto.auth.AuthenticationRequest;
import covoiturage.project.InnoCov.dto.auth.RegisterRequest;
import covoiturage.project.InnoCov.service.serviceInterface.auth.AuthenticationService;
import covoiturage.project.InnoCov.util.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return  ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request) {
        return  ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
            ) throws IOException {
        authenticationService.refreshToken(request,response);
    }

}

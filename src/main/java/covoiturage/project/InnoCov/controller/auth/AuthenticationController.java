package covoiturage.project.InnoCov.controller.auth;

import covoiturage.project.InnoCov.dto.auth.AuthenticationRequest;
import covoiturage.project.InnoCov.dto.auth.RegisterRequest;
import covoiturage.project.InnoCov.entity.enums.Occupation;
import covoiturage.project.InnoCov.entity.enums.Role;
import covoiturage.project.InnoCov.service.serviceInterface.auth.AuthenticationService;
import covoiturage.project.InnoCov.util.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestParam("firstname") String firstname,
            @RequestParam("lastname") String lastname,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") Role role,
            @RequestParam("occupation") Occupation occupation,
            @RequestPart("image") MultipartFile image) throws IOException {

        RegisterRequest registerRequest = new RegisterRequest(firstname, lastname, phone, email, password, role, occupation);

        AuthenticationResponse response = authenticationService.register(registerRequest, image);
        return ResponseEntity.ok(response);
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

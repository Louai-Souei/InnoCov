package covoiturage.project.InnoCov.service.serviceInterface.auth;

import covoiturage.project.InnoCov.dto.auth.AuthenticationRequest;
import covoiturage.project.InnoCov.dto.auth.RegisterRequest;
import covoiturage.project.InnoCov.util.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse register (RegisterRequest registerRequest, MultipartFile image) throws IOException;

    AuthenticationResponse login (AuthenticationRequest authenticationRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}

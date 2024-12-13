package covoiturage.project.InnoCov.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/secured-endpoint")
    public ResponseEntity<String> getSecuredMessage() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}

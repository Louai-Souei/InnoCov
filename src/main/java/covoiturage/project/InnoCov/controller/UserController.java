package covoiturage.project.InnoCov.controller;


import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getProfileByEmail(@PathVariable String email) {
        UserDto userDto = userService.getProfileByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserDto> updateProfileByEmail(@PathVariable String email, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateProfileByEmail(email, userDto);
        return ResponseEntity.ok(updatedUser);
    }
}

package covoiturage.project.InnoCov.controller;


import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.service.serviceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/getAll")
    public List<UserDto> getAllUsers() {

        return userService.getAllUsers();
    }

    @PatchMapping("/activate/{userId}")
    public UserDto activateUser(@PathVariable Integer userId) {
        return userService.activateUser(userId);
    }

    @PatchMapping("/deactivate/{userId}")
    public UserDto deactivateUser(@PathVariable Integer userId) {
        return userService.deactivateUser(userId);
    }
}

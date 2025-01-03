package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.service.serviceInterface.UserService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<UserDto>> getActiveUser() {
        try {
            log.info("Fetching active user");
            return userService.getActiveUser();
        } catch (Exception e) {
            log.error("Error while fetching active user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetch active user."));
        }
    }

    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer userId) {
        try {
            log.info("Fetching user with ID: {}", userId);
            return userService.getUserById(userId);
        } catch (Exception e) {
            log.error("Error while fetching user by ID: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetch user."));
        }
    }


    @PutMapping("/update-profile")
    public ResponseEntity<ApiResponse<UserDto>> updateUserProfile(@RequestBody UserDto userDto) {
        try {
            log.info("Updating profile for user: {}", userDto.getEmail());
            return userService.updateUserProfile(userDto.getId(), userDto);
        } catch (Exception e) {
            log.error("Error while updating user profile: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to update profile."));
        }
    }

    @GetMapping("/get-all")
//    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        try {
            log.info("Fetching all users ...");
            return userService.getAllUsers();
        } catch (Exception e) {
            log.error("Error while fetching users ", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetch all users."));
        }
    }
}

package covoiturage.project.InnoCov.controller;


import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.service.serviceInterface.UserService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/creation-stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUserCreationStatsForLast4Weeks() {
        try {
            log.info("Fetching user creation stats for the last 4 weeks");
            Map<String, Long> stats = userService.getUserCreationStatsForLast4Weeks();
            return ResponseEntity.ok(new ApiResponse<>(true, "User creation stats fetched successfully.", stats));
        } catch (Exception e) {
            log.error("Error fetching user creation stats: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Failed to fetch user creation stats."));
        }
    }

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

    @PutMapping("/activate/{userId}")
    public UserDto activateUser(@PathVariable Integer userId) {
        return userService.activateUser(userId);
    }

    @PutMapping("/deactivate/{userId}")
    public UserDto deactivateUser(@PathVariable Integer userId) {
        return userService.deactivateUser(userId);
    }
}

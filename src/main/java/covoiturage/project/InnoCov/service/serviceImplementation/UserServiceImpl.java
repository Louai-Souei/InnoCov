package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.UserRepository;
import covoiturage.project.InnoCov.service.serviceInterface.UserService;
import covoiturage.project.InnoCov.util.ApiResponse;
import covoiturage.project.InnoCov.service.serviceImplementation.auth.AuthenticationServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<UserDto>> getActiveUser() {
        try {
            User activeUser = authenticationService.getActiveUser();
            UserDto userDto = new UserDto(activeUser);
            log.info("Fetched active user: {}", activeUser.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "Active user fetched successfully.", userDto));
        } catch (Exception e) {
            log.error("Error fetching active user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Failed to fetch active user."));
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<UserDto>> getUserById(Integer userId) {
        try {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty()) {
                log.warn("User with ID {} not found", userId);
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "User not found."));
            }
            UserDto userDto = new UserDto(userOptional.get());
            log.info("Fetched user with ID {}: {}", userId, userOptional.get().getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully.", userDto));
        } catch (Exception e) {
            log.error("Error fetching user by ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Failed to fetch user."));
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ResponseEntity<ApiResponse<UserDto>> updateUserProfile(Integer userId, UserDto userDto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            user.setFirstname(userDto.getFirstname());
            user.setLastname(userDto.getLastname());
            user.setPhone(userDto.getPhone());
            user.setEmail(userDto.getEmail());
            user.setRole(userDto.getRole());
            user.setOccupation(userDto.getOccupation());

            userRepository.save(user);
            log.info("User profile updated successfully for ID {}: {}", userId, user.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "User profile updated successfully.", userDto));
        } catch (Exception e) {
            log.error("Error updating user profile for ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Failed to update user profile."));
        }
    }
}

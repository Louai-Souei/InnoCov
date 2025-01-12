package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.UserRepository;
import covoiturage.project.InnoCov.service.serviceImplementation.auth.AuthenticationServiceImpl;
import covoiturage.project.InnoCov.service.serviceInterface.UserService;
import covoiturage.project.InnoCov.util.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl  implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;


    @Override
    public UserDto getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        return new UserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateProfileByEmail(String email, UserDto userDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));

        // Mise à jour des champs
        if (userDto.getFirstname() != null) user.setFirstname(userDto.getFirstname());
        if (userDto.getLastname() != null) user.setLastname(userDto.getLastname());
        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());

        userRepository.save(user);
        return new UserDto(user);
    }
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::new) // Convertir chaque User en UserDto
                .collect(Collectors.toList());
    }

    @Override
    public UserDto activateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(true); // Activer le statut
        userRepository.save(user);
        return new UserDto(user); // Retourner le UserDto mis à jour
    }

    @Override
    public UserDto deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(false); // Désactiver le statut
        userRepository.save(user);
        return new UserDto(user); // Retourner le UserDto mis à jour
    }


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

    @Transactional()
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

    @Transactional
    @Override
    public Map<String, Long> getUserCreationStatsForLast4Weeks() {
        try {
            LocalDateTime weekEnd;
            LocalDateTime startDate = LocalDate.now().minusWeeks(4).atStartOfDay();
            LocalDateTime endDate = LocalDate.now().plusDays(1).atStartOfDay();

            List<User> users = userRepository.findAllByCreatedAtBetween(startDate, endDate);

            Map<String, Long> userStats = new TreeMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (int i = 0; i < 4; i++) {
                LocalDateTime weekStart = startDate.plusWeeks(i);
                if (i == 3) {
                    weekEnd = weekStart.plusDays(7).withHour(23).withMinute(59).withSecond(59);
                } else {
                    weekEnd = weekStart.plusDays(6).withHour(23).withMinute(59).withSecond(59);
                }

                LocalDateTime finalWeekEnd = weekEnd;
                long count = users.stream()
                        .filter(user -> !user.getCreatedAt().isBefore(weekStart) && !user.getCreatedAt().isAfter(finalWeekEnd))
                        .count();

                String dateRangeKey = weekStart.format(formatter) + " To " + weekEnd.format(formatter);
                userStats.put(dateRangeKey, count);
            }

            log.info("User creation stats for the last 4 weeks: {}", userStats);
            return userStats;
        } catch (Exception e) {
            log.error("Error fetching user creation stats: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user creation stats.");
        }
    }

}

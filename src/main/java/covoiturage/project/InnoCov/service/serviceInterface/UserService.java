package covoiturage.project.InnoCov.service.serviceInterface;


import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto getProfileByEmail(String email);

    UserDto updateProfileByEmail(String email, UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto activateUser(Integer userId);

    UserDto deactivateUser(Integer userId);

    ResponseEntity<ApiResponse<UserDto>> getActiveUser();

    ResponseEntity<ApiResponse<UserDto>> getUserById(Integer userId);

    ResponseEntity<ApiResponse<UserDto>> updateUserProfile(Integer userId, UserDto userDto);

    Map<String, Long> getUserCreationStatsForLast4Weeks();

}

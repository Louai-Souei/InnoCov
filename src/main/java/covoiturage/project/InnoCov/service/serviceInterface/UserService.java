package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse<UserDto>> getActiveUser();
    ResponseEntity<ApiResponse<UserDto>> getUserById(Integer userId);
    ResponseEntity<ApiResponse<UserDto>> updateUserProfile(Integer userId, UserDto userDto);
}

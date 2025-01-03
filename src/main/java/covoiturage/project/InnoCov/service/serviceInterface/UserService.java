package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<ApiResponse<UserDto>> getActiveUser();

    ResponseEntity<ApiResponse<UserDto>> getUserById(Integer userId);

    ResponseEntity<ApiResponse<UserDto>> updateUserProfile(Integer userId, UserDto userDto);

    ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers();
}

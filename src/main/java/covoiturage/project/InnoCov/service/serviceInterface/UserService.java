package covoiturage.project.InnoCov.service.serviceInterface;


import covoiturage.project.InnoCov.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getProfileByEmail(String email);
    UserDto updateProfileByEmail(String email, UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto activateUser(Integer userId);
    UserDto deactivateUser(Integer userId);
}

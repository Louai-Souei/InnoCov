package covoiturage.project.InnoCov.service.serviceInterface;


import covoiturage.project.InnoCov.dto.UserDto;

public interface UserService {
    UserDto getProfileByEmail(String email);
    UserDto updateProfileByEmail(String email, UserDto userDto);
}

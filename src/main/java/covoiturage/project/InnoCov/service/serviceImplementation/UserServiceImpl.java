package covoiturage.project.InnoCov.service.serviceImplementation;
import covoiturage.project.InnoCov.dto.UserDto;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.UserRepository;
import covoiturage.project.InnoCov.service.serviceInterface.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl  implements UserService {

    private final UserRepository userRepository;

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
}

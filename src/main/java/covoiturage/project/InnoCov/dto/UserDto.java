package covoiturage.project.InnoCov.dto;

import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.entity.enums.Role;
import covoiturage.project.InnoCov.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    int id;
    String firstname;
    String lastname;
    String phone;
    String email;
    String password;
    UserRole userRole;
    Role role;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.userRole = user.getUserRole();
        this.role = user.getRole();
    }
}

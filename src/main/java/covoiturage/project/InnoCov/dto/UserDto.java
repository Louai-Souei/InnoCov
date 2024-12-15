package covoiturage.project.InnoCov.dto;

import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.entity.enums.Role;
import covoiturage.project.InnoCov.entity.enums.UserRole;
import covoiturage.project.InnoCov.tools.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Convertible<User> {
    int id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private UserRole userRole;
    private Role role;

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

    @Override
    public User convert() {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(userRole);
        user.setRole(role);
        return user;
    }
}

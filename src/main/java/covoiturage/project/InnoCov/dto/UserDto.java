package covoiturage.project.InnoCov.dto;

import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.entity.enums.Occupation;
import covoiturage.project.InnoCov.entity.enums.Role;
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
    private Role role;
    private Occupation occupation;
    private byte[] userImage;

    public UserDto(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.occupation = user.getOccupation();
        this.userImage = user.getUserImage();
    }

    @Override
    public User convert() {
        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(role);
        user.setOccupation(occupation);
        return user;
    }
}

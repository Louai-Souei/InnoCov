package covoiturage.project.InnoCov.dto.auth;

import covoiturage.project.InnoCov.entity.enums.Role;
import covoiturage.project.InnoCov.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private UserRole userRole;
    private Role role;
}

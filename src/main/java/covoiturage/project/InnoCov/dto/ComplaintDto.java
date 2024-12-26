package covoiturage.project.InnoCov.dto;

import covoiturage.project.InnoCov.entity.Complaint;
import covoiturage.project.InnoCov.tools.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDto implements Convertible<Complaint> {
    private Integer id;
    private String description;
    private Date createdAt;
    private UserDto complainer;
    private UserDto targetUser;
    private boolean resolved;

    public ComplaintDto(Complaint complaint) {
        this.id = complaint.getId();
        this.description = complaint.getDescription();
        this.createdAt = complaint.getCreatedAt();
        this.complainer = new UserDto(complaint.getComplainer());
        this.targetUser = new UserDto(complaint.getTargetUser());
        this.resolved = complaint.isResolved();
    }

    @Override
    public Complaint convert() {
        Complaint complaint = new Complaint();
        complaint.setId(id);
        complaint.setDescription(description);
        complaint.setCreatedAt(createdAt);
        if (this.complainer != null) {
            complaint.setComplainer(this.complainer.convert());
        }
        if (this.targetUser != null) {
            complaint.setTargetUser(this.targetUser.convert());
        }
        complaint.setResolved(resolved);
        return complaint;

    }
}


package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.dto.ComplaintDto;
import covoiturage.project.InnoCov.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByTargetUserId(Integer targetUserId);

    List<Complaint> findByComplainerId(Integer complainerId);
    @Query("SELECT NEW covoiturage.project.InnoCov.dto.ComplaintDto(c) " +
            "FROM Complaint c " +
            "GROUP BY c.targetUser.id")
    List<ComplaintDto> findAllComplaintsGroupedByTargetUser();
}
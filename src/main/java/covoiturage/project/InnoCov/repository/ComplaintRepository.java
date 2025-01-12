package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByTargetUserId(Integer targetUserId);

    List<Complaint> findByComplainerId(Integer complainerId);
    @Query("""
        SELECT c FROM Complaint c
        ORDER BY c.targetUser.id
        """)
    List<Complaint> findAllComplaintsOrderedByTargetUser();
}
package covoiturage.project.InnoCov.repository;

import covoiturage.project.InnoCov.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByTargetUserId(Integer targetUserId);

    List<Complaint> findByComplainerId(Integer complainerId);
}
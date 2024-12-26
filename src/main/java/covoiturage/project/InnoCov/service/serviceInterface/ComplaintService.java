package covoiturage.project.InnoCov.service.serviceInterface;

import covoiturage.project.InnoCov.dto.ComplaintDto;
import covoiturage.project.InnoCov.util.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ComplaintService {

    ResponseEntity<ApiResponse<ComplaintDto>> addComplaint(ComplaintDto complaintDto);

    List<ComplaintDto> getComplaintsByTargetUser(Integer targetUserId);

    List<ComplaintDto> getComplaintsByComplainer();

    ResponseEntity<ApiResponse<ComplaintDto>> resolveComplaint(Integer complaintId);
}

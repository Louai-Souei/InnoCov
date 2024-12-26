package covoiturage.project.InnoCov.service.serviceImplementation;

import covoiturage.project.InnoCov.dto.ComplaintDto;
import covoiturage.project.InnoCov.entity.Complaint;
import covoiturage.project.InnoCov.entity.User;
import covoiturage.project.InnoCov.repository.ComplaintRepository;
import covoiturage.project.InnoCov.repository.UserRepository;
import covoiturage.project.InnoCov.service.serviceImplementation.auth.AuthenticationServiceImpl;
import covoiturage.project.InnoCov.service.serviceInterface.ComplaintService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;

    @Override
    public ResponseEntity<ApiResponse<ComplaintDto>> addComplaint(ComplaintDto complaintDto) {
        // Get active user
        User activeUser = authenticationService.getActiveUser();

        // Find target user
        User targetUser = userRepository.findById(complaintDto.getTargetUser().getId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        // Convert DTO to entity and set values
        Complaint complaint = complaintDto.convert();
        complaint.setComplainer(activeUser);
        complaint.setTargetUser(targetUser);
        complaint.setCreatedAt(new Date());
        complaint.setResolved(false);

        // Save complaint
        Complaint savedComplaint = complaintRepository.save(complaint);

        // Create ApiResponse with success message and the saved ComplaintDto
        ApiResponse<ComplaintDto> response = new ApiResponse<>(
                "Complaint Added",
                true,
                "Complaint has been successfully added",
                new ComplaintDto(savedComplaint)
        );

        // Return response
        return ResponseEntity.ok(response);
    }

    @Override
    public List<ComplaintDto> getComplaintsByTargetUser(Integer targetUserId) {

        return complaintRepository.findByTargetUserId(targetUserId).stream()
                .map(ComplaintDto::new)
                .toList();
    }

    @Override
    public List<ComplaintDto> getComplaintsByComplainer() {
        User activeUser = authenticationService.getActiveUser();

        return complaintRepository.findByComplainerId(activeUser.getId()).stream()
                .map(ComplaintDto::new)
                .toList();
    }

    @Override
    public ResponseEntity<ApiResponse<ComplaintDto>> resolveComplaint(Integer complaintId) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setResolved(true);
        Complaint updatedComplaint = complaintRepository.save(complaint);

        ApiResponse<ComplaintDto> response = new ApiResponse<>(
                "Complaint Resolved",
                true,
                "The complaint has been resolved successfully",
                new ComplaintDto(updatedComplaint)
        );

        return ResponseEntity.ok(response);
    }
}

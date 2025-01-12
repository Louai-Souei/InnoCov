package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.dto.ComplaintDto;
import covoiturage.project.InnoCov.service.serviceInterface.ComplaintService;
import covoiturage.project.InnoCov.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping("/new-complaint")
    public ResponseEntity<ApiResponse<ComplaintDto>> addComplaint(@RequestBody ComplaintDto complaintDto) {
        return complaintService.addComplaint(complaintDto);
    }

    @GetMapping("/all-complaints")
    public List<ComplaintDto> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/complaints-by-target/{targetUserId}")
    public List<ComplaintDto> getComplaintsByTargetUser(@PathVariable Integer targetUserId) {
        return complaintService.getComplaintsByTargetUser(targetUserId);
    }

    @GetMapping("/complaints-by-complainer")
    public List<ComplaintDto> getComplaintsByComplainer() {
        return complaintService.getComplaintsByComplainer();
    }

    @PatchMapping("/resolve-complaint/{complaintId}")
    public ResponseEntity<ApiResponse<ComplaintDto>> resolveComplaint(@PathVariable Integer complaintId) {
        return complaintService.resolveComplaint(complaintId);
    }
    @GetMapping("/complaints-grouped-by-target-user")
    public ResponseEntity<List<Map<String, Object>>> getAllComplaintsGroupedByTargetUser() {
        return complaintService.getAllComplaintsGroupedByTargetUser();
    }

}

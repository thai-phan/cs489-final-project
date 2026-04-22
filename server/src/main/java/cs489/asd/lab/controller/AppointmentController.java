package cs489.asd.lab.controller;

import cs489.asd.lab.dto.AppointmentRequest;
import cs489.asd.lab.dto.AppointmentResponse;
import cs489.asd.lab.dto.AppointmentDetailsView;
import cs489.asd.lab.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adsweb/api/v1")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments/request")
    @PreAuthorize("hasAnyRole('MANAGER','PATIENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse requestAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.requestAppointment(request);
    }

    @GetMapping("/appointments/my")
    @PreAuthorize("hasRole('PATIENT')")
    public List<AppointmentDetailsView> getMyAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        return appointmentService.getAppointmentsForCurrentPatient(userDetails.getUsername());
    }

    @GetMapping("/appointments/pending")
    @PreAuthorize("hasAnyRole('MANAGER','DENTIST')")
    public List<AppointmentDetailsView> getPendingAppointments() {
        return appointmentService.getPendingAppointments();
    }
}

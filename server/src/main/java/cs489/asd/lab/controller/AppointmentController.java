package cs489.asd.lab.controller;

import cs489.asd.lab.dto.AppointmentRequest;
import cs489.asd.lab.dto.AppointmentResponse;
import cs489.asd.lab.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adsweb/api/v1")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointments/request")
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse requestAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.requestAppointment(request);
    }
}

package cs489.asd.lab.controller.graphql;

import cs489.asd.lab.dto.AppointmentDetailsView;
import cs489.asd.lab.dto.AppointmentRequest;
import cs489.asd.lab.dto.AppointmentRequestInput;
import cs489.asd.lab.dto.AppointmentResponse;
import cs489.asd.lab.service.AppointmentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppointmentQL {

    private final AppointmentService appointmentService;

    public AppointmentQL(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN','PATIENT')")
    public AppointmentResponse requestAppointment(@Argument AppointmentRequestInput input) {
        AppointmentRequest request = new AppointmentRequest(
                input.firstName(),
                input.lastName(),
                input.phone(),
                input.email(),
                input.dentist(),
                input.contactMethod(),
                input.appointmentDate(),
                input.appointmentTime(),
                input.reason()
        );
        return appointmentService.requestAppointment(request);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','DENTIST','PATIENT')")
    public List<AppointmentDetailsView> appointmentsByDentist(@Argument long dentistId) {
        return appointmentService.getAppointmentsByDentist(dentistId);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','DENTIST','PATIENT')")
    public List<AppointmentDetailsView> appointmentsBySurgeryLocation(@Argument String locationAddress) {
        return appointmentService.getAppointmentsBySurgeryLocation(locationAddress);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','DENTIST','PATIENT')")
    public List<AppointmentDetailsView> appointmentsByPatientAndDate(@Argument long patientId, @Argument String appointmentDate) {
        return appointmentService.getAppointmentsByPatientAndDate(patientId, appointmentDate);
    }

    @QueryMapping
    @PreAuthorize("hasRole('PATIENT')")
    public List<AppointmentDetailsView> myAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        return appointmentService.getAppointmentsForCurrentPatient(userDetails.getUsername());
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN','DENTIST')")
    public List<AppointmentDetailsView> pendingAppointments() {
        return appointmentService.getPendingAppointments();
    }
}

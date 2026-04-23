package cs489.asd.lab.dto;

public record PublicAppointmentRequest(
        String firstName,
        String lastName,
        String phone,
        String email,
        String dentist,
        String contactMethod,
        String appointmentDate,
        String appointmentTime,
        String reason
) {
}

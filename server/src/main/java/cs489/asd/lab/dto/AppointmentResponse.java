package cs489.asd.lab.dto;

public record AppointmentResponse(
        long appointmentId,
        String appointmentDateTime,
        long dentistId,
        long patientId,
        long surgeryId,
        String status
) {
}


package cs489.asd.lab.dto;

public record AppointmentRequest(
        long patientId,
        long dentistId,
        long surgeryId,
        String appointmentDateTime
) {
}


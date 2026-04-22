package cs489.asd.lab.dto;

public record AppointmentRequestInput(
        long patientId,
        long dentistId,
        long surgeryId,
        String appointmentDateTime
) {
}


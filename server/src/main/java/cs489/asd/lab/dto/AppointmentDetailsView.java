package cs489.asd.lab.dto;

public record AppointmentDetailsView(
        long appointmentId,
        String appointmentDateTime,
        long dentistId,
        long surgeryId,
        String status,
        String surgeryLocation,
        PatientView patient
) {
}


package cs489.asd.lab.dto;

public record AppointmentDetailsView(
        long appointmentId,
        String appointmentDateTime,
        String dentistId,
        long surgeryId,
        String status,
        String surgeryLocation,
        DentistView dentist,
        PatientView patient
) {
}

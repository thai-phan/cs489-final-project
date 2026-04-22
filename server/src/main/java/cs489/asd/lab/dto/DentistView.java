package cs489.asd.lab.dto;

public record DentistView(
        String dentistId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String specialization
) {
}

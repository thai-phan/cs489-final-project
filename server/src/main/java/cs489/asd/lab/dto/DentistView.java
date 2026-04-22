package cs489.asd.lab.dto;

public record DentistView(
        long dentistId,
        String firstName,
        String lastName,
        String phone,
        String email,
        String specialization
) {
}


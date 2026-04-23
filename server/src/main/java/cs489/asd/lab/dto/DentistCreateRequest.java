package cs489.asd.lab.dto;

public record DentistCreateRequest(
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password,
        String dentistIdNumber,
        String specialization
) {
}


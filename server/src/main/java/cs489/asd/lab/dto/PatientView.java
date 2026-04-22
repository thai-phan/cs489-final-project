package cs489.asd.lab.dto;

public record PatientView(
        long userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String mailingAddress,
        String dateOfBirth
) {
}

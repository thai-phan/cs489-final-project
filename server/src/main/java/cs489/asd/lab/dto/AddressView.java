package cs489.asd.lab.dto;

public record AddressView(
        long patientId,
        String firstName,
        String lastName,
        String email,
        String contactPhone,
        String mailingAddress,
        String city,
        String dateOfBirth
) {
}


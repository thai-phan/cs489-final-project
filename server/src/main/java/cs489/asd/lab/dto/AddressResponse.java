package cs489.asd.lab.dto;

import java.time.LocalDate;

public record AddressResponse(
        long patientId,
        String firstName,
        String lastName,
        String email,
        String contactPhone,
        String mailingAddress,
        String city,
        LocalDate dateOfBirth
) {
}


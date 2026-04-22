package cs489.asd.lab.dto;

import java.time.LocalDate;

public record PatientResponse(
        long userId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String mailingAddress,
        LocalDate dateOfBirth
) {
}

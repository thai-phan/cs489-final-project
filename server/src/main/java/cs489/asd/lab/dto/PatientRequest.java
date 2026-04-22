package cs489.asd.lab.dto;

import java.time.LocalDate;

@SuppressWarnings("unused")
public record PatientRequest(
        String firstName,
        String lastName,
        String contactPhone,
        String email,
        String mailingAddress,
        LocalDate dateOfBirth
) {
}


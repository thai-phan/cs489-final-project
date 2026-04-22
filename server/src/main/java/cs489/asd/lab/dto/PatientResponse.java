package cs489.asd.lab.dto;

import java.time.LocalDate;

public record PatientResponse(
        long patientId,
        String firstName,
        String lastName,
        String contactPhone,
        String email,
        String mailingAddress,
        LocalDate dateOfBirth
) {
}


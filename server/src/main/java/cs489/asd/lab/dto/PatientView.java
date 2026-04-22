package cs489.asd.lab.dto;

public record PatientView(
        long patientId,
        String firstName,
        String lastName,
        String contactPhone,
        String email,
        String mailingAddress,
        String dateOfBirth
) {
}


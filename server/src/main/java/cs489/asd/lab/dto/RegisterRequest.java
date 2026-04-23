package cs489.asd.lab.dto;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber,
        Boolean isAdmin
) {
}

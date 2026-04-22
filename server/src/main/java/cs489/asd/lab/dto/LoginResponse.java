package cs489.asd.lab.dto;

import java.util.List;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        String email,
        List<String> roles
) {
}

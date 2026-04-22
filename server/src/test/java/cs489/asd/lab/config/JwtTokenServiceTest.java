package cs489.asd.lab.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtTokenServiceTest {

    private static final String SECRET = "this-is-a-very-long-secret-key-for-jwt-tests-123456";

    @Test
    void generateToken_extractUsername_andValidate() {
        JwtTokenService service = new JwtTokenService(SECRET, 3600, "test-issuer");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin",
                "ignored",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = service.generateToken(auth);

        assertTrue(service.isValidToken(token));
        assertEquals("admin", service.extractUsername(token));
        assertEquals(3600, service.getExpirationSeconds());
    }

    @Test
    void invalidWhenSignedWithDifferentSecret() {
        JwtTokenService service1 = new JwtTokenService(SECRET, 3600, "test-issuer");
        JwtTokenService service2 = new JwtTokenService(
                "another-very-long-secret-key-for-jwt-tests-654321",
                3600,
                "test-issuer"
        );
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "staff",
                "ignored",
                List.of(new SimpleGrantedAuthority("ROLE_STAFF"))
        );

        String token = service1.generateToken(auth);

        assertFalse(service2.isValidToken(token));
    }

    @Test
    void expiredTokenIsInvalid() {
        JwtTokenService service = new JwtTokenService(SECRET, -1, "test-issuer");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "dentist",
                "ignored",
                List.of(new SimpleGrantedAuthority("ROLE_DENTIST"))
        );

        String token = service.generateToken(auth);

        assertFalse(service.isValidToken(token));
    }
}


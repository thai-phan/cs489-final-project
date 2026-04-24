package cs489.asd.lab.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtTokenServiceTest {

    private static final String SECRET = "change-me-to-a-very-long-secret-key-32-plus-bytes";
    private static final long EXPIRATION_SECONDS = 3600;
    private static final String ISSUER = "cs489-server";

    private final JwtTokenService jwtTokenService = new JwtTokenService(
            SECRET,
            EXPIRATION_SECONDS,
            ISSUER
    );

    @Test
    void generateToken_shouldEmbedUsernameRolesAndValidate() {
        Authentication authentication = mock(Authentication.class);
        GrantedAuthority adminAuthority = () -> "ROLE_ADMIN";
        GrantedAuthority userAuthority = () -> "ROLE_USER";
        Collection<GrantedAuthority> authorities = List.of(adminAuthority, userAuthority);

        when(authentication.getName()).thenReturn("alice");
        doReturn(authorities).when(authentication).getAuthorities();

        String token = jwtTokenService.generateToken(authentication);

        assertThat(jwtTokenService.isValidToken(token)).isTrue();
        assertThat(jwtTokenService.extractUsername(token)).isEqualTo("alice");

        Claims claims = parseClaims(token);
        assertThat(claims.getIssuer()).isEqualTo(ISSUER);
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");
        assertThat(roles).containsExactly("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void isValidToken_shouldRejectTokenSignedWithDifferentSecret() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "alice",
                "password",
                List.of(() -> "ROLE_USER")
        );

        String token = jwtTokenService.generateToken(authentication);

        JwtTokenService otherTokenService = new JwtTokenService(
                "another-secret-key-that-is-also-long-enough-for-hmac",
                EXPIRATION_SECONDS,
                ISSUER
        );

        String tamperedToken = otherTokenService.generateToken(authentication);

        assertThat(jwtTokenService.isValidToken(token)).isTrue();
        assertThat(jwtTokenService.isValidToken(tamperedToken)).isFalse();
    }

    private Claims parseClaims(String token) {
        SecretKey signingKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}


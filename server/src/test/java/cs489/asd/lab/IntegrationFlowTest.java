package cs489.asd.lab;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IntegrationFlowTest {


    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void loginAndQueryProtectedGraphql() throws Exception {
        String baseUrl = baseUrl();

        HttpResponse<String> loginResponse = postJson(baseUrl + "/adsweb/api/v1/auth/login", "{\"username\":\"patient\",\"password\":\"patient123\"}");

        assertEquals(200, loginResponse.statusCode());
        String token = extractJsonStringValue(loginResponse.body(), "accessToken");
        assertNotNull(token);
        assertTrue(token.length() > 20);
        assertEquals("Bearer", extractJsonStringValue(loginResponse.body(), "tokenType"));
        assertEquals("patient", extractJsonStringValue(loginResponse.body(), "username"));

        String graphqlQuery = "query { dentists { dentistId firstName lastName email } }";
        HttpResponse<String> graphqlResponse = postJson(
                baseUrl + "/graphql",
                "{\"query\":\"" + graphqlQuery.replace("\"", "\\\"") + "\"}",
                token
        );

        assertEquals(200, graphqlResponse.statusCode());
        assertTrue(graphqlResponse.body().contains("\"dentists\""));
        assertTrue(graphqlResponse.body().contains("\"Alice\""));
    }

    @Test
    void graphqlWithoutTokenIsRejected() throws Exception {
        String baseUrl = baseUrl();

        HttpResponse<String> response = postJson(baseUrl + "/graphql", "{\"query\":\"query { dentists { dentistId } }\"}");

        assertEquals(401, response.statusCode());
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private HttpResponse<String> postJson(String url, String jsonBody) throws IOException, InterruptedException {
        return postJson(url, jsonBody, null);
    }

    private HttpResponse<String> postJson(String url, String jsonBody, String bearerToken) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody));

        if (bearerToken != null) {
            builder.header("Authorization", "Bearer " + bearerToken);
        }

        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private String extractJsonStringValue(String json, String fieldName) {
        Matcher matcher = Pattern.compile("\"" + Pattern.quote(fieldName) + "\"\\s*:\\s*\"([^\"]*)\"").matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}



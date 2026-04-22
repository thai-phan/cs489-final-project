package cs489.asd.lab.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("service", "ADS Spring Boot MVC");
        response.put("status", "running");
        return response;
    }

    @GetMapping("/db/ping")
    public Map<String, String> pingDatabase() throws SQLException {
        Map<String, String> response = new LinkedHashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            response.put("database", connection.getMetaData().getDatabaseProductName());
            response.put("version", connection.getMetaData().getDatabaseProductVersion());
            response.put("user", connection.getMetaData().getUserName());
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("select 'ok' as status")) {
                if (resultSet.next()) {
                    response.put("query", resultSet.getString("status"));
                }
            }
        }

        return response;
    }
}


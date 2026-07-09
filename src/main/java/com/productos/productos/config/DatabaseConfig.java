package com.productos.productos.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseConfig {

    private Dotenv dotenv;

    @Bean
    @Primary
    public DataSource dataSource(Environment environment) {
        String databaseUrl = resolveProperty(environment, "DATABASE_URL");
        boolean requireSsl = Boolean.parseBoolean(resolveProperty(environment, "DB_REQUIRE_SSL", "true"));

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");

        if (hasText(databaseUrl)) {
            ParsedDatabaseUrl parsed = parseDatabaseUrl(databaseUrl, requireSsl);
            dataSource.setJdbcUrl(parsed.jdbcUrl());
            dataSource.setUsername(parsed.username());
            dataSource.setPassword(parsed.password());
            return dataSource;
        }

        dataSource.setJdbcUrl(resolveProperty(environment, "spring.datasource.url"));
        dataSource.setUsername(resolveProperty(environment, "spring.datasource.username"));
        dataSource.setPassword(resolveProperty(environment, "spring.datasource.password"));
        return dataSource;
    }

    private ParsedDatabaseUrl parseDatabaseUrl(String databaseUrl, boolean requireSsl) {
        URI uri = URI.create(databaseUrl);
        String[] userInfoParts = uri.getUserInfo().split(":", 2);

        String username = decode(userInfoParts[0]);
        String password = userInfoParts.length > 1 ? decode(userInfoParts[1]) : "";

        int port = uri.getPort() == -1 ? 5432 : uri.getPort();
        String query = uri.getQuery();

        if (requireSsl && (query == null || !query.contains("sslmode="))) {
            query = hasText(query) ? query + "&sslmode=require" : "sslmode=require";
        }

        String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();
        if (hasText(query)) {
            jdbcUrl += "?" + query;
        }

        return new ParsedDatabaseUrl(jdbcUrl, username, password);
    }

    private String resolveProperty(Environment environment, String key) {
        return resolveProperty(environment, key, null);
    }

    private String resolveProperty(Environment environment, String key, String defaultValue) {
        String envValue = environment.getProperty(key);
        if (hasText(envValue)) {
            return envValue;
        }

        String dotenvValue = getDotenvValue(key);
        if (hasText(dotenvValue)) {
            return dotenvValue;
        }

        return defaultValue;
    }

    private String getDotenvValue(String key) {
        try {
            if (dotenv == null) {
                dotenv = Dotenv.configure().ignoreIfMissing().load();
            }
            return dotenv.get(key);
        } catch (Exception ex) {
            return null;
        }
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private record ParsedDatabaseUrl(String jdbcUrl, String username, String password) {
    }
}

package com.geekhub.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(@Value("${db.host}") String host,
                                 @Value("${db.port}") String port,
                                 @Value("${db.login}") String login,
                                 @Value("${db.password}") String password,
                                 @Value("${db.database_name}") String databaseName,
                                 @Value("${db.url}") String url) {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(url);
        config.setUsername(login);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
            .outOfOrder(true)
            .locations("classpath:db/migration")
            .dataSource(dataSource)
            .load();
    }

    @Bean
    public InitializingBean flywayMigrate(Flyway flyway) {
        return flyway::migrate;
    }

    @Bean
    public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}

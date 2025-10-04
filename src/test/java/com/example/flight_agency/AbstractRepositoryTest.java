package com.example.flight_agency;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Clase base para tests de repositorios.
 * - @DataJpaTest: levanta solo la capa JPA (rápido)
 * - @Testcontainers + @ServiceConnection: autoconfigura el DataSource con Postgres 16 en contenedor
 * - Contenedor estático y compartido entre todos los tests (no se recrea en cada clase).
 */
@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

}
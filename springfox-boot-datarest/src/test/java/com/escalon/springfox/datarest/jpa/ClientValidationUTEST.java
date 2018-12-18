package com.escalon.springfox.datarest.jpa;

import liquibase.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

/**
 * @author amutsch
 * @since 12/18/2018
 */
@SpringJUnitConfig(classes = ValidationAutoConfiguration.class)
public class ClientValidationUTEST {

    @Autowired
    private Validator validator;

    @ParameterizedTest
    @MethodSource("invalidClientData")
    void invalidClientNameValidation(String clientName, TestInfo testInfo) {
        Client client = ClientTestHelper.createValidClient(testInfo);
        client.setClientName(clientName);
        validate(client, "clientName");
    }

    @ParameterizedTest
    @MethodSource("invalidClientData")
    void invalidCreatedByValidation(String createdBy, TestInfo testInfo) {
        Client client = ClientTestHelper.createValidClient(testInfo);
        client.setCreatedBy(createdBy);
        validate(client, "createdBy");
    }

    @ParameterizedTest
    @MethodSource("invalidClientData")
    void invalidUpdatedByValidation(String updatedBy, TestInfo testInfo) {
        Client client = ClientTestHelper.createValidClient(testInfo);
        client.setUpdatedBy(updatedBy);
        validate(client, "updatedBy");
    }

    private void validate(Client client, String path) {
        Set<ConstraintViolation<Client>> failures = validator.validate(client);
        Assertions.assertEquals(1, failures.size());
        Assertions.assertEquals(path, failures.iterator().next().getPropertyPath().toString());
    }

    private static Stream<String> invalidClientData() {
        return Stream.of("", StringUtils.repeat("ABCDEF", (200 / 6) + 1), null);
    }
}

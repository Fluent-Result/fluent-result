package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_ReplaceWithBoolean_Test {

    @Test
    void replaceWithBoolean_success_successfullyMapValue() {
        BooleanResult<String> result =
                VoidResult.<String>success()
                        .replaceWithBoolean(() -> true);
        result.consumeEither(
                () -> {},
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void replaceWithBoolean_error_notNull() {
        VoidResult<String> result = VoidResult.error("Error");
        BooleanResult<String> replaced = result.replaceWithBoolean(() -> true);
        assertThat(replaced).isNotNull();
    }
}
package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_MapToBoolean_Test {

    @Test
    void mapToBoolean_success_successfullyMapValue() {
        BooleanResult<String> result =
                BooleanResult.<String>success(true)
                        .mapToBoolean(val -> !val);
        result.consumeEither(
                () -> fail("Should not be empty"),
                () -> {},
                err -> fail("Expected no error"));
    }

    @Test
    void mapToBoolean_failure_notMapped() {
        BooleanResult<String> result = BooleanResult.error("Error")
                .mapToBoolean(val -> !val);
        assertThat(result).isNotNull();
        switch (result) {
            case BooleanResult.Error<String>(String error) -> {
                assertThat(error).isEqualTo("Error");
            }
            case BooleanResult.Success<String> v -> {
                fail("Expected failure");
            }
        }
    }
}
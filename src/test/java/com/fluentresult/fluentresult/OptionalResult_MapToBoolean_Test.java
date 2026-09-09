package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_MapToBoolean_Test {

    @Test
    void mapToBoolean_success_successfullyMapValue() {
        BooleanResult<String> result =
                OptionalResult.<String, String>success("Success")
                        .mapToBoolean(maybeVal -> true);
        result.consumeEither(
                val -> assertThat(val).isTrue(),
                err -> fail("Should not be error"));
    }

    @Test
    void mapToBoolean_empty_successfullyMap() {
        OptionalResult<String, String> result = OptionalResult.empty();
        BooleanResult<String> boolResult = result
                .mapToBoolean(maybeVal -> true);
        assertThat(boolResult).isNotNull();
        switch (boolResult) {
            case BooleanResult.Error<String> v -> {
                fail("Expected error");
            }
            case BooleanResult.Success<String> (boolean value) -> {
                assertThat(value).isTrue();
            }
        }
        boolResult = result
                .mapToBoolean(maybeVal -> false);
        assertThat(boolResult).isNotNull();
        switch (boolResult) {
            case BooleanResult.Error<String> v -> {
                fail("Expected error");
            }
            case BooleanResult.Success<String> (boolean value) -> {
                assertThat(value).isFalse();
            }
        }
    }


    @Test
    void mapToBoolean_error_is_error() {
        BooleanResult<String> result =
                OptionalResult.<String, String>error("Error")
                        .mapToBoolean(maybeVal -> true);
        result.consumeEither(
                val -> fail("Should be error"),
                err -> assertThat(err).isEqualTo("Error"));
    }
}
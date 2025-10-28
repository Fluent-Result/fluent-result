package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_MapToOptional_Test {

    @Test
    void mapToOptional_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .mapToOptional(maybeVal -> Optional.of(7));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void mapToOptional_empty_successfullyMapValue() {
        OptionalResult<Integer, String> result = OptionalResult.empty();
        OptionalResult<Integer, String> flatmapped = result
                .mapToOptional(opt -> opt.map(integer -> integer * 2));
        assertThat(flatmapped).isNotNull();
        switch (flatmapped) {
            case OptionalResult.Empty<Integer, String> v -> {
            }
            case OptionalResult.Error<Integer, String> v -> {
                fail("Expected empty");
            }
            case OptionalResult.Value<Integer, String>(Integer value) -> {
                fail("Expected empty");
            }
        }
    }
    @Test
    void mapToOptional_error_maps_error() {
        OptionalResult<Integer, String> result = OptionalResult.error("Error");
        OptionalResult<Integer, String> flatmapped = result
                .mapToOptional(opt -> opt.map(integer -> integer * 2));
        assertThat(flatmapped).isNotNull();
        switch (flatmapped) {
            case OptionalResult.Empty<Integer, String> v -> {
                fail("Expected error");
            }
            case OptionalResult.Error<Integer, String> v -> {

            }
            case OptionalResult.Value<Integer, String>(Integer value) -> {
                fail("Expected empty");
            }
        }
    }
}

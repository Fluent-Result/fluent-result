package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_MapToOptional_Test {

    @Test
    void mapToOptional_success_successfullyMapValue() {
        OptionalResult<Integer, String> result =
                BooleanResult.<String>success(true)
                        .mapToOptional(val -> Optional.of(val ? 5 : 3));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(5),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error"));
    }

    @Test
    void mapToOptional_error_noNull() {
        OptionalResult<Integer, String> result =
                BooleanResult.error("Error")
                        .mapToOptional(val -> Optional.of(val ? 5 : 3));
        assertThat(result).isNotNull();
        switch (result) {
            case OptionalResult.Empty<Integer, String> v -> {
                fail("Should be error");
            }
            case OptionalResult.Error<Integer, String>(String e) -> {
                assertThat(e).isEqualTo("Error");
            }
            case OptionalResult.Value<Integer, String> v -> {
                fail("Should be error");
            }
        }
    }
}
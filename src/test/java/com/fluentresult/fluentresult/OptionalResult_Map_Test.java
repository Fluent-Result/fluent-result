package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_Map_Test {

    @Test
    void map_success_successfullyMapValue() {
        Result<Integer, String> result =
                OptionalResult.<String, String>success("Success")
                        .map(maybeVal -> maybeVal.map(String::length).orElse(4));
        result.consumeEither(
                val -> assertThat(val).isEqualTo(7),
                err -> fail("Should not be error"));
    }

    @Test
    void map_empty_successfullyMap() {
        Result<String, String> result = OptionalResult.<String,String>empty()
                .map(o -> o.orElse("OrElse"));
        assertThat(result).isNotNull();
        switch (result) {
            case Result.Error<String, String> v -> {
                fail("Expected value");
            }
            case Result.Success<String, String>(String value) -> {
                assertThat(value).isEqualTo("OrElse");
            }
        }
    }
    @Test
    void map_error_successfullyMap() {
        Result<String, String> result = OptionalResult.<String,String>error("Error")
                .map(o -> o.orElse("OrElse"));
        assertThat(result).isNotNull();
        switch (result) {
            case Result.Error<String, String>(String error) -> {
                assertThat(error).isEqualTo("Error");
            }
            case Result.Success<String, String>(String value) -> {
                fail("Expected error");
            }
        }
    }
}
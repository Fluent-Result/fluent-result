package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

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

    @Test
    void map_functionAcceptingSupertypeOfOptional_canBeUsed() {
        // Reusable function declared for any optional of a Number subtype. It is
        // a Function<Optional<? extends Number>, String>, which is not a
        // Function<Optional<Integer>, String>, but is a
        // Function<? super Optional<Integer>, String>.
        Function<Optional<? extends Number>, String> describe =
                o -> o.map(n -> "number " + n).orElse("no number");

        Result<String, String> present = OptionalResult.<Integer, String>success(1).map(describe);
        Result<String, String> empty = OptionalResult.<Integer, String>empty().map(describe);

        assertThat(present).isEqualTo(Result.success("number 1"));
        assertThat(empty).isEqualTo(Result.success("no number"));
    }
}

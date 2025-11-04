package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_FlatMapValueWithBooleanResult_Test {

    @Test
    void flatMapValueWithBooleanResult_success_successfullyMapValue() {
        OptionalResult<Boolean, String> result =
                OptionalResult.<String, String>success("Success")
                        .flatMapValueWithBooleanResult(val -> BooleanResult.success(val.isEmpty()));
        result.consumeEither(
                val -> assertThat(val).isFalse(),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void flatMapValueWithBooleanResult_empty_notNull() {
        OptionalResult<Integer, String> result = OptionalResult.empty();
        OptionalResult<Boolean, String> flatmapped = result.flatMapValueWithBooleanResult(integer -> BooleanResult.success(integer == 2));
        assertThat(flatmapped).isNotNull();
    }

    @Test
    void flatMapValueWithBooleanResult_error_notNull() {
        OptionalResult<Integer, String> result = OptionalResult.error("Error");
        OptionalResult<Boolean, String> flatmapped = result.flatMapValueWithBooleanResult(integer -> BooleanResult.success(integer == 2));
        assertThat(flatmapped).isNotNull();
    }


}
package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_FlatMapToBooleanResult_Test {

    @Test
    void flatMapToBooleanResult_success_shouldFlatMapValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.flatMapToBooleanResult(
                maybeVal -> BooleanResult.success(maybeVal.map(String::isEmpty).orElse(true)))
                .consumeEither(
                        val -> assertThat(val).isFalse(),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatMap_empty_notNull() {
        OptionalResult<Integer, String> result = OptionalResult.empty();
        BooleanResult<String> flatmapped = result.flatMapToBooleanResult(integer -> BooleanResult.success(integer.isEmpty()));
        assertThat(flatmapped).isNotNull();
    }

    @Test
    void flatMapWithBooleanResult_error_shouldBeError() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        BooleanResult<String> flatMapped = result.flatMapToBooleanResult(s -> BooleanResult.success(s.isEmpty()));
        assertThat(flatMapped).isNotNull();
    }
}
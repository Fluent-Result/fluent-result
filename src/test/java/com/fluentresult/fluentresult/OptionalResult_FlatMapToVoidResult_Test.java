package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_FlatMapToVoidResult_Test {

    @Test
    void flatMapToVoidResult_success_shouldFlatMapValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.flatMapToVoidResult(
                maybeVal -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatMapToVoidResult_empty_notNull() {
        OptionalResult<Integer, String> result = OptionalResult.empty();
        VoidResult<String> flatmapped = result.flatMapToVoidResult(integer -> VoidResult.success());
        assertThat(flatmapped).isNotNull();
    }

    @Test
    void flatMapToVoidResult_error_shouldBeError() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        VoidResult<String> flatMapped = result.flatMapToVoidResult(s -> VoidResult.success());
        assertThat(flatMapped).isNotNull();
    }
}
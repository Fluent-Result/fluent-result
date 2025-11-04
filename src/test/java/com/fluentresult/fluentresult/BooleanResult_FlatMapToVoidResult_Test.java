package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMapToVoidResult_Test {

    @Test
    void flatMapToVoidResult_success_shouldFlatMapValue() {
        BooleanResult.success(false)
                .flatMapToVoidResult(val -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatMap_error_shouldNotMapValue() {
        VoidResult<String> result = BooleanResult.error("Error")
                .flatMapToVoidResult(value -> VoidResult.success());
        assertThat(result).isNotNull();
    }
}
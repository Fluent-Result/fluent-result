package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_FlatMapToVoidResult_Test {

    @Test
    void flatMapToVoidResult_success_shouldFlatMapValue() {
        Result<String, String> result = Result.success("Success");
        result.flatMapToVoidResult(val -> VoidResult.success())
                .consumeEither(
                        () -> {},
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatMapToVoidResult_error_notNull() {
        VoidResult<String> result = Result.error("Error")
                .flatMapToVoidResult(o -> VoidResult.success());
        assertThat(result).isNotNull();
    }
}
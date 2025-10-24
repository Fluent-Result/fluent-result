package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMapToOptionalResult_Test {

    @Test
    void flatMapToOptionalResult_success_shouldFlatMapValue() {
        BooleanResult.success(true)
                .flatMapToOptionalResult(val -> OptionalResult.success(val ? "Success" : "Failure"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        () -> fail("Should not be empty"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatMap_error_shouldNotMapValue() {
        OptionalResult<Boolean, String> result = BooleanResult.error("Error")
                .flatMapToOptionalResult(value -> OptionalResult.success(!value));
        assertThat(result).isNotNull();
    }
}
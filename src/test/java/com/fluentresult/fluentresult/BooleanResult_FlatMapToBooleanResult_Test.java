package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMapToBooleanResult_Test {

    @Test
    void flatMapToBooleanResult_success_shouldFlatMapValue() {
        BooleanResult.success(true)
                .flatMapToBooleanResult(val -> BooleanResult.success(!val))
                .consumeEither(
                        () -> fail("Should not be empty"),
                        () -> {},
                        err -> fail("Should not be error")
                );
    }


    @Test
    void flatMap_error_shouldNotMapValue() {
        BooleanResult<String> result = BooleanResult.error("Error")
                .flatMapToBooleanResult(value -> BooleanResult.success(!value));
        assertThat(result).isNotNull();
    }
}
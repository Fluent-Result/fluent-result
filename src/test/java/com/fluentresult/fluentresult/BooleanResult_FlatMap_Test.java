package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatMap_Test {

    @Test
    void flatMap_success_shouldFlatMapValue() {
        BooleanResult.success(true)
                .flatMap(val -> Result.success(val ? "Success" : "Failure"))
                .consumeEither(
                        val -> assertThat(val).isEqualTo("Success"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatMap_error_shouldNotMapValue() {
        Result<Boolean, String> result = BooleanResult.error("Error")
                .flatMap(value -> Result.success(!value));
        assertThat(result).isNotNull();
    }
}
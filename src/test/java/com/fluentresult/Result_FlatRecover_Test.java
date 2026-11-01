package com.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class Result_FlatRecover_Test {

    @Test
    void flatRecover_success_shouldReturnValue() {
        Result<String, String> result = Result.success("Success");
        result.flatRecover(error -> Result.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo("Success"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRecover() {
        Result<String, String> result = Result.error("Error");
        result.flatRecover(error -> Result.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo("replaced error Error"),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRecover_change_error_type() {
        Result<String, String> result = Result.error("Error");
        Result<String, Integer> result2 = result.flatRecover(error -> Result.error(error.length()))
                .consumeEither(
                        value -> fail("should be error"),
                        err -> { }
                );
    }
}

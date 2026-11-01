package com.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_FlatRecover_Test {

    @Test
    void flatRecover_success_shouldRemainExistingSuccess() {
        BooleanResult<String> result = BooleanResult.success(false);
        BooleanResult<String> shouldNotBeError = result.flatRecover(error -> BooleanResult.<String>success(true))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(false),
                        err -> fail("Should not be error")
                );
        assertThat(shouldNotBeError).isNotNull();
    }

    @Test
    void flatRecover_error_shouldApplyRecoverFunction() {
        BooleanResult<String> result = BooleanResult.error("Error");
        result.flatRecover(error -> BooleanResult.success(true))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(true),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRecover_error_shouldApplyRecoverFunction_change_Error_type() {
        BooleanResult<String> result = BooleanResult.error("Error");
        BooleanResult<Boolean> result2 = result.flatRecover(error -> BooleanResult.error(true))
                .consumeEither(
                        value -> fail("Should not be error"),
                        err -> assertThat(err).isEqualTo(true)
                );
    }
}

package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BooleanResult_OrElseThrow_Test {

    @Test
    void orElseThrow_success_shouldRespondWithSuccessValueTrue() {
        BooleanResult<String> result = BooleanResult.success(true);
        Boolean orElse = result.orElseThrow(IllegalArgumentException::new);
        assertThat(orElse).isTrue();
    }

    @Test
    void orElseThrow_success_shouldRespondWithSuccessValueFalse() {
        BooleanResult<String> result = BooleanResult.success(false);
        Boolean orElse = result.orElseThrow(IllegalArgumentException::new);
        assertThat(orElse).isFalse();
    }

    @Test
    void orElseThrow_error_shouldThrow() {
        BooleanResult<String> result = BooleanResult.error("Error");
        assertThatThrownBy(() -> result.orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
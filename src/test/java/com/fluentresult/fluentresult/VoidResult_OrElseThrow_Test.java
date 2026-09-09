package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VoidResult_OrElseThrow_Test {

    @Test
    void orElseThrow_success_shouldRespondWithSuccessValue() {
        VoidResult<String> result = VoidResult.success();
        result.orElseThrow(IllegalArgumentException::new);
    }

    @Test
    void orElseThrow_error_shouldThrow() {
        VoidResult<String> result = VoidResult.error("Error");
        assertThatThrownBy(() -> result.orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
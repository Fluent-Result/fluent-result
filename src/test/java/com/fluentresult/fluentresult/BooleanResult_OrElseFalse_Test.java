package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_OrElseFalse_Test {

    @Test
    void orElseFalse_error_shouldRespondWithSuccessValue() {
        BooleanResult<String> result = BooleanResult.error("Error");
        boolean orElse = result.orElseFalse();
        assertThat(orElse).isFalse();
    }

    @Test
    void orElseTrue_success_TrueReturnTrue() {
        BooleanResult<String> result = BooleanResult.success(true);
        boolean orElse = result.orElseFalse();
        assertThat(orElse).isTrue();;
    }

    @Test
    void orElseTrue_success_FalseReturnFalse() {
        BooleanResult<String> result = BooleanResult.success(false);
        boolean orElse = result.orElseFalse();
        assertThat(orElse).isFalse();;
    }
}
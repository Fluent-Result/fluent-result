package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_OrElseTrue_Test {

    @Test
    void orElseTrue_error_shouldRespondWithSuccessValue() {
        BooleanResult<String> result = BooleanResult.error("Error");
        boolean orElse = result.orElseTrue();
        assertThat(orElse).isTrue();
    }

    @Test
    void orElseTrue_success_TrueReturnTrue() {
        BooleanResult<String> result = BooleanResult.success(true);
        boolean orElse = result.orElseTrue();
        assertThat(orElse).isTrue();;
    }

    @Test
    void orElseTrue_success_FalseReturnFalse() {
        BooleanResult<String> result = BooleanResult.success(false);
        boolean orElse = result.orElseTrue();
        assertThat(orElse).isFalse();;
    }
}
package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_OrElse_Test {

    @Test
    void orElse_success_shouldRespondWithSuccessValue() {
        BooleanResult<Object> result = BooleanResult.success(true);
        Boolean orElse = result.orElse(false);
        assertThat(orElse).isTrue();

        result = BooleanResult.success(false);
        orElse = result.orElse(true);
        assertThat(orElse).isFalse();
    }

    @Test
    void orElse_error_shouldRespondWithOtherValue() {
        BooleanResult<String> result = BooleanResult.error("Error");
        Boolean orElse = result.orElse(false);
        assertThat(orElse).isFalse();
        orElse = result.orElse(true);
        assertThat(orElse).isTrue();
    }

}
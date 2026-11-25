package com.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BooleanResult_OrElseGet_Test {

    @Test
    void orElseGet_success_shouldRespondWithSuccessValueTrue() {
        BooleanResult<String> result = BooleanResult.success(true);
        Boolean orElse = result.orElseGet(err -> false);
        assertThat(orElse).isTrue();
    }

    @Test
    void orElseGet_success_shouldRespondWithSuccessValueFalse() {
        BooleanResult<String> result = BooleanResult.success(false);
        Boolean orElse = result.orElseGet(err -> true);
        assertThat(orElse).isFalse();
    }

    @Test
    void orElseGet_error_ShouldReturnFunctionValue() {
        BooleanResult<String> result = BooleanResult.error("Error");
        Boolean b = result.orElseGet(s -> false);
        assertThat(b).isFalse();
        b = result.orElseGet(s -> true);
        assertThat(b).isTrue();
    }

    @Test
    void orElseGet_error_functionReturningNull_shouldThrowNullPointerException() {
        BooleanResult<String> result = BooleanResult.error("Error");
        assertThatThrownBy(() -> result.orElseGet(s -> null))
                .isInstanceOf(NullPointerException.class);
    }
}
package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_OrElseGet_Test {

    @Test
    void orElseGet_success_shouldRespondWithSuccessValue() {
        OptionalResult<String, Object> result = OptionalResult.success("Success");
        Optional<String> orElse = result.orElseGet(err -> Optional.of("Other"));
        assertThat(orElse).isPresent();
        orElse.ifPresent(val -> assertThat(val).isEqualTo("Success"));
    }

    @Test
    void orElseGet_empty_shouldRespondWithEmptyValue() {
        OptionalResult<String, Object> result = OptionalResult.empty();
        Optional<String> orElse = result.orElseGet(err -> Optional.of("Other"));
        assertThat(orElse).isEmpty();
    }

    @Test
    void orElseGet_error_shouldRespondWithEmptyValue() {
        OptionalResult<String, Object> result = OptionalResult.error("Error");
        Optional<String> orElse = result.orElseGet(err -> Optional.of("Other"));
        assertThat(orElse).isEqualTo(Optional.of("Other"));
    }
}
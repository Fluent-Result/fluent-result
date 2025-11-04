package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionalResult_Fold_2Args_Test {

    @Test
    void fold_success_shouldGiveSuccessValueFunctionResult() {
        OptionalResult<String, Integer> result = OptionalResult.success("Success");
        Integer folded = result.fold(
                maybeVal -> maybeVal.map(String::length).orElse(3),
                err -> err);
        assertThat(folded).isEqualTo(7);
    }

    @Test
    void fold_empty_shouldGiveSuccessValueFunctionResult() {
        OptionalResult<String, Integer> result = OptionalResult.empty();
        Integer folded = result.fold(
                maybeVal -> maybeVal.map(String::length).orElse(3),
                err -> err);
        assertThat(folded).isEqualTo(3);
    }

    @Test
    void fold_error_shouldGiveSuccessValueFunctionResult() {
        OptionalResult<String, Integer> result = OptionalResult.error(321);
        Integer folded = result.fold(
                maybeVal -> maybeVal.map(String::length).orElse(3),
                err -> err);
        assertThat(folded).isEqualTo(321);
    }
}
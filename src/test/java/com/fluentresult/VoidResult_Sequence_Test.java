package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_Sequence_Test {

    @Test
    void sequence_emptyResults_shouldGiveSuccess() {
        VoidResult<String> result = VoidResult.sequence(List.<VoidResult<String>>of());
        assertThat(result).isEqualTo(VoidResult.success());
    }

    @Test
    void sequence_allSuccess_shouldGiveSuccess() {
        VoidResult<String> result = VoidResult.sequence(List.of(VoidResult.success(), VoidResult.success()));
        assertThat(result).isEqualTo(VoidResult.success());
    }

    @Test
    void sequence_errors_shouldGiveFirstError() {
        VoidResult<String> result = VoidResult.sequence(List.of(
                VoidResult.success(), VoidResult.error("error1"), VoidResult.error("error2")));
        assertThat(result).isEqualTo(VoidResult.error("error1"));
    }

    @Test
    void sequence_nullResultsGivesNPE() {
        assertThatThrownBy(() -> VoidResult.sequence(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void sequence_nullElementGivesNPE() {
        assertThatThrownBy(() -> VoidResult.sequence(Arrays.asList(VoidResult.success(), null)))
                .isInstanceOf(NullPointerException.class);
    }
}

package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_SequenceAll_Test {

    @Test
    void sequenceAll_emptyResults_shouldGiveSuccess() {
        VoidResult<List<String>> result = VoidResult.sequenceAll(List.<VoidResult<String>>of());
        assertThat(result).isEqualTo(VoidResult.success());
    }

    @Test
    void sequenceAll_allSuccess_shouldGiveSuccess() {
        VoidResult<List<String>> result = VoidResult.sequenceAll(List.<VoidResult<String>>of(
                VoidResult.success(), VoidResult.success()));
        assertThat(result).isEqualTo(VoidResult.success());
    }

    @Test
    void sequenceAll_errors_shouldGiveAllErrorsInOrder() {
        VoidResult<List<String>> result = VoidResult.sequenceAll(List.of(
                VoidResult.error("error1"), VoidResult.success(), VoidResult.error("error2")));
        assertThat(result).isEqualTo(VoidResult.error(List.of("error1", "error2")));
    }

    @Test
    void sequenceAll_nullResultsGivesNPE() {
        assertThatThrownBy(() -> VoidResult.sequenceAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void sequenceAll_nullElementGivesNPE() {
        assertThatThrownBy(() -> VoidResult.sequenceAll(Arrays.asList(VoidResult.success(), null)))
                .isInstanceOf(NullPointerException.class);
    }
}

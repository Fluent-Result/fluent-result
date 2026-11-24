package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_SequenceAll_Test {

    @Test
    void sequenceAll_emptyResults_shouldGiveSuccessWithEmptyList() {
        Result<List<Integer>, List<String>> result = Result.sequenceAll(List.<Result<Integer, String>>of());
        assertThat(result).isEqualTo(Result.success(List.of()));
    }

    @Test
    void sequenceAll_allSuccess_shouldGiveSuccessWithValuesInOrder() {
        Result<List<Integer>, List<String>> result = Result.sequenceAll(List.of(
                Result.success(1), Result.success(2), Result.success(3)));
        assertThat(result).isEqualTo(Result.success(List.of(1, 2, 3)));
    }

    @Test
    void sequenceAll_errors_shouldGiveAllErrorsInOrder() {
        Result<List<Integer>, List<String>> result = Result.sequenceAll(List.of(
                Result.error("error1"), Result.success(1), Result.error("error2")));
        assertThat(result).isEqualTo(Result.error(List.of("error1", "error2")));
    }

    @Test
    void sequenceAll_nullResultsGivesNPE() {
        assertThatThrownBy(() -> Result.sequenceAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void sequenceAll_nullElementGivesNPE() {
        assertThatThrownBy(() -> Result.sequenceAll(Arrays.asList(Result.success(1), null)))
                .isInstanceOf(NullPointerException.class);
    }
}

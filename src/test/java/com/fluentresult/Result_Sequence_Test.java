package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_Sequence_Test {

    @Test
    void sequence_emptyResults_shouldGiveSuccessWithEmptyList() {
        Result<List<Integer>, String> result = Result.sequence(List.<Result<Integer, String>>of());
        assertThat(result).isEqualTo(Result.success(List.of()));
    }

    @Test
    void sequence_allSuccess_shouldGiveSuccessWithValuesInOrder() {
        Result<List<Integer>, String> result = Result.sequence(List.of(
                Result.success(1), Result.success(2), Result.success(3)));
        assertThat(result).isEqualTo(Result.success(List.of(1, 2, 3)));
    }

    @Test
    void sequence_errors_shouldGiveFirstError() {
        Result<List<Integer>, String> result = Result.sequence(List.of(
                Result.success(1), Result.error("error1"), Result.error("error2")));
        assertThat(result).isEqualTo(Result.error("error1"));
    }

    @Test
    void sequence_nullResultsGivesNPE() {
        assertThatThrownBy(() -> Result.sequence(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void sequence_nullElementGivesNPE() {
        assertThatThrownBy(() -> Result.sequence(Arrays.asList(Result.success(1), null)))
                .isInstanceOf(NullPointerException.class);
    }
}

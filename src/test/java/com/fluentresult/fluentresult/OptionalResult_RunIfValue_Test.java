package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_RunIfValue_Test {

    @Test
    void run_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult =
                result.runIfValue(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void run_empty_consumerShouldNotRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> runIfValue = result.runIfValue(() -> resultList.add("Ran"));
        assertThat(runIfValue).isNotNull();
        assertThat(resultList).isEmpty();
    }

    @Test
    void run_error_consumerShouldNotRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.error("Error");
        result.runIfValue(() -> resultList.add("Ran"));
        assertThat(resultList).isEmpty();
    }

    @Test
    void run_success_nullRunnableGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.runIfValue(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void runIfValue_error_consumerShoulNotdRun() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> consumedEither = result.runIfValue(() -> fail("Expected error"));
        assertThat(consumedEither).isNotNull();
    }
}
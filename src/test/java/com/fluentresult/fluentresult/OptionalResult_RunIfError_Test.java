package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_RunIfError_Test {

    @Test
    void runIfError_success_consumerShouldBeRun() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult =
                result.runIfError(() -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfError_empty_consumerShouldNotBeRun() {
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> finalResult =
                result.runIfError(() -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfError_error_consumerShouldBeRun() {
        List<String> values = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> finalResult =
                result.runIfError(() -> values.add("Ran"));
        assertThat(finalResult).isNotNull();
        assertThat(values).isEqualTo(List.of("Ran"));
    }
}
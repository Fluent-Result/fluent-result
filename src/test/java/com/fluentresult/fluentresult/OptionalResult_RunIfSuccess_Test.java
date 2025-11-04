package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_RunIfSuccess_Test {

    @Test
    void runIfSuccess_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult =
                result.runIfSuccess(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfSuccess_empty_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> consumedEither = result.runIfSuccess(() -> resultList.add("Ran"));
        assertThat(consumedEither).isNotNull();
        assertThat(resultList).isEqualTo(List.of("Ran"));
    }

    @Test
    void runIfSuccess_error_consumerShoulNotdRun() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> consumedEither = result.runIfSuccess(() -> fail("Expected error"));
        assertThat(consumedEither).isNotNull();
    }
}
package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_ConsumeError_Test {

    @Test
    void consumeError_success_consumerShouldNotBeRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult =
                result.consumeError(resultList::add);
        assertThat(resultList.size()).isZero();
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeValue_empty_consumerShouldNotBeRun() {
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> finalResult =
                result.consumeError(val -> fail("Should not be run"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeValue_error_consumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> finalResult =
                result.consumeError(resultList::add);
        assertThat(finalResult).isNotNull();
        assertThat(resultList).isEqualTo(List.of("Error"));
    }
}
package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_Consume_Test {

    @Test
    void consume_success_consumerShouldBeRun() {
        List<Optional<String>> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.success("Success");
        OptionalResult<String, String> finalResult = result.consume(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isPresent();
        resultList.get(0).ifPresent(val -> assertThat(val).isEqualTo("Success"));
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consume_error_consumerNotRun() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        OptionalResult<String, String> consumed = result.consume(s -> fail("Expected error"));
        assertThat(consumed).isNotNull();
    }

    @Test
    void consume_empty_consumerNotRun() {
        List<Optional<String>> resultList = new ArrayList<>();
        OptionalResult<String, String> result = OptionalResult.empty();
        OptionalResult<String, String> consumed = result.consume(resultList::add);
        assertThat(consumed).isNotNull();
        assertThat(resultList).isEqualTo(List.of(Optional.empty()));
    }
}
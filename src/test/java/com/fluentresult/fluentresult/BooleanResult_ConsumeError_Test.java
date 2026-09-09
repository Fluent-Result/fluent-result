package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_ConsumeError_Test {

    @Test
    void consumeError_error_consumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.error("Error");
        BooleanResult<String> finalResult = result.consumeError(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Error");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeError_success_consumerShouldNotBeRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult = result.consumeError(resultList::add);
        assertThat(resultList.size()).isZero();
        assertThat(finalResult).isNotNull();
    }
}
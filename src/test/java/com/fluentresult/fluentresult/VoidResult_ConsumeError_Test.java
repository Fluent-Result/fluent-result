package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_ConsumeError_Test {

    @Test
    void consumeError_success_consumerShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        VoidResult<String> result = VoidResult.error("Error");
        VoidResult<String> finalResult = result.consumeError(resultList::add);
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Error");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeError_success_notNull() {
        VoidResult<Object> result = VoidResult.success()
                .consumeError(o -> fail("Expected success"));
        assertThat(result).isNotNull();
    }
}
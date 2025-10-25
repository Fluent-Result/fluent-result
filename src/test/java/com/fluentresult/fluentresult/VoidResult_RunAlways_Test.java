package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_RunAlways_Test {

    @Test
    void runAlways_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        VoidResult<String> result = VoidResult.success();
        VoidResult<String> finalResult =
                result.runAlways(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runAlways_error_errorRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();

        VoidResult<String> result = VoidResult.error("Error")
                .runAlways(() -> resultList.add("Ran"));
        assertThat(result).isNotNull();
        assertThat(resultList).isEqualTo(List.of("Ran"));
    }
}
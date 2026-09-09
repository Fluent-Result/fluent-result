package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BooleanResult_RunIfError_Test {

    @Test
    void runIfError_error_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.error("Error");
        BooleanResult<String> finalResult =
                result.runIfError(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfError_success_consumerShouldNotRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(true);
        BooleanResult<String> finalResult =
                result.runIfError(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isZero();
        assertThat(finalResult).isNotNull();
    }
}
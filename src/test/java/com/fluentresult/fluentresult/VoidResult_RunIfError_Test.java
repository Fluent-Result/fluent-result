package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_RunIfError_Test {

    @Test
    void runIfError_success_runnableShouldBeRun() {
        List<String> resultList = new ArrayList<>();
        VoidResult<String> result = VoidResult.error("Error");
        VoidResult<String> finalResult =
                result.runIfError(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfError_success_notNull() {
        VoidResult<Object> result = VoidResult.success()
                .runIfError(() -> fail("Expected success"));
        assertThat(result).isNotNull();
    }
}
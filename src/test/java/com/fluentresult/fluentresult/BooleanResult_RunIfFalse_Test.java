package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

class BooleanResult_RunIfFalse_Test {

    @Test
    void runIfFalse_success_consumerShouldRun() {
        List<String> resultList = new ArrayList<>();
        BooleanResult<String> result = BooleanResult.success(false);
        BooleanResult<String> finalResult =
                result.runIfFalse(() -> resultList.add("Ran"));
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runIfFalse_error_notNull() {
        BooleanResult<String> result = BooleanResult.error("Error")
                .runIfFalse(() -> fail("Expected error"));
        assertThat(result).isNotNull();
    }
}
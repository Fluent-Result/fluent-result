package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_RunEither_Test {

    @Test
    void runEither_success_successRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();
        VoidResult<String> result = VoidResult.success();
        VoidResult<String> finalResult = result.runEither(
                () -> resultList.add("Ran"),
                () -> fail("Should not run"));
        assertThat(resultList).hasSize(1);
        assertThat(resultList.get(0)).isEqualTo("Ran");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void runEither_error_errorRunnableShouldRun() {
        List<String> resultList = new ArrayList<>();

        VoidResult<String> result = VoidResult.error("Error")
                .runEither(
                        () -> fail("Expected error"),
                        () -> resultList.add("Ran"));
        assertThat(result).isNotNull();
        assertThat(resultList).isEqualTo(List.of("Ran"));
    }
}
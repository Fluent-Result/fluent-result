package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

class VoidResult_ConsumeEither_Test {

    @Test
    void consumeEither_success_shouldRunSuccessRunnable() {
        List<String> resultList = new ArrayList<>();
        VoidResult<String> result = VoidResult.success();
        VoidResult<String> finalResult = result.consumeEither(
                () -> resultList.add("Success"),
                err -> { throw new RuntimeException(); });
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0)).isEqualTo("Success");
        assertThat(finalResult).isNotNull();
    }

    @Test
    void consumeEither_error_shouldRunErrorRunnable() {
        VoidResult<String> result = VoidResult.error("Error");
        List<String> resultList = new ArrayList<>();

        VoidResult<String> either = result.consumeEither(() -> fail("Expected error"), resultList::add);
        assertThat(either).isNotNull();
        assertThat(resultList).isEqualTo(List.of("Error"));
    }
}
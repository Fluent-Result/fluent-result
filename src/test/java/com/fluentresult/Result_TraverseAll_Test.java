package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_TraverseAll_Test {

    @Test
    void traverseAll_emptyItems_shouldGiveSuccessWithEmptyList() {
        Result<List<Integer>, List<String>> result = Result.traverseAll(List.<String>of(), s -> Result.success(s.length()));
        assertThat(result).isEqualTo(Result.success(List.of()));
    }

    @Test
    void traverseAll_allSuccess_shouldGiveSuccessWithValuesInOrder() {
        Result<List<Integer>, List<String>> result = Result.traverseAll(List.of("a", "bb", "ccc"), s -> Result.success(s.length()));
        assertThat(result).isEqualTo(Result.success(List.of(1, 2, 3)));
    }

    @Test
    void traverseAll_errors_shouldGiveAllErrorsInOrderAndApplyFunctionToAllItems() {
        List<String> applied = new ArrayList<>();
        Result<List<Integer>, List<String>> result = Result.traverseAll(List.of("a", "error1", "b", "error2"), s -> {
            applied.add(s);
            return s.startsWith("error") ? Result.error(s) : Result.success(s.length());
        });
        assertThat(result).isEqualTo(Result.error(List.of("error1", "error2")));
        assertThat(applied).containsExactly("a", "error1", "b", "error2");
    }

    @Test
    void traverseAll_successList_shouldBeUnmodifiable() {
        Result<List<Integer>, List<String>> result = Result.traverseAll(List.of("a"), s -> Result.success(s.length()));
        assertThatThrownBy(() -> result.orElseThrow(e -> new IllegalStateException()).add(2))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void traverseAll_errorList_shouldBeUnmodifiable() {
        Result<List<Integer>, List<String>> result = Result.traverseAll(List.of("a"), s -> Result.error(s));
        List<String> errors = result.fold(v -> null, e -> e);
        assertThatThrownBy(() -> errors.add("b"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void traverseAll_nullItemsGivesNPE() {
        assertThatThrownBy(() -> Result.traverseAll(null, s -> Result.success(s)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverseAll_nullFunctionGivesNPE() {
        assertThatThrownBy(() -> Result.traverseAll(List.of(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverseAll_nullReturnedFromFunctionGivesNPE() {
        assertThatThrownBy(() -> Result.traverseAll(List.of("a"), s -> null))
                .isInstanceOf(NullPointerException.class);
    }
}

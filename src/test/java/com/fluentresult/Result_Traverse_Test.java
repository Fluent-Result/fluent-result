package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class Result_Traverse_Test {

    @Test
    void traverse_emptyItems_shouldGiveSuccessWithEmptyList() {
        Result<List<Integer>, String> result = Result.traverse(List.<String>of(), s -> Result.success(s.length()));
        assertThat(result).isEqualTo(Result.success(List.of()));
    }

    @Test
    void traverse_allSuccess_shouldGiveSuccessWithValuesInOrder() {
        Result<List<Integer>, String> result = Result.traverse(List.of("a", "bb", "ccc"), s -> Result.success(s.length()));
        assertThat(result).isEqualTo(Result.success(List.of(1, 2, 3)));
    }

    @Test
    void traverse_error_shouldGiveFirstErrorAndNotApplyFunctionToRemainingItems() {
        List<String> applied = new ArrayList<>();
        Result<List<Integer>, String> result = Result.traverse(List.of("a", "error1", "error2", "b"), s -> {
            applied.add(s);
            return s.startsWith("error") ? Result.error(s) : Result.success(s.length());
        });
        assertThat(result).isEqualTo(Result.error("error1"));
        assertThat(applied).containsExactly("a", "error1");
    }

    @Test
    void traverse_successList_shouldBeUnmodifiable() {
        Result<List<Integer>, String> result = Result.traverse(List.of("a"), s -> Result.success(s.length()));
        assertThatThrownBy(() -> result.orElseThrow(IllegalStateException::new).add(2))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void traverse_nullItemsGivesNPE() {
        assertThatThrownBy(() -> Result.traverse(null, s -> Result.success(s)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverse_nullFunctionGivesNPE() {
        assertThatThrownBy(() -> Result.traverse(List.of(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverse_nullReturnedFromFunctionGivesNPE() {
        assertThatThrownBy(() -> Result.traverse(List.of("a"), s -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverse_nullItemIsPassedToFunction() {
        Result<List<String>, String> result = Result.traverse(Arrays.asList("a", null), s -> Result.success(String.valueOf(s)));
        assertThat(result).isEqualTo(Result.success(List.of("a", "null")));
    }
}

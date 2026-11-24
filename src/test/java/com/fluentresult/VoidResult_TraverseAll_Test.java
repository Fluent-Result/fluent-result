package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_TraverseAll_Test {

    @Test
    void traverseAll_emptyItems_shouldGiveSuccess() {
        VoidResult<List<String>> result = VoidResult.traverseAll(List.<String>of(), s -> VoidResult.success());
        assertThat(result).isEqualTo(VoidResult.success());
    }

    @Test
    void traverseAll_allSuccess_shouldGiveSuccess() {
        VoidResult<List<String>> result = VoidResult.traverseAll(List.of("a", "b"), s -> VoidResult.success());
        assertThat(result).isEqualTo(VoidResult.success());
    }

    @Test
    void traverseAll_errors_shouldGiveAllErrorsInOrderAndApplyFunctionToAllItems() {
        List<String> applied = new ArrayList<>();
        VoidResult<List<String>> result = VoidResult.traverseAll(List.of("a", "error1", "b", "error2"), s -> {
            applied.add(s);
            return s.startsWith("error") ? VoidResult.error(s) : VoidResult.success();
        });
        assertThat(result).isEqualTo(VoidResult.error(List.of("error1", "error2")));
        assertThat(applied).containsExactly("a", "error1", "b", "error2");
    }

    @Test
    void traverseAll_errorList_shouldBeUnmodifiable() {
        VoidResult<List<String>> result = VoidResult.traverseAll(List.of("a"), s -> VoidResult.error(s));
        List<String> errors = result.fold(() -> null, e -> e);
        assertThatThrownBy(() -> errors.add("b"))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void traverseAll_nullItemsGivesNPE() {
        assertThatThrownBy(() -> VoidResult.traverseAll(null, s -> VoidResult.success()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverseAll_nullFunctionGivesNPE() {
        assertThatThrownBy(() -> VoidResult.traverseAll(List.of(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverseAll_nullReturnedFromFunctionGivesNPE() {
        assertThatThrownBy(() -> VoidResult.traverseAll(List.of("a"), s -> null))
                .isInstanceOf(NullPointerException.class);
    }
}

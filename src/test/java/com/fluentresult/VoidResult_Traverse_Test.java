package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_Traverse_Test {

    @Test
    void traverse_emptyItems_shouldGiveSuccess() {
        VoidResult<String> result = VoidResult.traverse(List.<String>of(), s -> VoidResult.success());
        assertThat(result).isEqualTo(VoidResult.success());
    }

    @Test
    void traverse_allSuccess_shouldGiveSuccessAndApplyFunctionToAllItems() {
        List<String> applied = new ArrayList<>();
        VoidResult<String> result = VoidResult.traverse(List.of("a", "b"), s -> {
            applied.add(s);
            return VoidResult.success();
        });
        assertThat(result).isEqualTo(VoidResult.success());
        assertThat(applied).containsExactly("a", "b");
    }

    @Test
    void traverse_error_shouldGiveFirstErrorAndNotApplyFunctionToRemainingItems() {
        List<String> applied = new ArrayList<>();
        VoidResult<String> result = VoidResult.traverse(List.of("a", "error1", "error2", "b"), s -> {
            applied.add(s);
            return s.startsWith("error") ? VoidResult.error(s) : VoidResult.success();
        });
        assertThat(result).isEqualTo(VoidResult.error("error1"));
        assertThat(applied).containsExactly("a", "error1");
    }

    @Test
    void traverse_nullItemsGivesNPE() {
        assertThatThrownBy(() -> VoidResult.traverse(null, s -> VoidResult.success()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverse_nullFunctionGivesNPE() {
        assertThatThrownBy(() -> VoidResult.traverse(List.of(), null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void traverse_nullReturnedFromFunctionGivesNPE() {
        assertThatThrownBy(() -> VoidResult.traverse(List.of("a"), s -> null))
                .isInstanceOf(NullPointerException.class);
    }
}

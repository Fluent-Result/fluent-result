package com.fluentresult.assertj;

import com.fluentresult.VoidResult;
import org.junit.jupiter.api.Test;

import static com.fluentresult.assertj.FluentResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

class VoidResultAssert_Test {

    private final VoidResult<Integer> success = VoidResult.success();
    private final VoidResult<Integer> error = VoidResult.error(42);

    @Test
    void isSuccess_success_shouldPass() {
        assertThat(success).isSuccess().isSuccess();
    }

    @Test
    void isSuccess_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).isSuccess())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting VoidResult to be Success but was:")
                .hasMessageContaining("Error[error=42]");
    }

    @Test
    void isSuccess_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((VoidResult<Integer>) null).isSuccess())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void isError_error_shouldPass() {
        assertThat(error).isError().hasError(42);
    }

    @Test
    void isError_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).isError())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting VoidResult to be Error but was:")
                .hasMessageContaining("Success[]");
    }

    @Test
    void isError_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((VoidResult<Integer>) null).isError())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void hasError_equalError_shouldPass() {
        assertThat(error).hasError(42).isError();
    }

    @Test
    void hasError_otherError_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).hasError(43))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting VoidResult to have error:%n  43%nbut was:%n  42".formatted());
    }

    @Test
    void hasError_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).hasError(42))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting VoidResult to be Error but was:");
    }

    @Test
    void hasErrorSatisfying_satisfied_shouldPass() {
        assertThat(error)
                .hasErrorSatisfying(e -> assertThat(e).isPositive())
                .isError();
    }

    @Test
    void hasErrorSatisfying_notSatisfied_shouldFail() {
        assertThatThrownBy(() -> assertThat(error)
                .hasErrorSatisfying(e -> assertThat(e).isNegative()))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("to be less than");
    }

    @Test
    void hasErrorSatisfying_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).hasErrorSatisfying(e -> {}))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting VoidResult to be Error but was:");
    }

    @Test
    void hasErrorSatisfying_nullRequirements_shouldThrow() {
        assertThatThrownBy(() -> assertThat(success).hasErrorSatisfying(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void error_error_shouldReturnAssertOnError() {
        assertThat(error).error().isEqualTo(42);
    }

    @Test
    void error_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).error())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting VoidResult to be Error but was:");
    }

    @Test
    void errorWithFactory_error_shouldReturnTypedAssertOnError() {
        assertThat(error).error(INTEGER).isGreaterThan(41);
    }

    @Test
    void errorWithFactory_wrongType_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).error(STRING))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("to be an instance of");
    }

    @Test
    void errorWithFactory_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).error(INTEGER))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting VoidResult to be Error but was:");
    }
}

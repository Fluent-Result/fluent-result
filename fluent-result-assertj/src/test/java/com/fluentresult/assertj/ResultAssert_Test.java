package com.fluentresult.assertj;

import com.fluentresult.Result;
import org.junit.jupiter.api.Test;

import static com.fluentresult.assertj.FluentResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

class ResultAssert_Test {

    private final Result<String, Integer> success = Result.success("Success");
    private final Result<String, Integer> error = Result.error(42);

    @Test
    void isSuccess_success_shouldPass() {
        assertThat(success).isSuccess().hasValue("Success");
    }

    @Test
    void isSuccess_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).isSuccess())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to be Success but was:")
                .hasMessageContaining("Result[Error: 42]");
    }

    @Test
    void isSuccess_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((Result<String, Integer>) null).isSuccess())
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
                .hasMessageContaining("Expecting Result to be Error but was:")
                .hasMessageContaining("Result[Value: Success]");
    }

    @Test
    void isError_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((Result<String, Integer>) null).isError())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void hasValue_equalValue_shouldPass() {
        assertThat(success).hasValue("Success").isSuccess();
    }

    @Test
    void hasValue_otherValue_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).hasValue("Other"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to have value:%n  Other%nbut was:%n  Success".formatted());
    }

    @Test
    void hasValue_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).hasValue("Success"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to be Success but was:");
    }

    @Test
    void hasValueSatisfying_satisfied_shouldPass() {
        assertThat(success)
                .hasValueSatisfying(value -> assertThat(value).startsWith("Succ"))
                .isSuccess();
    }

    @Test
    void hasValueSatisfying_notSatisfied_shouldFail() {
        assertThatThrownBy(() -> assertThat(success)
                .hasValueSatisfying(value -> assertThat(value).startsWith("Other")))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("to start with");
    }

    @Test
    void hasValueSatisfying_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).hasValueSatisfying(value -> {}))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to be Success but was:");
    }

    @Test
    void hasValueSatisfying_nullRequirements_shouldThrow() {
        assertThatThrownBy(() -> assertThat(error).hasValueSatisfying(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void hasError_equalError_shouldPass() {
        assertThat(error).hasError(42).isError();
    }

    @Test
    void hasError_otherError_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).hasError(43))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to have error:%n  43%nbut was:%n  42".formatted());
    }

    @Test
    void hasError_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).hasError(42))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to be Error but was:");
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
                .hasMessageContaining("Expecting Result to be Error but was:");
    }

    @Test
    void hasErrorSatisfying_nullRequirements_shouldThrow() {
        assertThatThrownBy(() -> assertThat(success).hasErrorSatisfying(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void value_success_shouldReturnAssertOnValue() {
        assertThat(success).value().isEqualTo("Success");
    }

    @Test
    void value_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).value())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to be Success but was:");
    }

    @Test
    void valueWithFactory_success_shouldReturnTypedAssertOnValue() {
        assertThat(success).value(STRING).startsWith("Succ");
    }

    @Test
    void valueWithFactory_wrongType_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).value(INTEGER))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("to be an instance of");
    }

    @Test
    void valueWithFactory_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).value(STRING))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to be Success but was:");
    }

    @Test
    void error_error_shouldReturnAssertOnError() {
        assertThat(error).error().isEqualTo(42);
    }

    @Test
    void error_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).error())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting Result to be Error but was:");
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
                .hasMessageContaining("Expecting Result to be Error but was:");
    }
}

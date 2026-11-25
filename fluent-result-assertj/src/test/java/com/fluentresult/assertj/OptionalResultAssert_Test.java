package com.fluentresult.assertj;

import com.fluentresult.OptionalResult;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.fluentresult.assertj.FluentResultAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

class OptionalResultAssert_Test {

    private final OptionalResult<String, Integer> success = OptionalResult.success("Success");
    private final OptionalResult<String, Integer> empty = OptionalResult.empty();
    private final OptionalResult<String, Integer> error = OptionalResult.error(42);

    @Test
    void isPresent_value_shouldPass() {
        assertThat(success).isPresent().hasValue("Success");
    }

    @Test
    void isPresent_empty_shouldFail() {
        assertThatThrownBy(() -> assertThat(empty).isPresent())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Value but was:")
                .hasMessageContaining("Empty[]");
    }

    @Test
    void isPresent_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).isPresent())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
    }

    @Test
    void isPresent_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((OptionalResult<String, Integer>) null).isPresent())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void isEmpty_empty_shouldPass() {
        assertThat(empty).isEmpty().isSuccess();
    }

    @Test
    void isEmpty_value_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).isEmpty())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Empty but was:")
                .hasMessageContaining("Value[value=Success]");
    }

    @Test
    void isEmpty_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).isEmpty())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Empty but was:");
    }

    @Test
    void isEmpty_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((OptionalResult<String, Integer>) null).isEmpty())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void hasValue_empty_shouldFail() {
        assertThatThrownBy(() -> assertThat(empty).hasValue("Success"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
    }

    @Test
    void isError_empty_shouldFail() {
        assertThatThrownBy(() -> assertThat(empty).isError())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Error but was:");
    }

    @Test
    void isSuccess_success_shouldPass() {
        assertThat(success).isSuccess().hasValue("Success");
    }

    @Test
    void isSuccess_empty_shouldPass() {
        assertThat(empty).isSuccess().isEmpty();
    }

    @Test
    void isSuccess_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).isSuccess())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Value or Empty but was:")
                .hasMessageContaining("Error[error=42]");
    }

    @Test
    void isSuccess_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((OptionalResult<String, Integer>) null).isSuccess())
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
                .hasMessageContaining("Expecting OptionalResult to be Error but was:")
                .hasMessageContaining("Value[value=Success]");
    }

    @Test
    void isError_null_shouldFail() {
        assertThatThrownBy(() -> assertThat((OptionalResult<String, Integer>) null).isError())
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
                .hasMessageContaining("Expecting OptionalResult to have value:%n  Other%nbut was:%n  Success".formatted());
    }

    @Test
    void hasValue_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).hasValue("Success"))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
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
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
    }

    @Test
    void hasValueSatisfying_nullRequirements_shouldThrow() {
        assertThatThrownBy(() -> assertThat(error).hasValueSatisfying((Consumer<Object>) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void hasValueSatisfyingCondition_satisfied_shouldPass() {
        assertThat(success)
                .hasValueSatisfying(new Condition<>(value -> value.startsWith("Succ"), "starts with Succ"))
                .isSuccess();
    }

    @Test
    void hasValueSatisfyingCondition_notSatisfied_shouldFail() {
        assertThatThrownBy(() -> assertThat(success)
                .hasValueSatisfying(new Condition<>(value -> value.startsWith("Other"), "starts with Other")))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult value:")
                .hasMessageContaining("Success")
                .hasMessageContaining("to satisfy:")
                .hasMessageContaining("starts with Other");
    }

    @Test
    void hasValueSatisfyingCondition_empty_shouldFail() {
        assertThatThrownBy(() -> assertThat(empty).hasValueSatisfying(new Condition<>(value -> true, "anything")))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
    }

    @Test
    void hasValueSatisfyingCondition_error_shouldFail() {
        assertThatThrownBy(() -> assertThat(error).hasValueSatisfying(new Condition<>(value -> true, "anything")))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
    }

    @Test
    void hasValueSatisfyingCondition_nullCondition_shouldThrow() {
        assertThatThrownBy(() -> assertThat(error).hasValueSatisfying((Condition<Object>) null))
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
                .hasMessageContaining("Expecting OptionalResult to have error:%n  43%nbut was:%n  42".formatted());
    }

    @Test
    void hasError_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).hasError(42))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Error but was:");
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
                .hasMessageContaining("Expecting OptionalResult to be Error but was:");
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
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
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
                .hasMessageContaining("Expecting OptionalResult to be Value but was:");
    }

    @Test
    void error_error_shouldReturnAssertOnError() {
        assertThat(error).error().isEqualTo(42);
    }

    @Test
    void error_success_shouldFail() {
        assertThatThrownBy(() -> assertThat(success).error())
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Expecting OptionalResult to be Error but was:");
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
                .hasMessageContaining("Expecting OptionalResult to be Error but was:");
    }
}

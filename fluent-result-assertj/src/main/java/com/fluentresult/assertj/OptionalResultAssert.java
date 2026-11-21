package com.fluentresult.assertj;

import com.fluentresult.OptionalResult;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;

import java.util.Objects;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatObject;

/**
 * Assertions for {@link OptionalResult}.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value
 */
public final class OptionalResultAssert<T, E> extends AbstractAssert<OptionalResultAssert<T, E>, OptionalResult<T, E>> {

    OptionalResultAssert(OptionalResult<T, E> actual) {
        super(actual, OptionalResultAssert.class);
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state,
     * either with a value or empty.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.empty();
     * assertThat(result).isSuccess();
     * }
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code OptionalResult} is
     * {@code null} or in error state
     */
    public OptionalResultAssert<T, E> isSuccess() {
        isNotNull();
        if (actual instanceof OptionalResult.Error) {
            throw failure("%nExpecting OptionalResult to be Value or Empty but was:%n  %s", actual);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state with
     * a value.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.success(42);
     * assertThat(result).isPresent();
     * }
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code OptionalResult} is
     * {@code null}, empty or in error state
     */
    public OptionalResultAssert<T, E> isPresent() {
        successValue();
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state
     * without a value.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.empty();
     * assertThat(result).isEmpty();
     * }
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code OptionalResult} is
     * {@code null}, has a value or is in error state
     */
    public OptionalResultAssert<T, E> isEmpty() {
        isNotNull();
        if (!(actual instanceof OptionalResult.Empty)) {
            throw failure("%nExpecting OptionalResult to be Empty but was:%n  %s", actual);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in error state.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.error("failed");
     * assertThat(result).isError();
     * }
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null} or
     * in success state (with a value or empty)
     */
    public OptionalResultAssert<T, E> isError() {
        errorValue();
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state with a
     * value equal to the given one.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.success(42);
     * assertThat(result).hasValue(42);
     * }
     *
     * @param expected the expected success value
     * @return this assertion object
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null},
     * empty, in error state, or has a different success value
     */
    public OptionalResultAssert<T, E> hasValue(T expected) {
        T value = successValue();
        if (!Objects.equals(value, expected)) {
            throw failureWithActualExpected(value, expected,
                    "%nExpecting OptionalResult to have value:%n  %s%nbut was:%n  %s", expected, value);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state with a
     * value satisfying the given requirements.
     * <p>
     * Unlike {@link #value()}, which switches to an assertion object on the
     * success value, this returns this assertion object, so further
     * assertions can be chained on the {@code OptionalResult}. The
     * requirements also receive the value as {@code T}, so
     * {@code assertThat(value)} inside them gives the type-specific assertion
     * object without needing {@link #value(InstanceOfAssertFactory)}.
     *
     * {@snippet lang="java" :
     * OptionalResult<String, String> result = OptionalResult.success("hello");
     * assertThat(result).hasValueSatisfying(value -> assertThat(value).startsWith("he"));
     * }
     *
     * @param requirements the requirements, expressed as assertions on the
     * success value
     * @return this assertion object
     * @throws NullPointerException if the given requirements are {@code null}
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null},
     * empty, in error state, or the success value does not satisfy the requirements
     */
    public OptionalResultAssert<T, E> hasValueSatisfying(Consumer<? super T> requirements) {
        Objects.requireNonNull(requirements);
        requirements.accept(successValue());
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state with a
     * value satisfying the given condition.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.success(42);
     * Condition<Integer> positive = new Condition<>(value -> value > 0, "positive");
     * assertThat(result).hasValueSatisfying(positive);
     * }
     *
     * @param condition the condition the success value must satisfy
     * @return this assertion object
     * @throws NullPointerException if the given condition is {@code null}
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null},
     * empty, in error state, or the success value does not satisfy the condition
     */
    public OptionalResultAssert<T, E> hasValueSatisfying(Condition<? super T> condition) {
        Objects.requireNonNull(condition);
        T value = successValue();
        if (!condition.matches(value)) {
            throw failure("%nExpecting OptionalResult value:%n  %s%nto satisfy:%n  %s", value, condition);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in error state with an error
     * value equal to the given one.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.error("failed");
     * assertThat(result).hasError("failed");
     * }
     *
     * @param expected the expected error value
     * @return this assertion object
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null}, in
     * success state, or has a different error value
     */
    public OptionalResultAssert<T, E> hasError(E expected) {
        E error = errorValue();
        if (!Objects.equals(error, expected)) {
            throw failureWithActualExpected(error, expected,
                    "%nExpecting OptionalResult to have error:%n  %s%nbut was:%n  %s", expected, error);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in error state with an error
     * value satisfying the given requirements.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.error("failed");
     * assertThat(result).hasErrorSatisfying(error -> assertThat(error).contains("fail"));
     * }
     *
     * @param requirements the requirements, expressed as assertions on the
     * error value
     * @return this assertion object
     * @throws NullPointerException if the given requirements are {@code null}
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null}, in
     * success state, or the error value does not satisfy the requirements
     */
    public OptionalResultAssert<T, E> hasErrorSatisfying(Consumer<? super E> requirements) {
        Objects.requireNonNull(requirements);
        requirements.accept(errorValue());
        return myself;
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state, and returns
     * a new assertion object on the success value.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.success(42);
     * assertThat(result).value().isEqualTo(42);
     * }
     *
     * @return an assertion object on the success value
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null},
     * empty or in error state
     */
    public ObjectAssert<T> value() {
        return assertThatObject(successValue());
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in success state, and returns
     * a new assertion object on the success value narrowed by the given
     * factory, for example {@code value(InstanceOfAssertFactories.STRING)}.
     *
     * {@snippet lang="java" :
     * OptionalResult<String, String> result = OptionalResult.success("hello");
     * assertThat(result).value(InstanceOfAssertFactories.STRING).startsWith("he");
     * }
     *
     * @param assertFactory the factory used to create the assertion object
     * @param <ASSERT> the type of the returned assertion object
     * @return an assertion object on the success value
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null},
     * empty, in error state, or the success value is not of the factory's type
     */
    public <ASSERT extends AbstractAssert<?, ?>> ASSERT value(
            InstanceOfAssertFactory<?, ASSERT> assertFactory) {
        return assertThatObject(successValue()).asInstanceOf(assertFactory);
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in error state, and returns a
     * new assertion object on the error value.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.error("failed");
     * assertThat(result).error().isEqualTo("failed");
     * }
     *
     * @return an assertion object on the error value
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null} or
     * in success state (with a value or empty)
     */
    public ObjectAssert<E> error() {
        return assertThatObject(errorValue());
    }

    /**
     * Verifies that the actual {@code OptionalResult} is in error state, and returns a
     * new assertion object on the error value narrowed by the given factory,
     * for example {@code error(InstanceOfAssertFactories.THROWABLE)}.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, Exception> result = OptionalResult.error(new IllegalStateException("boom"));
     * assertThat(result).error(InstanceOfAssertFactories.THROWABLE).hasMessage("boom");
     * }
     *
     * @param assertFactory the factory used to create the assertion object
     * @param <ASSERT> the type of the returned assertion object
     * @return an assertion object on the error value
     * @throws AssertionError if the actual {@code OptionalResult} is {@code null}, in
     * success state, or the error value is not of the factory's type
     */
    public <ASSERT extends AbstractAssert<?, ?>> ASSERT error(
            InstanceOfAssertFactory<?, ASSERT> assertFactory) {
        return assertThatObject(errorValue()).asInstanceOf(assertFactory);
    }

    private T successValue() {
        isNotNull();
        if (!(actual instanceof OptionalResult.Value<T, E>(T value))) {
            throw failure("%nExpecting OptionalResult to be Value but was:%n  %s", actual);
        }
        return value;
    }

    private E errorValue() {
        isNotNull();
        if (!(actual instanceof OptionalResult.Error<T, E>(E error))) {
            throw failure("%nExpecting OptionalResult to be Error but was:%n  %s", actual);
        }
        return error;
    }
}

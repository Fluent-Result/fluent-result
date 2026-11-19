package com.fluentresult.assertj;

import com.fluentresult.Result;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;

import java.util.Objects;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatObject;

/**
 * Assertions for {@link Result}.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value
 */
public final class ResultAssert<T, E> extends AbstractAssert<ResultAssert<T, E>, Result<T, E>> {

    ResultAssert(Result<T, E> actual) {
        super(actual, ResultAssert.class);
    }

    /**
     * Verifies that the actual {@code Result} is in success state.
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code Result} is {@code null} or
     * in error state
     */
    public ResultAssert<T, E> isSuccess() {
        successValue();
        return myself;
    }

    /**
     * Verifies that the actual {@code Result} is in error state.
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code Result} is {@code null} or
     * in success state
     */
    public ResultAssert<T, E> isError() {
        errorValue();
        return myself;
    }

    /**
     * Verifies that the actual {@code Result} is in success state with a
     * value equal to the given one.
     *
     * @param expected the expected success value
     * @return this assertion object
     * @throws AssertionError if the actual {@code Result} is {@code null}, in
     * error state, or has a different success value
     */
    public ResultAssert<T, E> hasValue(T expected) {
        T value = successValue();
        if (!Objects.equals(value, expected)) {
            throw failureWithActualExpected(value, expected,
                    "%nExpecting Result to have value:%n  %s%nbut was:%n  %s", expected, value);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code Result} is in success state with a
     * value satisfying the given requirements.
     *
     * @param requirements the requirements, expressed as assertions on the
     * success value
     * @return this assertion object
     * @throws NullPointerException if the given requirements are {@code null}
     * @throws AssertionError if the actual {@code Result} is {@code null}, in
     * error state, or the success value does not satisfy the requirements
     */
    public ResultAssert<T, E> hasValueSatisfying(Consumer<? super T> requirements) {
        Objects.requireNonNull(requirements);
        requirements.accept(successValue());
        return myself;
    }

    /**
     * Verifies that the actual {@code Result} is in error state with an error
     * value equal to the given one.
     *
     * @param expected the expected error value
     * @return this assertion object
     * @throws AssertionError if the actual {@code Result} is {@code null}, in
     * success state, or has a different error value
     */
    public ResultAssert<T, E> hasError(E expected) {
        E error = errorValue();
        if (!Objects.equals(error, expected)) {
            throw failureWithActualExpected(error, expected,
                    "%nExpecting Result to have error:%n  %s%nbut was:%n  %s", expected, error);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code Result} is in error state with an error
     * value satisfying the given requirements.
     *
     * @param requirements the requirements, expressed as assertions on the
     * error value
     * @return this assertion object
     * @throws NullPointerException if the given requirements are {@code null}
     * @throws AssertionError if the actual {@code Result} is {@code null}, in
     * success state, or the error value does not satisfy the requirements
     */
    public ResultAssert<T, E> hasErrorSatisfying(Consumer<? super E> requirements) {
        Objects.requireNonNull(requirements);
        requirements.accept(errorValue());
        return myself;
    }

    /**
     * Verifies that the actual {@code Result} is in success state, and returns
     * a new assertion object on the success value.
     *
     * @return an assertion object on the success value
     * @throws AssertionError if the actual {@code Result} is {@code null} or
     * in error state
     */
    public ObjectAssert<T> value() {
        return assertThatObject(successValue());
    }

    /**
     * Verifies that the actual {@code Result} is in success state, and returns
     * a new assertion object on the success value narrowed by the given
     * factory, for example {@code value(InstanceOfAssertFactories.STRING)}.
     *
     * @param assertFactory the factory used to create the assertion object
     * @param <ASSERT> the type of the returned assertion object
     * @return an assertion object on the success value
     * @throws AssertionError if the actual {@code Result} is {@code null}, in
     * error state, or the success value is not of the factory's type
     */
    public <ASSERT extends AbstractAssert<?, ?>> ASSERT value(
            InstanceOfAssertFactory<?, ASSERT> assertFactory) {
        return assertThatObject(successValue()).asInstanceOf(assertFactory);
    }

    /**
     * Verifies that the actual {@code Result} is in error state, and returns a
     * new assertion object on the error value.
     *
     * @return an assertion object on the error value
     * @throws AssertionError if the actual {@code Result} is {@code null} or
     * in success state
     */
    public ObjectAssert<E> error() {
        return assertThatObject(errorValue());
    }

    /**
     * Verifies that the actual {@code Result} is in error state, and returns a
     * new assertion object on the error value narrowed by the given factory,
     * for example {@code error(InstanceOfAssertFactories.THROWABLE)}.
     *
     * @param assertFactory the factory used to create the assertion object
     * @param <ASSERT> the type of the returned assertion object
     * @return an assertion object on the error value
     * @throws AssertionError if the actual {@code Result} is {@code null}, in
     * success state, or the error value is not of the factory's type
     */
    public <ASSERT extends AbstractAssert<?, ?>> ASSERT error(
            InstanceOfAssertFactory<?, ASSERT> assertFactory) {
        return assertThatObject(errorValue()).asInstanceOf(assertFactory);
    }

    private T successValue() {
        isNotNull();
        if (!(actual instanceof Result.Success<T, E>(T value))) {
            throw failure("%nExpecting Result to be Success but was:%n  %s", actual);
        }
        return value;
    }

    private E errorValue() {
        isNotNull();
        if (!(actual instanceof Result.Error<T, E>(E error))) {
            throw failure("%nExpecting Result to be Error but was:%n  %s", actual);
        }
        return error;
    }
}

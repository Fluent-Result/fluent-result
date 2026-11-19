package com.fluentresult.assertj;

import com.fluentresult.VoidResult;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;

import java.util.Objects;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThatObject;

/**
 * Assertions for {@link VoidResult}.
 *
 * @param <E> the type of the error value
 */
public final class VoidResultAssert<E> extends AbstractAssert<VoidResultAssert<E>, VoidResult<E>> {

    VoidResultAssert(VoidResult<E> actual) {
        super(actual, VoidResultAssert.class);
    }

    /**
     * Verifies that the actual {@code VoidResult} is in success state.
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code VoidResult} is {@code null}
     * or in error state
     */
    public VoidResultAssert<E> isSuccess() {
        isNotNull();
        if (!(actual instanceof VoidResult.Success)) {
            throw failure("%nExpecting VoidResult to be Success but was:%n  %s", actual);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code VoidResult} is in error state.
     *
     * @return this assertion object
     * @throws AssertionError if the actual {@code VoidResult} is {@code null} or
     * in success state
     */
    public VoidResultAssert<E> isError() {
        errorValue();
        return myself;
    }

    /**
     * Verifies that the actual {@code VoidResult} is in error state with an error
     * value equal to the given one.
     *
     * @param expected the expected error value
     * @return this assertion object
     * @throws AssertionError if the actual {@code VoidResult} is {@code null}, in
     * success state, or has a different error value
     */
    public VoidResultAssert<E> hasError(E expected) {
        E error = errorValue();
        if (!Objects.equals(error, expected)) {
            throw failureWithActualExpected(error, expected,
                    "%nExpecting VoidResult to have error:%n  %s%nbut was:%n  %s", expected, error);
        }
        return myself;
    }

    /**
     * Verifies that the actual {@code VoidResult} is in error state with an error
     * value satisfying the given requirements.
     *
     * @param requirements the requirements, expressed as assertions on the
     * error value
     * @return this assertion object
     * @throws NullPointerException if the given requirements are {@code null}
     * @throws AssertionError if the actual {@code VoidResult} is {@code null}, in
     * success state, or the error value does not satisfy the requirements
     */
    public VoidResultAssert<E> hasErrorSatisfying(Consumer<? super E> requirements) {
        Objects.requireNonNull(requirements);
        requirements.accept(errorValue());
        return myself;
    }

    /**
     * Verifies that the actual {@code VoidResult} is in error state, and returns a
     * new assertion object on the error value.
     *
     * @return an assertion object on the error value
     * @throws AssertionError if the actual {@code VoidResult} is {@code null} or
     * in success state
     */
    public ObjectAssert<E> error() {
        return assertThatObject(errorValue());
    }

    /**
     * Verifies that the actual {@code VoidResult} is in error state, and returns a
     * new assertion object on the error value narrowed by the given factory,
     * for example {@code error(InstanceOfAssertFactories.THROWABLE)}.
     *
     * @param assertFactory the factory used to create the assertion object
     * @param <ASSERT> the type of the returned assertion object
     * @return an assertion object on the error value
     * @throws AssertionError if the actual {@code VoidResult} is {@code null}, in
     * success state, or the error value is not of the factory's type
     */
    public <ASSERT extends AbstractAssert<?, ?>> ASSERT error(
            InstanceOfAssertFactory<?, ASSERT> assertFactory) {
        return assertThatObject(errorValue()).asInstanceOf(assertFactory);
    }

    private E errorValue() {
        isNotNull();
        if (!(actual instanceof VoidResult.Error<E>(E error))) {
            throw failure("%nExpecting VoidResult to be Error but was:%n  %s", actual);
        }
        return error;
    }
}

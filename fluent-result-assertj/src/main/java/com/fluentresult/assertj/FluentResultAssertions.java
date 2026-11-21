package com.fluentresult.assertj;

import com.fluentresult.BooleanResult;
import com.fluentresult.OptionalResult;
import com.fluentresult.Result;
import com.fluentresult.VoidResult;

/**
 * Entry point for AssertJ assertions on the result types of Fluent Result.
 * <p>
 * The {@code assertThat} methods do not clash with the ones in
 * {@link org.assertj.core.api.Assertions}, so both can be statically imported
 * in the same test.
 */
public final class FluentResultAssertions {

    private FluentResultAssertions() {
    }

    /**
     * Creates a new assertion for the given {@code Result}.
     *
     * {@snippet lang="java" :
     * Result<Integer, String> result = Result.success(42);
     * assertThat(result).hasValue(42);
     * }
     *
     * @param actual the {@code Result} to make assertions on
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return the created assertion object
     */
    public static <T, E> ResultAssert<T, E> assertThat(Result<T, E> actual) {
        return new ResultAssert<>(actual);
    }

    /**
     * Creates a new assertion for the given {@code OptionalResult}.
     *
     * {@snippet lang="java" :
     * OptionalResult<Integer, String> result = OptionalResult.empty();
     * assertThat(result).isEmpty();
     * }
     *
     * @param actual the {@code OptionalResult} to make assertions on
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return the created assertion object
     */
    public static <T, E> OptionalResultAssert<T, E> assertThat(OptionalResult<T, E> actual) {
        return new OptionalResultAssert<>(actual);
    }

    /**
     * Creates a new assertion for the given {@code BooleanResult}.
     *
     * {@snippet lang="java" :
     * BooleanResult<String> result = BooleanResult.successTrue();
     * assertThat(result).isTrue();
     * }
     *
     * @param actual the {@code BooleanResult} to make assertions on
     * @param <E> the type of the error value
     * @return the created assertion object
     */
    public static <E> BooleanResultAssert<E> assertThat(BooleanResult<E> actual) {
        return new BooleanResultAssert<>(actual);
    }

    /**
     * Creates a new assertion for the given {@code VoidResult}.
     *
     * {@snippet lang="java" :
     * VoidResult<String> result = VoidResult.success();
     * assertThat(result).isSuccess();
     * }
     *
     * @param actual the {@code VoidResult} to make assertions on
     * @param <E> the type of the error value
     * @return the created assertion object
     */
    public static <E> VoidResultAssert<E> assertThat(VoidResult<E> actual) {
        return new VoidResultAssert<>(actual);
    }
}

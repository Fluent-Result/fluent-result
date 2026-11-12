package com.fluentresult;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A result object which either is in success state, where it may contain a
 * non-{@code null} success value or be empty, or it may be in error state
 * containing a non-{@code null} error value.
 * <p>
 * A variable whose type is {@code OptionalResult} should never itself be
 * {@code null}, it should always point to an {@code OptionalResult} instance.
 * <p>
 * All methods taking functional arguments (functions, suppliers, consumers,
 * predicates and runnables) check them for {@code null} eagerly. A
 * {@code null} argument results in a {@link NullPointerException} regardless
 * of the state of the result, even if the argument would not have been used.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value
 */
public sealed interface OptionalResult<T, E> {

    /**
     * Returns an {@code OptionalResult} in success state, containing the value
     * present in the given {@code Optional} as success value, or empty if the
     * given {@code Optional} is empty.
     *
     * @param maybeValue an {@code Optional} which may contain a success value,
     * or may be empty
     * @param <T> the type of the success value which may be present in the
     * given {@code Optional}
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in success state which either contains
     * a success value or is empty
     * @throws NullPointerException if given {@code Optional} is {@code null}
     */
    static <T, E> OptionalResult<T, E> success(Optional<? extends T> maybeValue) {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) Objects.requireNonNull(maybeValue);
        return t.map(OptionalResult::<T, E>success).orElse(empty());
    }

    /**
     * Returns an {@code OptionalResult} in success state containing the given
     * non-{@code null} value as success value.
     *
     * @param value the success value, which must be non-{@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in success state containing the given
     * success value
     * @throws NullPointerException if given success value is {@code null}
     */
    static <T, E> OptionalResult<T, E> success(T value) {
        return new Value<>(Objects.requireNonNull(value));
    }

    /**
     * Returns an {@code OptionalResult} in success state either containing the
     * given value as success value, or empty if the given value is
     * {@code null}.
     *
     * @param value the success value, which may be {@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in success state containing the given
     * success value if not {@code null}, otherwise an empty
     * {@code OptionalResult}
     */
    static <T, E> OptionalResult<T, E> successNullable(T value) {
        if (value == null) {
            return empty();
        }
        return new Value<>(value);
    }

    /**
     * Returns an {@code OptionalResult} in success state, which is empty with
     * no success value.
     *
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an empty {@code OptionalResult} in success state
     */
    static <T, E> OptionalResult<T, E> empty() {
        return new Empty<>();
    }

    /**
     * Returns an {@code OptionalResult} in error state containing the given
     * non-{@code null} value as error value.
     *
     * @param value the error value, which must be non-{@code null}
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return an {@code OptionalResult} in error state containing the given
     * error value
     * @throws NullPointerException if given error value is {@code null}
     */
    static <T, E> OptionalResult<T, E> error(E value) {
        return new Error<>(Objects.requireNonNull(value));
    }

    /**
     * If in success state, returns a {@code Result} containing the result of
     * applying the given mapping function to the optional success value of this
     * {@code OptionalResult}, otherwise returns a {@code Result} containing the
     * error value of this {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @param <N> the type of the value returned from the mapping function
     * @return a {@code Result} containing the result of applying the mapping
     * function to the optional success value of this {@code OptionalResult}, if
     * in success state, otherwise a {@code Result} containing the error value
     * of this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> Result<N, E> map(Function<? super Optional<T>, ? extends N> function);

    /**
     * If in success state, returns an {@code OptionalResult} containing the
     * result of applying the given mapping function to the optional success
     * value, otherwise returns the unaltered {@code OptionalResult} in error
     * state.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @param <N> the type of the success value which may be present in the
     * {@code Optional} returned from the mapping function
     * @return an {@code OptionalResult} containing the result of applying the
     * mapping function to the optional success value of this
     * {@code OptionalResult}, if in success state, otherwise the unaltered
     * {@code OptionalResult} in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> OptionalResult<N, E> mapToOptional(
            Function<? super Optional<T>, ? extends Optional<? extends N>> function);

    /**
     * If in success state, returns a {@code BooleanResult} containing the
     * result of applying the given mapping function to the optional success
     * value, otherwise returns a {@code BooleanResult} containing the error
     * value of this {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return a {@code BooleanResult} containing the result of applying the
     * mapping function to the optional success value of this
     * {@code OptionalResult}, if in success state, otherwise a
     * {@code BooleanResult} containing the error value of this
     * {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    BooleanResult<E> mapToBoolean(Function<? super Optional<T>, Boolean> function);

    /**
     * If in error state, returns an {@code OptionalResult} containing the
     * result of applying the given mapping function to the error value,
     * otherwise returns the unaltered {@code OptionalResult} in success state.
     *
     * @param function the mapping function to apply to the error value, if
     * error state
     * @param <N> the type of the value returned from the mapping function
     * @return an {@code OptionalResult} containing the result of applying the
     * mapping function to the error value of this {@code OptionalResult}, if in
     * error state, otherwise the unaltered {@code OptionalResult} in success
     * state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> OptionalResult<T, N> mapError(Function<? super E, ? extends N> function);

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} containing the result of applying the given
     * mapping function to the success value, otherwise returns the unaltered
     * {@code OptionalResult} which may be empty or in error state.
     * <p>
     * If the given mapping function returns {@code null}, then the returned
     * {@code OptionalResult} will be empty.
     *
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @param <N> the type of the value returned from the mapping function
     * @return an {@code OptionalResult} containing the result of applying the
     * mapping function to the success value of this {@code OptionalResult}, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null}
     */
    <N> OptionalResult<N, E> mapValue(Function<? super T, ? extends N> function);

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} containing the result of applying the given
     * mapping function to the success value, otherwise returns the unaltered
     * {@code OptionalResult} which may be empty or in error state.
     * <p>
     * If the given mapping function returns a non-empty {@code Optional}, then
     * the returned {@code OptionalResult} will contain the {@code Optional}
     * content as success value. If the given mapping function returns an empty
     * {@code Optional}, then the returned {@code OptionalResult} will also be
     * empty.
     *
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @param <N> the type of the success value which may be present in the
     * {@code Optional} returned from the mapping function
     * @return an {@code OptionalResult} containing the result of applying the
     * mapping function to the success value of this {@code OptionalResult}, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> OptionalResult<N, E> mapValueToOptional(
            Function<? super T, ? extends Optional<? extends N>> function);

    /**
     * If in success state, returns the {@code Result} from applying the given
     * mapping function to the optional success value, otherwise returns a
     * {@code Result} containing the error value of this {@code OptionalResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code Result} returned by the mapping function
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code Result} returned from the mapping function, if in
     * success state, otherwise a {@code Result} containing the error value of
     * this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> Result<N, E> flatMap(
            Function<? super Optional<T>, ? extends Result<? extends N, ? extends E>> function);

    /**
     * If in success state, returns the {@code OptionalResult} from applying
     * the given mapping function to the optional success value, otherwise
     * returns the unaltered {@code OptionalResult} in error state.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} returned by the mapping function
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code OptionalResult} returned from the mapping function, if
     * in success state, otherwise the unaltered {@code OptionalResult} in error
     * state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> OptionalResult<N, E> flatMapToOptionalResult(
            Function<? super Optional<T>, ? extends OptionalResult<? extends N, ? extends E>> function);

    /**
     * If in empty success state, returns the {@code OptionalResult} provided by
     * the given supplier, otherwise returns the unaltered
     * {@code OptionalResult} in success state with a value or in error state.
     *
     * @param supplier the supplier providing the replacement
     * {@code OptionalResult}, if in empty success state
     * @return the {@code OptionalResult} provided by the supplier, if in empty
     * success state, otherwise the unaltered {@code OptionalResult} in success
     * state with a value or in error state
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    OptionalResult<T, E> flatReplaceEmpty(
            Supplier<? extends OptionalResult<? extends T, ? extends E>> supplier);

    /**
     * If in empty success state, returns the {@code Result} provided by the
     * given supplier. If in success state with a value, returns a
     * {@code Result} in success state containing that value. If in error
     * state, returns a {@code Result} containing the error value of this
     * {@code OptionalResult}.
     *
     * @param supplier the supplier providing the {@code Result}, if in empty
     * success state
     * @return the {@code Result} provided by the supplier, if in empty success
     * state, otherwise a {@code Result} containing the existing success value
     * or error value
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    Result<T, E> flatReplaceEmptyWithResult(
            Supplier<? extends Result<? extends T, ? extends E>> supplier);
    /**
     * If in success state, returns the {@code BooleanResult} from applying
     * the given mapping function to the optional success value, otherwise
     * returns a {@code BooleanResult} containing the error value of this
     * {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code BooleanResult} returned from the mapping function, if
     * in success state, otherwise a {@code BooleanResult} containing the error
     * value of this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    BooleanResult<E> flatMapToBooleanResult(
            Function<? super Optional<T>, ? extends BooleanResult<? extends E>> function);

    /**
     * If in success state, returns the {@code VoidResult} from applying the
     * given mapping function to the optional success value, otherwise returns a
     * {@code VoidResult} containing the error value of this
     * {@code OptionalResult}.
     *
     * @param function the mapping function to apply to the optional success
     * value, if success state
     * @return the {@code VoidResult} returned from the mapping function, if in
     * success state, otherwise a {@code VoidResult} containing the error value
     * of this {@code OptionalResult}
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    VoidResult<E> flatMapToVoidResult(
            Function<? super Optional<T>, ? extends VoidResult<? extends E>> function);

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} from applying the given mapping function to the
     * success value, otherwise returns the unaltered {@code OptionalResult}
     * which may be empty or in error state.
     * <p>
     * If the {@code Result} returned from the mapping function is in success
     * state, the returned {@code OptionalResult} will contain the success value
     * from the {@code Result}. If the {@code Result} is in error state, the
     * returned {@code OptionalResult} will contain the error value from the
     * {@code Result}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} after applying the mapping function
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @return an {@code OptionalResult} after applying the mapping function, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> OptionalResult<N, E> flatMapValueWithResult(
            Function<? super T, ? extends Result<? extends N, ? extends E>> function);

    /**
     * If in success state with a success value, returns the
     * {@code OptionalResult} from applying the given mapping function to the
     * success value, otherwise returns the unaltered {@code OptionalResult}
     * which may be empty or in error state.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} returned by the mapping function
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @return the {@code OptionalResult} returned from the mapping function, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> OptionalResult<N, E> flatMapValueWithOptionalResult(
            Function<? super T, ? extends OptionalResult<? extends N, ? extends E>> function);

    /**
     * If in success state with a success value, returns an
     * {@code OptionalResult} from applying the given mapping function to the
     * success value, otherwise returns the unaltered {@code OptionalResult}
     * which may be empty or in error state.
     * <p>
     * If the {@code BooleanResult} returned from the mapping function is in
     * success state, the returned {@code OptionalResult} will contain the
     * boolean success value from the {@code BooleanResult}. If the
     * {@code BooleanResult} is in error state, the returned
     * {@code OptionalResult} will contain the error value from the
     * {@code BooleanResult}.
     *
     * @param function the mapping function to apply to the success value, if
     * success state with a success value
     * @return an {@code OptionalResult} after applying the mapping function, if
     * in success state with a success value, otherwise the unaltered
     * {@code OptionalResult} which may be empty or in error state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    OptionalResult<Boolean, E> flatMapValueWithBooleanResult(
            Function<? super T, ? extends BooleanResult<? extends E>> function);

    /**
     * If in error state, returns an {@code OptionalResult} in success state
     * containing the optional success value from applying the given mapping
     * function to the error value, otherwise returns the unaltered
     * {@code OptionalResult} in success state.
     *
     * @param function the mapping function to apply to the error value to
     * convert to a new optional success value, if error state
     * @return an {@code OptionalResult} containing the optional value from the
     * mapping function, if in error state, otherwise the unaltered
     * {@code OptionalResult} in success state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    OptionalResult<T, E> recover(Function<? super E, ? extends Optional<? extends T>> function);

    /**
     * If in error state, returns the {@code OptionalResult} from applying the
     * given mapping function to the error value, otherwise returns the
     * unaltered {@code OptionalResult} in success state.
     *
     * @param function the mapping function to apply to the error value to
     * convert to a new {@code OptionalResult}, if error state
     * @param <F> the type of error value which may be present in the
     * {@code OptionalResult} returned by the mapping function
     * @return the {@code OptionalResult} returned from the mapping function, if
     * in error state, otherwise the unaltered {@code OptionalResult} in success
     * state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <F> OptionalResult<T, F> flatRecover(
            Function<? super E, ? extends OptionalResult<? extends T, ? extends F>> function);

    /**
     * If in success state, applies the optional success value to the given
     * consumer, otherwise does nothing.
     *
     * @param consumer the consumer which accepts the optional success value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    OptionalResult<T, E> consume(Consumer<? super Optional<T>> consumer);

    /**
     * If in success state with a success value, applies the success value to
     * the given consumer, otherwise does nothing.
     *
     * @param consumer the consumer which accepts the success value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    OptionalResult<T, E> consumeValue(Consumer<? super T> consumer);

    /**
     * If in error state, applies the error value to the given consumer,
     * otherwise does nothing.
     *
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    OptionalResult<T, E> consumeError(Consumer<? super E> errorConsumer);

    /**
     * If in success state, applies the optional success value to the given
     * success consumer. If in error state, applies the error value to the given
     * error consumer.
     *
     * @param successConsumer the consumer which accepts the optional success
     * value
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given consumers is {@code null}
     */
    OptionalResult<T, E> consumeEither(
            Consumer<? super Optional<T>> successConsumer,
            Consumer<? super E> errorConsumer);

    /**
     * If in success state with a success value, applies the success value to
     * the given value consumer. If empty, runs the given empty runnable. If in
     * error state, applies the error value to the given error consumer.
     *
     * @param valueConsumer the consumer which accepts the success value
     * @param emptyRunnable the runnable to run if empty
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given consumers or the
     * runnable is {@code null}
     */
    OptionalResult<T, E> consumeEither(
            Consumer<? super T> valueConsumer,
            Runnable emptyRunnable,
            Consumer<? super E> errorConsumer);

    /**
     * If in success state, applies the optional success value to the given
     * function. If the function returns a {@code VoidResult} in success state,
     * the original {@code OptionalResult} is returned unaltered. If the function
     * returns a {@code VoidResult} in error state, an {@code OptionalResult}
     * containing the error value is returned. If in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param function the function which accepts the optional success value
     * @return the original {@code OptionalResult} unaltered if the given
     * function returns success or the original {@code OptionalResult} is in
     * error state, otherwise an {@code OptionalResult} containing the error
     * value from the function result
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    OptionalResult<T, E> flatConsume(
            Function<? super Optional<T>, ? extends VoidResult<? extends E>> function);

    /**
     * If in success state with a success value, applies the success value to
     * the given function. If the function returns a {@code VoidResult} in
     * success state, the original {@code OptionalResult} is returned
     * unaltered. If the function returns a {@code VoidResult} in error state,
     * an {@code OptionalResult} containing the error value is returned. If
     * empty or in error state, the original {@code OptionalResult} is returned
     * unaltered.
     *
     * @param function the function which accepts the success value
     * @return the original {@code OptionalResult} unaltered if the given
     * function returns success or the original {@code OptionalResult} is
     * empty or in error state, otherwise an {@code OptionalResult} containing
     * the error value from the function result
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    OptionalResult<T, E> flatConsumeValue(
            Function<? super T, ? extends VoidResult<? extends E>> function);

    /**
     * If in success state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if success state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    OptionalResult<T, E> runIfSuccess(Runnable runnable);

    /**
     * If in success state with a success value, runs the given runnable,
     * otherwise does nothing.
     *
     * @param runnable the runnable to run if success state with a success value
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    OptionalResult<T, E> runIfValue(Runnable runnable);

    /**
     * If in empty success state or error state, runs the given runnable,
     * otherwise does nothing.
     *
     * @param runnable the runnable to run if empty success state or error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    OptionalResult<T, E> runIfNoValue(Runnable runnable);

    /**
     * If in empty success state with no success value, runs the given runnable,
     * otherwise does nothing.
     *
     * @param runnable the runnable to run if empty success state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    OptionalResult<T, E> runIfEmpty(Runnable runnable);

    /**
     * If in error state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    OptionalResult<T, E> runIfError(Runnable runnable);

    /**
     * If in success state, runs the given success runnable. If in error state,
     * runs the given error runnable.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    OptionalResult<T, E> runEither(Runnable successRunnable,
                                   Runnable errorRunnable);

    /**
     * If in success state with a success value, runs the given value runnable.
     * If empty, runs the given empty runnable. If in error state, runs the
     * given error runnable.
     *
     * @param valueRunnable the runnable to run if success state with a success
     * value
     * @param emptyRunnable the runnable to run if empty
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    OptionalResult<T, E> runEither(Runnable valueRunnable,
                                   Runnable emptyRunnable,
                                   Runnable errorRunnable);

    /**
     * Runs the given runnable, no matter the state.
     *
     * @param runnable the runnable to run
     * @return the original {@code OptionalResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    OptionalResult<T, E> runAlways(Runnable runnable);

    /**
     * If in success state, runs the given supplier. If the supplier returns a
     * {@code VoidResult} in success state, the original {@code OptionalResult}
     * is returned unaltered. If the supplier returns a {@code VoidResult} in
     * error state, an {@code OptionalResult} containing the error value is
     * returned. If in error state, the original {@code OptionalResult} is
     * returned unaltered.
     *
     * @param supplier the supplier to run
     * @return the original {@code OptionalResult} unaltered if the given
     * supplier returns success or the original {@code OptionalResult} is in
     * error state, otherwise an {@code OptionalResult} containing the error
     * value from the supplier result
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    OptionalResult<T, E> flatRunIfSuccess(
            Supplier<? extends VoidResult<? extends E>> supplier);

    /**
     * If in success state with a success value, runs the given supplier. If the
     * supplier returns a {@code VoidResult} in success state, the original
     * {@code OptionalResult} is returned unaltered. If the supplier returns a
     * {@code VoidResult} in error state, an {@code OptionalResult} containing
     * the error value is returned. If empty or in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param supplier the supplier to run
     * @return the original {@code OptionalResult} unaltered if the given
     * supplier returns success or the original {@code OptionalResult} is
     * empty or in error state, otherwise an {@code OptionalResult} containing
     * the error value from the supplier result
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    OptionalResult<T, E> flatRunIfValue(
            Supplier<? extends VoidResult<? extends E>> supplier);

    /**
     * If in success state, verifies the optional success value of this
     * {@code OptionalResult} by testing it with the given predicate. If the
     * predicate evaluates to false, a new {@code OptionalResult} is returned
     * containing the error value provided by the given error supplier. If the
     * predicate evaluates to true, or the {@code OptionalResult} already was
     * in error state, the original {@code OptionalResult} is returned
     * unaltered.
     *
     * @param predicate the predicate used to verify the optional success value,
     * if success state
     * @param errorSupplier supplier providing the error if predicate evaluates
     * to false
     * @return the original {@code OptionalResult} unaltered, unless the
     * predicate evaluates to false, then a new {@code OptionalResult} in error
     * state is returned containing the supplied error value
     * @throws NullPointerException if the given predicate is {@code null}, or
     * the given error supplier is {@code null} or returns {@code null}
     */
    OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                Supplier<? extends E> errorSupplier);

    /**
     * If in success state, verifies the optional success value of this
     * {@code OptionalResult} by testing it with the given predicate. If the
     * predicate evaluates to false, a new {@code OptionalResult} is returned
     * containing the error provided by mapping the optional value with the
     * given error function. If the predicate evaluates to true, or the
     * {@code OptionalResult} already was in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param predicate the predicate used to verify the optional success value,
     * if success state
     * @param errorFunction the function mapping the optional value to an error,
     * if the predicate evaluates to false
     * @return the original {@code OptionalResult} unaltered, unless the
     * predicate evaluates to false, then a new {@code OptionalResult} in error
     * state is returned containing the result of mapping the optional value to
     * an error
     * @throws NullPointerException if the given predicate is {@code null}, or
     * the given error function is {@code null} or returns {@code null}
     */
    OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                Function<? super Optional<T>, ? extends E> errorFunction);

    /**
     * If in success state, verifies the optional success value of this
     * {@code OptionalResult} by mapping it to a {@code VoidResult}. If the
     * returned {@code VoidResult} is in error state, a new
     * {@code OptionalResult} is returned containing the error value of the
     * {@code VoidResult}. If the {@code VoidResult} is in success state, or the
     * {@code OptionalResult} already was in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param function the function applied to the optional success value, if
     * success state
     * @return the original {@code OptionalResult} unaltered, unless the
     * {@code VoidResult} returned by the mapping function is in error state,
     * then a new {@code OptionalResult} in error state is returned containing
     * the error value from the {@code VoidResult}
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    OptionalResult<T, E> verify(
            Function<? super Optional<T>, ? extends VoidResult<? extends E>> function);

    /**
     * If in success state with a success value, verifies the success value of
     * this {@code OptionalResult} by testing it with the given predicate. If
     * the predicate evaluates to false, a new {@code OptionalResult} is returned
     * containing the error value provided by the given error supplier. If the
     * predicate evaluates to true, or the {@code OptionalResult} already was
     * empty or in error state, the original {@code OptionalResult} is returned
     * unaltered.
     *
     * @param predicate the predicate used to verify the success value, if
     * success state with a success value
     * @param errorSupplier supplier providing the error if predicate evaluates
     * to false
     * @return the original {@code OptionalResult} unaltered, unless the
     * predicate evaluates to false, then a new {@code OptionalResult} in error
     * state is returned containing the supplied error value
     * @throws NullPointerException if the given predicate is {@code null}, or
     * the given error supplier is {@code null} or returns {@code null}
     */
    OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                     Supplier<? extends E> errorSupplier);

    /**
     * If in success state with a success value, verifies the success value of
     * this {@code OptionalResult} by testing it with the given predicate. If
     * the predicate evaluates to false, a new {@code OptionalResult} is returned
     * containing the error value provided by mapping the value with the given
     * error function. If the predicate evaluates to true, or the
     * {@code OptionalResult} already was empty or in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param predicate the predicate used to verify the success value, if
     * success state with a success value
     * @param errorFunction the function mapping the value to an error, if the
     * predicate evaluates to false
     * @return the original {@code OptionalResult} unaltered, unless the
     * predicate evaluates to false, then a new {@code OptionalResult} in error
     * state is returned containing the result of mapping the value to an error
     * @throws NullPointerException if the given predicate is {@code null}, or
     * the given error function is {@code null} or returns {@code null}
     */
    OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                     Function<? super T, ? extends E> errorFunction);

    /**
     * If in success state with a success value, verifies the success value of
     * this {@code OptionalResult} by mapping it to a {@code VoidResult}. If the
     * returned {@code VoidResult} is in error state, a new
     * {@code OptionalResult} is returned containing the error value of the
     * {@code VoidResult}. If the {@code VoidResult} is in success state, or the
     * {@code OptionalResult} already was empty or in error state, the original
     * {@code OptionalResult} is returned unaltered.
     *
     * @param function the function applied to the success value, if success
     * state with a success value
     * @return the original {@code OptionalResult} unaltered, unless the
     * {@code VoidResult} returned by the mapping function is in error state,
     * then a new {@code OptionalResult} in error state is returned containing
     * the error value from the {@code VoidResult}
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    OptionalResult<T, E> verifyValue(
            Function<? super T, ? extends VoidResult<? extends E>> function);

    /**
     * Retrieve a value from this {@code OptionalResult} by folding the states.
     * If in success state, return the value of applying the success function to
     * the optional success value. If in error state, return the value of
     * applying the error function to the error value.
     *
     * @param <N> the type of the retrieved value
     * @param successFunction the mapping function to apply to the optional
     * success value, if success state, may return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value mapped from either the optional success value or
     * the error value, may be {@code null}
     * @throws NullPointerException if one of the given functions is
     * {@code null}
     */
    <N> N fold(Function<? super Optional<T>, ? extends N> successFunction,
               Function<? super E, ? extends N> errorFunction);

    /**
     * Retrieve a value from this {@code OptionalResult} by folding the states.
     * If in success state with a success value, return the value of applying
     * the value function to the success value. If empty, return the value
     * provided by the empty-supplier. If in error state, return the value of
     * applying the error function to the error value.
     *
     * @param <N> the type of the retrieved value
     * @param valueFunction the mapping function to apply to the success value,
     * if success state with a success value, may return {@code null}
     * @param emptySupplier the supplier to provide the value if empty, may
     * return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value mapped from the success value, provided by the
     * empty-supplier, or mapped from the error value, may be {@code null}
     * @throws NullPointerException if one of the given functions or the
     * supplier is {@code null}
     */
    <N> N fold(Function<? super T, ? extends N> valueFunction,
               Supplier<? extends N> emptySupplier,
               Function<? super E, ? extends N> errorFunction);

    /**
     * If in success state, returns the optional success value, otherwise
     * returns {@code other}.
     *
     * @param other the value to be returned, if not in success state, must not
     * be {@code null}, may contain a subtype of the success value type
     * @return the optional success value, if success state, otherwise
     * {@code other}
     * @throws NullPointerException if the other value is {@code null}
     */
    Optional<T> orElse(Optional<? extends T> other);

    /**
     * If in success state with a success value, returns the success value,
     * otherwise returns {@code other}.
     *
     * @param other the value to be returned, if not in success state with a
     * success value, may be {@code null}
     * @return the success value, if success state with a success value,
     * otherwise {@code other}
     */
    T valueOrElse(T other);

    /**
     * If in success state, returns the optional success value, otherwise
     * returns the value returned from the given function.
     *
     * @param function the mapping function to apply to the error value, if not
     * in success state, must not return {@code null}, may return an
     * {@code Optional} containing a subtype of the success value type
     * @return the optional success value, if success state, otherwise the
     * result returned from the given function
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    Optional<T> orElseGet(Function<? super E, ? extends Optional<? extends T>> function);

    /**
     * If in success state with a success value, returns the success value,
     * otherwise returns the value provided by the given supplier.
     *
     * @param supplier the supplier providing the return value, if not in
     * success state with a success value, may return {@code null}
     * @return the success value, if success state with a success value,
     * otherwise the value provided by the given supplier
     * @throws NullPointerException if the given supplier is {@code null}
     */
    T valueOrElseGet(Supplier<? extends T> supplier);

    /**
     * If in success state, returns the optional success value, otherwise throws
     * the exception returned by the given function.
     *
     * @param <X> type of the exception to be thrown
     * @param function the mapping function producing an exception by applying
     * the error value, if not in success state
     * @return the optional success value, if success state
     * @throws X if in error state
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    <X extends Throwable> Optional<T> orElseThrow(
            Function<? super E, ? extends X> function) throws X;

    /**
     * If in success state with a success value, returns the success value,
     * otherwise throws the exception provided by the given supplier.
     *
     * @param <X> type of the exception to be thrown
     * @param supplier the supplier providing the exception to be thrown, if
     * empty or in error state
     * @return the success value, if success state with a success value
     * @throws X if empty or in error state
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    <X extends Throwable> T valueOrElseThrow(
            Supplier<? extends X> supplier) throws X;

    /**
     * Transforms this {@code OptionalResult} to a {@code Result}. If in
     * non-empty success state, the {@code Result} will be in success state
     * containing the success value of this {@code OptionalResult}. If in empty
     * success state, the {@code Result} will be in error state, containing the
     * error value supplied by the given error supplier. If in error state, the
     * {@code Result} will be in error state containing the error value of this
     * {@code OptionalResult}.
     *
     * @param errorSupplier supplier providing the error value if empty success
     * state
     * @return a {@code Result} either in success state containing the success
     * value from this {@code OptionalResult}, or in error state containing
     * either the error value from the given error supplier or the error value
     * present in this {@code OptionalResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    Result<T, E> toResult(Supplier<? extends E> errorSupplier);

    /**
     * Transforms this {@code OptionalResult} to a {@code VoidResult}. If in
     * success state, empty or not, the {@code VoidResult} will be in success
     * state. If in error state, the {@code VoidResult} will be in error state
     * containing the error value from this {@code OptionalResult}.
     *
     * @return a {@code VoidResult} either in success state or in error state
     * containing the error value from this {@code OptionalResult}
     */
    VoidResult<E> toVoidResult();

    /**
     * Handle the given {@code Callable}. If the {@code Callable} executes
     * successfully, the {@code OptionalResult} will be in success state
     * containing the value present in the returned {@code Optional}, or empty
     * if the returned {@code Optional} is empty. If the {@code Callable} throws
     * an exception, the {@code OptionalResult} will be in error state
     * containing the thrown exception.
     *
     * @param callable the {@code Callable} to handle
     * @param <T> type of the value which may be present in the {@code Optional}
     * returned by the {@code Callable}
     * @return an {@code OptionalResult} either in success state containing the
     * optional value from the {@code Callable}, or in error state containing
     * the exception thrown by the {@code Callable}
     * @throws NullPointerException if the given callable is {@code null} or
     * returns {@code null}
     */
    static <T> OptionalResult<T, Exception> handle(
            Callable<? extends Optional<? extends T>> callable) {
        Objects.requireNonNull(callable);
        final Optional<? extends T> value;
        try {
            value = callable.call();
        } catch (Exception e) {
            return OptionalResult.error(e);
        }
        return OptionalResult.success(Objects.requireNonNull(value));
    }

    /**
     * Handle the given {@code Callable}. If the {@code Callable} executes
     * successfully, the {@code OptionalResult} will be in success state
     * containing the value present in the returned {@code Optional}, or empty
     * if the returned {@code Optional} is empty. If the {@code Callable} throws
     * an exception, the {@code OptionalResult} will be in error state
     * containing the result after mapping the exception with the given
     * exception mapper function.
     *
     * @param callable the {@code Callable} to handle
     * @param exceptionMapper the function mapping a thrown exception to an
     * error value
     * @param <T> type of the value which may be present in the {@code Optional}
     * returned by the {@code Callable}
     * @param <E> type of the error value after mapping a thrown exception
     * @return an {@code OptionalResult} either in success state containing the
     * optional value from the {@code Callable}, or in error state containing
     * the result after mapping the exception thrown by the {@code Callable}
     * @throws NullPointerException if the given callable is {@code null} or
     * returns {@code null}, or if the given exception mapper function is
     * {@code null} or returns {@code null}
     */
    static <T, E> OptionalResult<T, E> handle(Callable<? extends Optional<? extends T>> callable,
                                              Function<? super Exception, ? extends E> exceptionMapper) {
        Objects.requireNonNull(callable);
        Objects.requireNonNull(exceptionMapper);
        return handle(callable).mapError(exceptionMapper);
    }

    record Value<T, E>(T value) implements OptionalResult<T, E> {

        public Value(T value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public <N> Result<N, E> map(Function<? super Optional<T>, ? extends N> function) {
            Objects.requireNonNull(function);
            return Result.success(function.apply(Optional.of(value)));
        }

        @Override
        public <N> OptionalResult<N, E> mapToOptional(
                Function<? super Optional<T>, ? extends Optional<? extends N>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.success(function.apply(Optional.of(value)));
        }

        @Override
        public BooleanResult<E> mapToBoolean(Function<? super Optional<T>, Boolean> function) {
            Objects.requireNonNull(function);
            return BooleanResult.success(
                    Objects.requireNonNull(function.apply(Optional.of(value))));
        }

        @Override
        public <N> OptionalResult<T, N> mapError(Function<? super E, ? extends N> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public <N> OptionalResult<N, E> mapValue(Function<? super T, ? extends N> function) {
            Objects.requireNonNull(function);
            return OptionalResult.successNullable(function.apply(value));
        }

        @Override
        public <N> OptionalResult<N, E> mapValueToOptional(
                Function<? super T, ? extends Optional<? extends N>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.success(function.apply(value));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> Result<N, E> flatMap(
                Function<? super Optional<T>, ? extends Result<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return (Result<N, E>) Objects.requireNonNull(function.apply(Optional.of(value)));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> OptionalResult<N, E> flatMapToOptionalResult(
                Function<? super Optional<T>, ? extends OptionalResult<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<N, E>) Objects.requireNonNull(
                    function.apply(Optional.of(value)));
        }

        @Override
        public OptionalResult<T, E> flatReplaceEmpty(
                Supplier<? extends OptionalResult<? extends T, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return this;
        }

        @Override
        public Result<T, E> flatReplaceEmptyWithResult(
                Supplier<? extends Result<? extends T, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return Result.success(value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public BooleanResult<E> flatMapToBooleanResult(
                Function<? super Optional<T>, ? extends BooleanResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return (BooleanResult<E>) Objects.requireNonNull(function.apply(Optional.of(value)));
        }

        @SuppressWarnings("unchecked")
        @Override
        public VoidResult<E> flatMapToVoidResult(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return (VoidResult<E>) Objects.requireNonNull(function.apply(Optional.of(value)));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> OptionalResult<N, E> flatMapValueWithResult(
                Function<? super T, ? extends Result<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<N, E>) Objects.requireNonNull(function.apply(value))
                    .toOptionalResult();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> OptionalResult<N, E> flatMapValueWithOptionalResult(
                Function<? super T, ? extends OptionalResult<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<N, E>) Objects.requireNonNull(function.apply(value));
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionalResult<Boolean, E> flatMapValueWithBooleanResult(
                Function<? super T, ? extends BooleanResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<Boolean, E>) Objects.requireNonNull(function.apply(value))
                    .toOptionalResult();
        }

        @Override
        public OptionalResult<T, E> recover(
                Function<? super E, ? extends Optional<? extends T>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public <F> OptionalResult<T, F> flatRecover(
                Function<? super E, ? extends OptionalResult<? extends T, ? extends F>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consume(Consumer<? super Optional<T>> consumer) {
            Objects.requireNonNull(consumer);
            consumer.accept(Optional.of(value));
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeValue(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            consumer.accept(value);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeError(Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(errorConsumer);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeEither(Consumer<? super Optional<T>> successConsumer,
                                                    Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(successConsumer);
            Objects.requireNonNull(errorConsumer);
            successConsumer.accept(Optional.of(value));
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeEither(Consumer<? super T> valueConsumer,
                                                    Runnable emptyRunnable,
                                                    Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(valueConsumer);
            Objects.requireNonNull(emptyRunnable);
            Objects.requireNonNull(errorConsumer);
            valueConsumer.accept(value);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatConsume(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            VoidResult<? extends E> result =
                    Objects.requireNonNull(function.apply(Optional.of(value)));
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> flatConsumeValue(
                Function<? super T, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            VoidResult<? extends E> result = Objects.requireNonNull(function.apply(value));
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> runIfSuccess(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfValue(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfNoValue(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfEmpty(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfError(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runEither(Runnable successRunnable, Runnable errorRunnable) {
            Objects.requireNonNull(successRunnable);
            Objects.requireNonNull(errorRunnable);
            successRunnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runEither(Runnable valueRunnable,
                                                Runnable emptyRunnable,
                                                Runnable errorRunnable) {
            Objects.requireNonNull(valueRunnable);
            Objects.requireNonNull(emptyRunnable);
            Objects.requireNonNull(errorRunnable);
            valueRunnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runAlways(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatRunIfSuccess(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            VoidResult<? extends E> result = Objects.requireNonNull(supplier.get());
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> flatRunIfValue(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            VoidResult<? extends E> result = Objects.requireNonNull(supplier.get());
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                             Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorSupplier);
            if (predicate.test(Optional.of(value))) {
                return safeCast();
            } else {
                return OptionalResult.error(errorSupplier.get());
            }
        }

        @Override
        public OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                             Function<? super Optional<T>, ? extends E> errorFunction) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorFunction);
            Optional<T> optional = Optional.of(value);
            if (predicate.test(optional)) {
                return safeCast();
            } else {
                return OptionalResult.error(errorFunction.apply(optional));
            }
        }

        @Override
        public OptionalResult<T, E> verify(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            VoidResult<? extends E> result =
                    Objects.requireNonNull(function.apply(Optional.of(value)));
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                                  Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorSupplier);
            if (predicate.test(value)) {
                return safeCast();
            } else {
                return OptionalResult.error(errorSupplier.get());
            }
        }

        @Override
        public OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                                  Function<? super T, ? extends E> errorFunction) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorFunction);
            if (predicate.test(value)) {
                return safeCast();
            } else {
                return OptionalResult.error(errorFunction.apply(value));
            }
        }

        @Override
        public OptionalResult<T, E> verifyValue(
                Function<? super T, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            VoidResult<? extends E> result = Objects.requireNonNull(function.apply(value));
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public <N> N fold(Function<? super Optional<T>, ? extends N> successFunction,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(successFunction);
            Objects.requireNonNull(errorFunction);
            return successFunction.apply(Optional.of(value));
        }

        @Override
        public <N> N fold(Function<? super T, ? extends N> valueFunction,
                          Supplier<? extends N> emptySupplier,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(valueFunction);
            Objects.requireNonNull(emptySupplier);
            Objects.requireNonNull(errorFunction);
            return valueFunction.apply(value);
        }

        @Override
        public Optional<T> orElse(Optional<? extends T> other) {
            Objects.requireNonNull(other);
            return Optional.of(value);
        }

        @Override
        public T valueOrElse(T other) {
            return value;
        }

        @Override
        public Optional<T> orElseGet(Function<? super E, ? extends Optional<? extends T>> function) {
            Objects.requireNonNull(function);
            return Optional.of(value);
        }

        @Override
        public T valueOrElseGet(Supplier<? extends T> supplier) {
            Objects.requireNonNull(supplier);
            return value;
        }

        @Override
        public <X extends Throwable> Optional<T> orElseThrow(
                Function<? super E, ? extends X> function) throws X {
            Objects.requireNonNull(function);
            return Optional.of(value);
        }

        @Override
        public <X extends Throwable> T valueOrElseThrow(
                Supplier<? extends X> supplier) throws X {
            Objects.requireNonNull(supplier);
            return value;
        }

        @Override
        public Result<T, E> toResult(Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(errorSupplier);
            return Result.success(value);
        }

        @Override
        public VoidResult<E> toVoidResult() {
            return VoidResult.success();
        }

        @SuppressWarnings("unchecked")
        private <E1> OptionalResult.Value<T, E1> safeCast() {
            return (OptionalResult.Value<T, E1>) this;
        }
    }

    record Empty<T, E>() implements OptionalResult<T, E> {

        @Override
        public <N> Result<N, E> map(Function<? super Optional<T>, ? extends N> function) {
            Objects.requireNonNull(function);
            return Result.success(function.apply(Optional.empty()));
        }

        @Override
        public <N> OptionalResult<N, E> mapToOptional(
                Function<? super Optional<T>, ? extends Optional<? extends N>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.success(function.apply(Optional.empty()));
        }

        @Override
        public BooleanResult<E> mapToBoolean(Function<? super Optional<T>, Boolean> function) {
            Objects.requireNonNull(function);
            return BooleanResult.success(
                    Objects.requireNonNull(function.apply(Optional.empty())));
        }

        @Override
        public <N> OptionalResult<T, N> mapError(Function<? super E, ? extends N> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> OptionalResult<N, E> mapValue(Function<? super T, ? extends N> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<N, E>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> OptionalResult<N, E> mapValueToOptional(
                Function<? super T, ? extends Optional<? extends N>> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<N, E>) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> Result<N, E> flatMap(
                Function<? super Optional<T>, ? extends Result<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return (Result<N, E>) Objects.requireNonNull(function.apply(Optional.empty()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> OptionalResult<N, E> flatMapToOptionalResult(
                Function<? super Optional<T>, ? extends OptionalResult<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<N, E>) Objects.requireNonNull(
                    function.apply(Optional.empty()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public OptionalResult<T, E> flatReplaceEmpty(
                Supplier<? extends OptionalResult<? extends T, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return (OptionalResult<T, E>) Objects.requireNonNull(supplier.get());
        }

        @SuppressWarnings("unchecked")
        @Override
        public Result<T, E> flatReplaceEmptyWithResult(
                Supplier<? extends Result<? extends T, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return (Result<T, E>) Objects.requireNonNull(supplier.get());
        }

        @SuppressWarnings("unchecked")
        @Override
        public BooleanResult<E> flatMapToBooleanResult(
                Function<? super Optional<T>, ? extends BooleanResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return (BooleanResult<E>) Objects.requireNonNull(function.apply(Optional.empty()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public VoidResult<E> flatMapToVoidResult(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return (VoidResult<E>) Objects.requireNonNull(function.apply(Optional.empty()));
        }

        @Override
        public <N> OptionalResult<N, E> flatMapValueWithResult(
                Function<? super T, ? extends Result<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.empty();
        }

        @Override
        public <N> OptionalResult<N, E> flatMapValueWithOptionalResult(
                Function<? super T, ? extends OptionalResult<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.empty();
        }

        @Override
        public OptionalResult<Boolean, E> flatMapValueWithBooleanResult(
                Function<? super T, ? extends BooleanResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.empty();
        }

        @Override
        public OptionalResult<T, E> recover(
                Function<? super E, ? extends Optional<? extends T>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public <F> OptionalResult<T, F> flatRecover(
                Function<? super E, ? extends OptionalResult<? extends T, ? extends F>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consume(Consumer<? super Optional<T>> consumer) {
            Objects.requireNonNull(consumer);
            consumer.accept(Optional.empty());
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeValue(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeError(Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(errorConsumer);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeEither(Consumer<? super Optional<T>> successConsumer,
                                                    Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(successConsumer);
            Objects.requireNonNull(errorConsumer);
            successConsumer.accept(Optional.empty());
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeEither(Consumer<? super T> valueConsumer,
                                                    Runnable emptyRunnable,
                                                    Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(valueConsumer);
            Objects.requireNonNull(emptyRunnable);
            Objects.requireNonNull(errorConsumer);
            emptyRunnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatConsume(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            VoidResult<? extends E> result =
                    Objects.requireNonNull(function.apply(Optional.empty()));
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> flatConsumeValue(
                Function<? super T, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfSuccess(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfValue(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfNoValue(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfEmpty(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfError(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runEither(Runnable successRunnable, Runnable errorRunnable) {
            Objects.requireNonNull(successRunnable);
            Objects.requireNonNull(errorRunnable);
            successRunnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runEither(Runnable valueRunnable,
                                                Runnable emptyRunnable,
                                                Runnable errorRunnable) {
            Objects.requireNonNull(valueRunnable);
            Objects.requireNonNull(emptyRunnable);
            Objects.requireNonNull(errorRunnable);
            emptyRunnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runAlways(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatRunIfSuccess(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            VoidResult<? extends E> result = Objects.requireNonNull(supplier.get());
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> flatRunIfValue(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                             Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorSupplier);
            if (predicate.test(Optional.empty())) {
                return safeCast();
            } else {
                return OptionalResult.error(errorSupplier.get());
            }
        }

        @Override
        public OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                             Function<? super Optional<T>, ? extends E> errorFunction) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorFunction);
            if (predicate.test(Optional.empty())) {
                return safeCast();
            } else {
                return OptionalResult.error(errorFunction.apply(Optional.empty()));
            }
        }

        @Override
        public OptionalResult<T, E> verify(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            VoidResult<? extends E> result =
                    Objects.requireNonNull(function.apply(Optional.empty()));
            return result.fold(() -> this, OptionalResult::error);
        }

        @Override
        public OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                                  Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorSupplier);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                                  Function<? super T, ? extends E> errorFunction) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorFunction);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verifyValue(
                Function<? super T, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public <N> N fold(Function<? super Optional<T>, ? extends N> successFunction,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(successFunction);
            Objects.requireNonNull(errorFunction);
            return successFunction.apply(Optional.empty());
        }

        @Override
        public <N> N fold(Function<? super T, ? extends N> valueFunction,
                          Supplier<? extends N> emptySupplier,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(valueFunction);
            Objects.requireNonNull(emptySupplier);
            Objects.requireNonNull(errorFunction);
            return emptySupplier.get();
        }

        @Override
        public Optional<T> orElse(Optional<? extends T> other) {
            Objects.requireNonNull(other);
            return Optional.empty();
        }

        @Override
        public T valueOrElse(T other) {
            return other;
        }

        @Override
        public Optional<T> orElseGet(Function<? super E, ? extends Optional<? extends T>> function) {
            Objects.requireNonNull(function);
            return Optional.empty();
        }

        @Override
        public T valueOrElseGet(Supplier<? extends T> supplier) {
            Objects.requireNonNull(supplier);
            return supplier.get();
        }

        @Override
        public <X extends Throwable> Optional<T> orElseThrow(
                Function<? super E, ? extends X> function) throws X {
            Objects.requireNonNull(function);
            return Optional.empty();
        }

        @Override
        public <X extends Throwable> T valueOrElseThrow(
                Supplier<? extends X> supplier) throws X {
            Objects.requireNonNull(supplier);
            throw Objects.requireNonNull(supplier.get());
        }

        @Override
        public Result<T, E> toResult(Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(errorSupplier);
            return Result.error(errorSupplier.get());
        }

        @Override
        public VoidResult<E> toVoidResult() {
            return VoidResult.success();
        }

        @SuppressWarnings("unchecked")
        private <E1> OptionalResult.Empty<T, E1> safeCast() {
            return (OptionalResult.Empty<T, E1>) this;
        }
    }

    record Error<T, E>(E error) implements OptionalResult<T, E> {

        public Error(E error) {
            this.error = Objects.requireNonNull(error);
        }

        @Override
        public <N> Result<N, E> map(Function<? super Optional<T>, ? extends N> function) {
            Objects.requireNonNull(function);
            return Result.error(error);
        }

        @Override
        public <N> OptionalResult<N, E> mapToOptional(
                Function<? super Optional<T>, ? extends Optional<? extends N>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(error);
        }

        @Override
        public BooleanResult<E> mapToBoolean(Function<? super Optional<T>, Boolean> function) {
            Objects.requireNonNull(function);
            return BooleanResult.error(error);
        }

        @Override
        public <N> OptionalResult<T, N> mapError(Function<? super E, ? extends N> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(function.apply(error));
        }

        @Override
        public <N> OptionalResult<N, E> mapValue(Function<? super T, ? extends N> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(error);
        }

        @Override
        public <N> OptionalResult<N, E> mapValueToOptional(
                Function<? super T, ? extends Optional<? extends N>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(error);
        }

        @Override
        public <N> Result<N, E> flatMap(
                Function<? super Optional<T>, ? extends Result<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return Result.error(error);
        }

        @Override
        public <N> OptionalResult<N, E> flatMapToOptionalResult(
                Function<? super Optional<T>, ? extends OptionalResult<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(error);
        }

        @Override
        public OptionalResult<T, E> flatReplaceEmpty(
                Supplier<? extends OptionalResult<? extends T, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return this;
        }

        @Override
        public Result<T, E> flatReplaceEmptyWithResult(
                Supplier<? extends Result<? extends T, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return Result.error(error);
        }

        @Override
        public BooleanResult<E> flatMapToBooleanResult(
                Function<? super Optional<T>, ? extends BooleanResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return BooleanResult.error(error);
        }

        @Override
        public VoidResult<E> flatMapToVoidResult(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return VoidResult.error(error);
        }

        @Override
        public <N> OptionalResult<N, E> flatMapValueWithResult(
                Function<? super T, ? extends Result<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(error);
        }

        @Override
        public <N> OptionalResult<N, E> flatMapValueWithOptionalResult(
                Function<? super T, ? extends OptionalResult<? extends N, ? extends E>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(error);
        }

        @Override
        public OptionalResult<Boolean, E> flatMapValueWithBooleanResult(
                Function<? super T, ? extends BooleanResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.error(error);
        }

        @Override
        public OptionalResult<T, E> recover(
                Function<? super E, ? extends Optional<? extends T>> function) {
            Objects.requireNonNull(function);
            return OptionalResult.success(function.apply(error));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <F> OptionalResult<T, F> flatRecover(
                Function<? super E, ? extends OptionalResult<? extends T, ? extends F>> function) {
            Objects.requireNonNull(function);
            return (OptionalResult<T, F>) Objects.requireNonNull(function.apply(error));
        }

        @Override
        public OptionalResult<T, E> consume(Consumer<? super Optional<T>> consumer) {
            Objects.requireNonNull(consumer);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeValue(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeError(Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(errorConsumer);
            errorConsumer.accept(error);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeEither(Consumer<? super Optional<T>> successConsumer,
                                                    Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(successConsumer);
            Objects.requireNonNull(errorConsumer);
            errorConsumer.accept(error);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> consumeEither(Consumer<? super T> valueConsumer,
                                                    Runnable emptyRunnable,
                                                    Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(valueConsumer);
            Objects.requireNonNull(emptyRunnable);
            Objects.requireNonNull(errorConsumer);
            errorConsumer.accept(error);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatConsume(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatConsumeValue(
                Function<? super T, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfSuccess(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfValue(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfNoValue(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfEmpty(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runIfError(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runEither(Runnable successRunnable, Runnable errorRunnable) {
            Objects.requireNonNull(successRunnable);
            Objects.requireNonNull(errorRunnable);
            errorRunnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runEither(Runnable valueRunnable,
                                                Runnable emptyRunnable,
                                                Runnable errorRunnable) {
            Objects.requireNonNull(valueRunnable);
            Objects.requireNonNull(emptyRunnable);
            Objects.requireNonNull(errorRunnable);
            errorRunnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> runAlways(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatRunIfSuccess(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> flatRunIfValue(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                             Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorSupplier);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verify(Predicate<? super Optional<T>> predicate,
                                             Function<? super Optional<T>, ? extends E> errorFunction) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorFunction);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verify(
                Function<? super Optional<T>, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                                  Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorSupplier);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verifyValue(Predicate<? super T> predicate,
                                                  Function<? super T, ? extends E> errorFunction) {
            Objects.requireNonNull(predicate);
            Objects.requireNonNull(errorFunction);
            return safeCast();
        }

        @Override
        public OptionalResult<T, E> verifyValue(
                Function<? super T, ? extends VoidResult<? extends E>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public <N> N fold(Function<? super Optional<T>, ? extends N> successFunction,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(successFunction);
            Objects.requireNonNull(errorFunction);
            return errorFunction.apply(error);
        }

        @Override
        public <N> N fold(Function<? super T, ? extends N> valueFunction,
                          Supplier<? extends N> emptySupplier,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(valueFunction);
            Objects.requireNonNull(emptySupplier);
            Objects.requireNonNull(errorFunction);
            return errorFunction.apply(error);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Optional<T> orElse(Optional<? extends T> other) {
            return (Optional<T>) Objects.requireNonNull(other);
        }

        @Override
        public T valueOrElse(T other) {
            return other;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Optional<T> orElseGet(Function<? super E, ? extends Optional<? extends T>> function) {
            Objects.requireNonNull(function);
            return (Optional<T>) Objects.requireNonNull(function.apply(error));
        }

        @Override
        public T valueOrElseGet(Supplier<? extends T> supplier) {
            Objects.requireNonNull(supplier);
            return supplier.get();
        }

        @Override
        public <X extends Throwable> Optional<T> orElseThrow(
                Function<? super E, ? extends X> function) throws X {
            Objects.requireNonNull(function);
            throw Objects.requireNonNull(function.apply(error));
        }

        @Override
        public <X extends Throwable> T valueOrElseThrow(
                Supplier<? extends X> supplier) throws X {
            Objects.requireNonNull(supplier);
            throw Objects.requireNonNull(supplier.get());
        }

        @Override
        public Result<T, E> toResult(Supplier<? extends E> errorSupplier) {
            Objects.requireNonNull(errorSupplier);
            return Result.error(error);
        }

        @Override
        public VoidResult<E> toVoidResult() {
            return VoidResult.error(error);
        }

        @SuppressWarnings("unchecked")
        private <E1> OptionalResult.Error<T, E1> safeCast() {
            return (OptionalResult.Error<T, E1>) this;
        }
    }
}
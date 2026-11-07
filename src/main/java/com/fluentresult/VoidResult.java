package com.fluentresult;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A result object which either is in success state with no value, or in error
 * state containing a non-{@code null} error value.
 * <p>
 * A variable whose type is {@code VoidResult} should never itself be
 * {@code null}, it should always point to a {@code VoidResult} instance.
 * <p>
 * All methods taking functional arguments (functions, suppliers, consumers
 * and runnables) check them for {@code null} eagerly. A {@code null} argument
 * results in a {@link NullPointerException} regardless of the state of the
 * result, even if the argument would not have been used.
 *
 * @param <E> the type of the error value
 */
public sealed interface VoidResult<E> {

    /**
     * Returns a {@code VoidResult} in success state.
     *
     * @param <E> the type of the error value
     * @return a {@code VoidResult} in success state
     */
    static <E> VoidResult<E> success() {
        return new Success<>();
    }

    /**
     * Returns a {@code VoidResult} in error state containing the given
     * non-{@code null} value as error value.
     *
     * @param value the error value, which must be non-{@code null}
     * @param <E> the type of the error value
     * @return a {@code VoidResult} in error state containing the given error
     * value
     * @throws NullPointerException if given error value is {@code null}
     */
    static <E> VoidResult<E> error(E value) {
        return new Error<>(Objects.requireNonNull(value));
    }

    /**
     * If in error state, returns a {@code VoidResult} containing the result of
     * applying the given mapping function to the error value, otherwise returns
     * the unaltered {@code VoidResult} in success state.
     *
     * @param function the mapping function to apply to the error value, if
     * error state
     * @param <N> the type of the value returned from the mapping function
     * @return a {@code VoidResult} containing the result of applying the
     * mapping function to the error value of this {@code VoidResult}, if in
     * error state, otherwise the unaltered {@code VoidResult} in success state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <N> VoidResult<N> mapError(Function<? super E, ? extends N> function);

    /**
     * If in success state, returns a {@code Result} containing the value
     * provided by the given supplier, otherwise returns a {@code Result}
     * containing the error value of this {@code VoidResult}.
     *
     * @param supplier the supplier to provide the value if success state, must
     * not return {@code null}
     * @param <N> the type of the value provided by the supplier
     * @return a {@code Result} containing the value provided by the given
     * supplier, if in success state, otherwise a {@code Result} containing the
     * error value of this {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    <N> Result<N, E> replace(Supplier<? extends N> supplier);

    /**
     * If in success state, returns an {@code OptionalResult} containing the
     * optional value provided by the given supplier, otherwise returns an
     * {@code OptionalResult} containing the error value of this
     * {@code VoidResult}.
     *
     * @param supplier the supplier to provide the optional value if success
     * state, must not return {@code null}
     * @param <N> the type of the value which may be present in the
     * {@code Optional} provided by the supplier
     * @return an {@code OptionalResult} containing the optional value provided
     * by the given supplier, if in success state, otherwise an
     * {@code OptionalResult} containing the error value of this
     * {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    <N> OptionalResult<N, E> replaceWithOptional(
            Supplier<? extends Optional<? extends N>> supplier);

    /**
     * If in success state, returns a {@code BooleanResult} containing the
     * boolean value provided by the given supplier, otherwise returns a
     * {@code BooleanResult} containing the error value of this
     * {@code VoidResult}.
     *
     * @param supplier the supplier to provide the boolean value if success
     * state, must not return {@code null}
     * @return a {@code BooleanResult} containing the boolean value provided by
     * the given supplier, if in success state, otherwise a
     * {@code BooleanResult} containing the error value of this
     * {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    BooleanResult<E> replaceWithBoolean(Supplier<Boolean> supplier);

    /**
     * If in success state, returns the {@code Result} provided by the given
     * supplier, otherwise returns a {@code Result} containing the error value
     * of this {@code VoidResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code Result} provided by the supplier
     * @param supplier the supplier to provide the {@code Result}, if success
     * state
     * @return the {@code Result} provided by the supplier, if in success state,
     * otherwise a {@code Result} containing the error value of this
     * {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    <N> Result<N, E> flatReplace(
            Supplier<? extends Result<? extends N, ? extends E>> supplier);

    /**
     * If in success state, returns the {@code OptionalResult} provided by the
     * given supplier, otherwise returns an {@code OptionalResult} containing
     * the error value of this {@code VoidResult}.
     *
     * @param <N> the type of success value which may be present in the
     * {@code OptionalResult} provided by the supplier
     * @param supplier the supplier to provide the {@code OptionalResult}, if
     * success state
     * @return the {@code OptionalResult} provided by the supplier, if in
     * success state, otherwise an {@code OptionalResult} containing the error
     * value of this {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    <N> OptionalResult<N, E> flatReplaceToOptionalResult(
            Supplier<? extends OptionalResult<? extends N, ? extends E>> supplier);

    /**
     * If in success state, returns the {@code BooleanResult} provided by the
     * given supplier, otherwise returns a {@code BooleanResult} containing the
     * error value of this {@code VoidResult}.
     *
     * @param supplier the supplier to provide the {@code BooleanResult}, if
     * success state
     * @return the {@code BooleanResult} provided by the supplier, if in
     * success state, otherwise a {@code BooleanResult} containing the error
     * value of this {@code VoidResult}
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    BooleanResult<E> flatReplaceToBooleanResult(
            Supplier<? extends BooleanResult<? extends E>> supplier);

    /**
     * If in success state, returns the {@code VoidResult} provided by the given
     * supplier, otherwise returns the unaltered {@code VoidResult} in error
     * state.
     *
     * @param supplier the supplier to provide the {@code VoidResult}, if
     * success state
     * @return the {@code VoidResult} provided by the supplier, if in success
     * state, otherwise the unaltered {@code VoidResult} in error state
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    VoidResult<E> flatReplaceToVoidResult(
            Supplier<? extends VoidResult<? extends E>> supplier);

    /**
     * If in error state, returns a new {@code VoidResult} in success state,
     * discarding the error value, otherwise returns the unaltered
     * {@code VoidResult} in success state.
     * <p>
     * This method never throws.
     *
     * @return a {@code VoidResult} in success state
     */
    VoidResult<E> recover();

    /**
     * If in error state, returns the {@code VoidResult} from applying the given
     * mapping function to the error value, otherwise returns the unaltered
     * {@code VoidResult} in success state.
     *
     * @param function the mapping function to apply to the error value to
     * convert to a new {@code VoidResult}, if error state
     * @param <F> the type of error value which may be present in the
     * {@code VoidResult} returned by the mapping function
     * @return the {@code VoidResult} returned from the mapping function, if in
     * error state, otherwise the unaltered {@code VoidResult} in success state
     * @throws NullPointerException if the given mapping function is
     * {@code null} or returns {@code null}
     */
    <F> VoidResult<F> flatRecover(
            Function<? super E, ? extends VoidResult<? extends F>> function);

    /**
     * If in error state, applies the error value to the given consumer,
     * otherwise does nothing.
     *
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given consumer is {@code null}
     */
    VoidResult<E> consumeError(Consumer<? super E> errorConsumer);

    /**
     * If in success state, runs the given success runnable. If in error state,
     * applies the error value to the given error consumer.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorConsumer the consumer which accepts the error value
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if either the given runnable or the
     * consumer is {@code null}
     */
    VoidResult<E> consumeEither(
            Runnable successRunnable,
            Consumer<? super E> errorConsumer);

    /**
     * If in success state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if success state
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    VoidResult<E> runIfSuccess(Runnable runnable);

    /**
     * If in error state, runs the given runnable, otherwise does nothing.
     *
     * @param runnable the runnable to run if error state
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    VoidResult<E> runIfError(Runnable runnable);

    /**
     * If in success state, runs the given success runnable. If in error state,
     * runs the given error runnable.
     *
     * @param successRunnable the runnable to run if success state
     * @param errorRunnable the runnable to run if error state
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if one of the given runnables is {@code null}
     */
    VoidResult<E> runEither(Runnable successRunnable, Runnable errorRunnable);

    /**
     * Runs the given runnable, no matter the state.
     *
     * @param runnable the runnable to run
     * @return the original {@code VoidResult} unaltered
     * @throws NullPointerException if the given runnable is {@code null}
     */
    VoidResult<E> runAlways(Runnable runnable);

    /**
     * If in success state, runs the given supplier. If the supplier returns a
     * {@code VoidResult} in success state, the original {@code VoidResult} is
     * returned unaltered. If the supplier returns a {@code VoidResult} in error
     * state, a {@code VoidResult} containing the error value is returned. If in
     * error state, the original {@code VoidResult} is returned unaltered.
     *
     * @param supplier the supplier to run
     * @return the original {@code VoidResult} unaltered if the given supplier
     * returns success or the original {@code VoidResult} is in error state,
     * otherwise a {@code VoidResult} containing the error value from the
     * supplier result
     * @throws NullPointerException if the given supplier is {@code null} or
     * returns {@code null}
     */
    VoidResult<E> flatRunIfSuccess(
            Supplier<? extends VoidResult<? extends E>> supplier);

    /**
     * Retrieve a value from this {@code VoidResult} by folding the states. If
     * in success state, return the value provided by the value supplier. If in
     * error state, return the value of applying the error function to the
     * error value.
     *
     * @param <N> the type of the retrieved value
     * @param valueSupplier the supplier to provide the value, if success
     * state, may return {@code null}
     * @param errorFunction the mapping function to apply to the error value, if
     * error state, may return {@code null}
     * @return the folded value provided by the value supplier or mapped from
     * the error value, may be {@code null}
     * @throws NullPointerException if either the given supplier or the
     * function is {@code null}
     */
    <N> N fold(Supplier<? extends N> valueSupplier,
               Function<? super E, ? extends N> errorFunction);

    /**
     * If in success state, does nothing, otherwise throws the exception
     * returned by the given function.
     *
     * @param <X> type of the exception to be thrown
     * @param function the mapping function producing an exception by applying
     * the error value, if not in success state
     * @throws X if in error state
     * @throws NullPointerException if the given function is {@code null} or
     * returns {@code null}
     */
    <X extends Throwable> void orElseThrow(
            Function<? super E, ? extends X> function) throws X;

    /**
     * Transforms this {@code VoidResult} to an {@code OptionalResult}. If in
     * success state, the {@code OptionalResult} will be in empty success state.
     * If in error state, the {@code OptionalResult} will be in error state
     * containing the error value from this {@code VoidResult}.
     * <p>
     * The returned {@code OptionalResult} will never have a value.
     *
     * @param <N> the type of the success value in the returned
     * {@code OptionalResult}, inferred from the context
     * @return an {@code OptionalResult} in empty success state or in error
     * state containing the error value from this {@code VoidResult}
     */
    <N> OptionalResult<N, E> toOptionalResult();

    /**
     * Handle the given {@code CheckedRunnable}. If the {@code CheckedRunnable}
     * executes successfully, the {@code VoidResult} will be in success state.
     * If the {@code CheckedRunnable} throws an exception, the
     * {@code VoidResult} will be in error state containing the thrown
     * exception.
     * <p>
     * Note! A custom {@code CheckedRunnable} is used here instead of
     * {@code Runnable} to allow handling of checked exceptions.
     *
     * @param runnable the {@code CheckedRunnable} to handle
     * @return a {@code VoidResult} either in success state, or in error state
     * containing the exception thrown by the {@code CheckedRunnable}
     * @throws NullPointerException if the given runnable is {@code null}
     */
    static VoidResult<Exception> handle(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable);
        try {
            runnable.run();
            return VoidResult.success();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            return VoidResult.error(e);
        }
    }

    /**
     * Handle the given {@code CheckedRunnable}. If the {@code CheckedRunnable}
     * executes successfully, the {@code VoidResult} will be in success state.
     * If the {@code CheckedRunnable} throws an exception, the
     * {@code VoidResult} will be in error state containing the result after
     * mapping the exception with the given exception mapper function.
     * <p>
     * Note! A custom {@code CheckedRunnable} is used here instead of
     * {@code Runnable} to allow handling of checked exceptions.
     *
     * @param runnable the {@code CheckedRunnable} to handle
     * @param exceptionMapper the function mapping a thrown exception to an
     * error value
     * @param <E> type of the error value after mapping a thrown exception
     * @return a {@code VoidResult} either in success state, or in error state
     * containing the result after mapping the exception thrown by the
     * {@code CheckedRunnable}
     * @throws NullPointerException if the given runnable is {@code null}, or
     * if the given exception mapper function is {@code null} or returns
     * {@code null}
     */
    static <E> VoidResult<E> handle(CheckedRunnable runnable,
                                    Function<? super Exception, ? extends E> exceptionMapper) {
        Objects.requireNonNull(runnable);
        Objects.requireNonNull(exceptionMapper);
        return handle(runnable).mapError(exceptionMapper);
    }

    record Success<E>() implements VoidResult<E> {

        @Override
        public <N> VoidResult<N> mapError(Function<? super E, ? extends N> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public <N> Result<N, E> replace(Supplier<? extends N> supplier) {
            Objects.requireNonNull(supplier);
            return Result.success(supplier.get());
        }

        @Override
        public <N> OptionalResult<N, E> replaceWithOptional(
                Supplier<? extends Optional<? extends N>> supplier) {
            Objects.requireNonNull(supplier);
            return OptionalResult.success(supplier.get());
        }

        @Override
        public BooleanResult<E> replaceWithBoolean(Supplier<Boolean> supplier) {
            Objects.requireNonNull(supplier);
            return BooleanResult.success(Objects.requireNonNull(supplier.get()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> Result<N, E> flatReplace(
                Supplier<? extends Result<? extends N, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return (Result<N, E>) Objects.requireNonNull(supplier.get());
        }

        @SuppressWarnings("unchecked")
        @Override
        public <N> OptionalResult<N, E> flatReplaceToOptionalResult(
                Supplier<? extends OptionalResult<? extends N, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return (OptionalResult<N, E>) Objects.requireNonNull(supplier.get());
        }

        @SuppressWarnings("unchecked")
        @Override
        public BooleanResult<E> flatReplaceToBooleanResult(
                Supplier<? extends BooleanResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return (BooleanResult<E>) Objects.requireNonNull(supplier.get());
        }

        @SuppressWarnings("unchecked")
        @Override
        public VoidResult<E> flatReplaceToVoidResult(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return (VoidResult<E>) Objects.requireNonNull(supplier.get());
        }

        @Override
        public VoidResult<E> recover() {
            return safeCast();
        }

        @Override
        public <F> VoidResult<F> flatRecover(
                Function<? super E, ? extends VoidResult<? extends F>> function) {
            Objects.requireNonNull(function);
            return safeCast();
        }

        @Override
        public VoidResult<E> consumeError(Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(errorConsumer);
            return safeCast();
        }

        @Override
        public VoidResult<E> consumeEither(Runnable successRunnable,
                                             Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(successRunnable);
            Objects.requireNonNull(errorConsumer);
            successRunnable.run();
            return safeCast();
        }

        @Override
        public VoidResult<E> runIfSuccess(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public VoidResult<E> runIfError(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public VoidResult<E> runEither(Runnable successRunnable, Runnable errorRunnable) {
            Objects.requireNonNull(successRunnable);
            Objects.requireNonNull(errorRunnable);
            successRunnable.run();
            return safeCast();
        }

        @Override
        public VoidResult<E> runAlways(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public VoidResult<E> flatRunIfSuccess(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            VoidResult<? extends E> result = Objects.requireNonNull(supplier.get());
            return result.fold(() -> this, VoidResult::error);
        }

        @Override
        public <N> N fold(Supplier<? extends N> valueSupplier,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(valueSupplier);
            Objects.requireNonNull(errorFunction);
            return valueSupplier.get();
        }

        @Override
        public <X extends Throwable> void orElseThrow(
                Function<? super E, ? extends X> function) throws X {
            Objects.requireNonNull(function);
        }

        @Override
        public <N> OptionalResult<N, E> toOptionalResult() {
            return OptionalResult.empty();
        }

        @SuppressWarnings("unchecked")
        private <E1> Success<E1> safeCast() {
            return (Success<E1>) this;
        }
    }

    record Error<E>(E error) implements VoidResult<E> {

        public Error(E error) {
            this.error = Objects.requireNonNull(error);
        }

        @Override
        public <N> VoidResult<N> mapError(Function<? super E, ? extends N> function) {
            Objects.requireNonNull(function);
            return VoidResult.error(function.apply(error));
        }

        @Override
        public <N> Result<N, E> replace(Supplier<? extends N> supplier) {
            Objects.requireNonNull(supplier);
            return Result.error(error);
        }

        @Override
        public <N> OptionalResult<N, E> replaceWithOptional(
                Supplier<? extends Optional<? extends N>> supplier) {
            Objects.requireNonNull(supplier);
            return OptionalResult.error(error);
        }

        @Override
        public BooleanResult<E> replaceWithBoolean(Supplier<Boolean> supplier) {
            Objects.requireNonNull(supplier);
            return BooleanResult.error(error);
        }

        @Override
        public <N> Result<N, E> flatReplace(
                Supplier<? extends Result<? extends N, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return Result.error(error);
        }

        @Override
        public <N> OptionalResult<N, E> flatReplaceToOptionalResult(
                Supplier<? extends OptionalResult<? extends N, ? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return OptionalResult.error(error);
        }

        @Override
        public BooleanResult<E> flatReplaceToBooleanResult(
                Supplier<? extends BooleanResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return BooleanResult.error(error);
        }

        @Override
        public VoidResult<E> flatReplaceToVoidResult(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return safeCast();
        }

        @Override
        public VoidResult<E> recover() {
            return VoidResult.success();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <F> VoidResult<F> flatRecover(
                Function<? super E, ? extends VoidResult<? extends F>> function) {
            Objects.requireNonNull(function);
            return (VoidResult<F>) Objects.requireNonNull(function.apply(error));
        }

        @Override
        public VoidResult<E> consumeError(Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(errorConsumer);
            errorConsumer.accept(error);
            return safeCast();
        }

        @Override
        public VoidResult<E> consumeEither(Runnable successRunnable,
                                             Consumer<? super E> errorConsumer) {
            Objects.requireNonNull(successRunnable);
            Objects.requireNonNull(errorConsumer);
            errorConsumer.accept(error);
            return safeCast();
        }

        @Override
        public VoidResult<E> runIfSuccess(Runnable runnable) {
            Objects.requireNonNull(runnable);
            return safeCast();
        }

        @Override
        public VoidResult<E> runIfError(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public VoidResult<E> runEither(Runnable successRunnable, Runnable errorRunnable) {
            Objects.requireNonNull(successRunnable);
            Objects.requireNonNull(errorRunnable);
            errorRunnable.run();
            return safeCast();
        }

        @Override
        public VoidResult<E> runAlways(Runnable runnable) {
            Objects.requireNonNull(runnable);
            runnable.run();
            return safeCast();
        }

        @Override
        public VoidResult<E> flatRunIfSuccess(
                Supplier<? extends VoidResult<? extends E>> supplier) {
            Objects.requireNonNull(supplier);
            return safeCast();
        }

        @Override
        public <N> N fold(Supplier<? extends N> valueSupplier,
                          Function<? super E, ? extends N> errorFunction) {
            Objects.requireNonNull(valueSupplier);
            Objects.requireNonNull(errorFunction);
            return errorFunction.apply(error);
        }

        @Override
        public <X extends Throwable> void orElseThrow(
                Function<? super E, ? extends X> function) throws X {
            Objects.requireNonNull(function);
            throw Objects.requireNonNull(function.apply(error));
        }

        @Override
        public <N> OptionalResult<N, E> toOptionalResult() {
            return OptionalResult.error(error);
        }

        private Error<E> safeCast() {
            return this;
        }
    }
}
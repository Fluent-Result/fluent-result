/**
 * A result library helping you get rid of your exceptions, handling errors in
 * a more fluent way.
 * <p>
 * The module exports a single package, {@code com.fluentresult}, containing
 * the sealed result types {@link com.fluentresult.Result},
 * {@link com.fluentresult.OptionalResult},
 * {@link com.fluentresult.BooleanResult} and
 * {@link com.fluentresult.VoidResult}, together with the
 * {@link com.fluentresult.CheckedRunnable} functional interface used by
 * {@code VoidResult.handle}.
 */
module com.fluentresult {
    exports com.fluentresult;
}

/**
 * AssertJ assertions for Fluent Result.
 * <p>
 * The module exports the package {@code com.fluentresult.assertj}, whose entry
 * point is {@link com.fluentresult.assertj.FluentResultAssertions}.
 */
module com.fluentresult.assertj {
    requires transitive com.fluentresult;
    requires transitive org.assertj.core;
    exports com.fluentresult.assertj;
}

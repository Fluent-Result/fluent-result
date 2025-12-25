package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_Verify_Predicate_Error_Function_Test {

    @Test
    void verify_predicate_success_shouldKeepSuccessResultWhenVerifiedTrue() {
        OptionalResult<String, String> result = OptionalResult.<String, String>success("Success")
                .verify(
                        maybeVal -> maybeVal.map(val -> val.length() == 7).orElse(false),
                        (s) -> "ValidationError " + s);
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Should not be error"));
    }

    @Test
    void verify_predicate_success_shouldChangeToProvidedErrorWhenVerifiedFalse() {
        OptionalResult<String, String> result = OptionalResult.<String, String>success("Success")
                .verify(
                        maybeVal -> maybeVal.map(val -> val.length() == 5).orElse(false),
                        (s) -> "ValidationError " + s);
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError Optional[Success]"));
    }

    @Test
    void verify_predicate_error_shouldKeepOriginalError() {
        OptionalResult<String, String> result = OptionalResult.<String, String>error("Error")
                .verify(
                        maybeVal -> maybeVal.map(val -> val.length() == 5).orElse(false),
                        (s) -> "ValidationError " + s);
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verify_predicate_error_shouldNotRunVerificatorWhenError() {
        OptionalResult<String, String> result = OptionalResult.<String, String>error("Error")
                .verify(
                        val -> {
                            throw new RuntimeException();
                        },
                        (s) -> "ValidationError " + s);
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verify_predicate_success_nullVerificatorGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verify(null, (s) -> "ValidationError " + s))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_predicate_success_nullErrorSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verify(val -> true, (Function<Optional<String>, ? extends String>)  null))
                .isInstanceOf(NullPointerException.class);
    }
}
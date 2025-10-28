package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_Verify_Predicate_Test {

    @Test
    void verify_predicate_success_shouldKeepSuccessResultWhenVerifiedTrue() {
        OptionalResult<String, String> result = OptionalResult.<String, String>success("Success")
                .verify(
                        maybeVal -> maybeVal.map(val -> val.length() == 7).orElse(false),
                        () -> "ValidationError");
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
                        () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError"));
    }
    @Test
    void verify_predicate_empty_shouldKeepSuccessResultWhenVerifiedTrue() {
        OptionalResult<String, String> result = OptionalResult.<String, String>empty()
                .verify(
                        Optional::isEmpty,
                        () -> "ValidationError");
        List<String> results = new ArrayList<>();
        OptionalResult<String, String> consumed = result.consumeEither(
                val -> fail("Expected empty"),
                () -> results.add("Empty"),
                err -> fail("Should not be error"));
        assertThat(consumed).isNotNull();
    }

    @Test
    void verify_predicate_empty_shouldChangeToProvidedErrorWhenVerifiedFalse() {
        OptionalResult<String, String> result = OptionalResult.<String, String>empty()
                .verify(
                        Optional::isPresent,
                        () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("ValidationError"));
    }

    @Test
    void verify_predicate_error_shouldKeepOriginalError() {
        OptionalResult<String, String> result = OptionalResult.<String, String>error("Error")
                .verify(
                        maybeVal -> maybeVal.map(val -> val.length() == 5).orElse(false),
                        () -> "ValidationError");
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
                        () -> "ValidationError");
        result.consumeEither(
                val -> fail("Expected no value"),
                err -> assertThat(err).isEqualTo("Error"));
    }

    @Test
    void verify_predicate_success_nullVerificatorGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verify(null, () -> "ValidationError"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_predicate_success_nullErrorSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verify(val -> true, null))
                .isInstanceOf(NullPointerException.class);
    }
}

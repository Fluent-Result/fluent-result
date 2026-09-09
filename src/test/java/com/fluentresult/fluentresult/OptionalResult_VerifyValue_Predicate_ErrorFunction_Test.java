package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

class OptionalResult_VerifyValue_Predicate_ErrorFunction_Test {

    @Test
    void verifyValue_predicate_success_valueVerifiedTrue() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .verifyValue(val -> val.length() == 7, (v) -> "Error " + v);
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                () -> fail("Should not be empty"),
                err -> fail("Expected no error"));
    }

    @Test
    void verifyValue_predicate_success_valueVerifiedFalse() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>success("Success")
                        .verifyValue(val -> val.length() == 5, (v) -> "Error " + v);
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("Error Success"));
    }

    @Test
    void verifyValue_predicate_empty_shouldRemainEmpty() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>empty()
                        .verifyValue(val -> val.length() == 5, (v) -> "Error " + v);
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> {},
                err -> fail("Should not have error"));
    }

    @Test
    void verifyValue_predicate_error_shouldKeepOriginalError() {
        OptionalResult<String, String> result =
                OptionalResult.<String, String>error("OriginalError")
                        .verifyValue(val -> val.length() == 5, (v) -> "Error " + v);
        result.consumeEither(
                val -> fail("Should not have value"),
                () -> fail("Should not be empty"),
                err -> assertThat(err).isEqualTo("OriginalError"));
    }

    @Test
    void verifyValue_predicate_empty_shouldNotRunVerificatorWhenEmpty() {
        OptionalResult<Object, Object> result =
                OptionalResult.empty()
                        .verifyValue(
                                val -> {
                                    throw new RuntimeException();
                                },
                                (v) -> "Error " + v);
        assertThat(result).isNotNull();
    }

    @Test
    void verifyValue_predicate_error_shouldNotRunVerificatorWhenError() {
        OptionalResult<Object, String> result = OptionalResult.error("OriginalError")
                .verifyValue(
                        val -> {
                            throw new RuntimeException();
                        },
                        (v) -> "Error " + v);
        assertThat(result).isNotNull();
    }

    @Test
    void verifyValue_predicate_success_nullVerificatorGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verifyValue(null, () -> "ValidationError"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void verify_success_nullErrorSupplierGivesNPE() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        assertThatThrownBy(() -> result.verifyValue(val -> true, (Function<String, ? extends String>) null))
                .isInstanceOf(NullPointerException.class);
    }
}
package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class VoidResult_Replace_Test {

    @Test
    void replace_success_successfullyMapValue() {
        Result<String, String> result =
                VoidResult.<String>success()
                        .replace(() -> "Success");
        result.consumeEither(
                val -> assertThat(val).isEqualTo("Success"),
                err -> fail("Expected no error"));
    }

    @Test
    void replace_error_notNull() {
        VoidResult<String> result = VoidResult.error("Error");
        Result<String, String> replaced = result.replace(() -> "Replaced");
        assertThat(replaced).isNotNull();
    }
}
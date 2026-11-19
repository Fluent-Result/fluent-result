package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class OptionalResult_FlatRecover_Test {

    @Test
    void flatRecover_success_shouldReturnValue() {
        OptionalResult<String, String> result = OptionalResult.success("Success");
        result.flatRecover(error -> OptionalResult.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(Optional.of("Success")),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRecover_error_should_return_recovered_value() {
        OptionalResult<String, String> result = OptionalResult.error("Error");
        result.flatRecover(error -> OptionalResult.success("replaced error " + error))
                .consumeEither(
                        value -> assertThat(value).isEqualTo(Optional.of("replaced error Error")),
                        err -> fail("Should not be error")
                );
    }

    @Test
    void flatRecover_empty_notNull() {
        OptionalResult<Integer, String> result = OptionalResult.empty();
        OptionalResult<Integer, String> flatRecovered = result.flatRecover(s -> OptionalResult.success(s.length()));
        assertThat(flatRecovered).isNotNull();
    }

    @Test
    void flatRevover_success_change_error_type() {
        OptionalResult<Integer, String> result = OptionalResult.success(1);
        OptionalResult<Integer, Integer> result2 = result.flatRecover(e -> OptionalResult.success(e.length()));
        assertThat(result2).isNotNull();
    }

    @Test
    void flatRevover_error_change_error_type() {
        OptionalResult<Integer, String> result = OptionalResult.error("1");
        OptionalResult<Integer, Integer> result2 = result.flatRecover(e -> OptionalResult.success(e.length()));
        assertThat(result2).isNotNull();
    }
}

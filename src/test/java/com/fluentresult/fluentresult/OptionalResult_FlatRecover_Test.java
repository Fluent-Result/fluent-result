package com.fluentresult.fluentresult;

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
        OptionalResult<String, String> flatRecovered = result.flatRecover(s -> OptionalResult.success(s.toLowerCase()));
        assertThat(flatRecovered).isNotNull();
    }
}

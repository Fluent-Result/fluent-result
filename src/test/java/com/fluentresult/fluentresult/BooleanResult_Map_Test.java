package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class BooleanResult_Map_Test {

    @Test
    void map_success_successfullyMapValue() {
        Result<Integer, String> result =
                BooleanResult.<String>success(true)
                        .map(val -> val ? 5 : 3);
        result.consumeEither(
                val -> assertThat(val).isEqualTo(5),
                err -> fail("Should not be error"));
    }

    @Test
    void map_error_not_null() {
        Result<String, String> error = BooleanResult.error("Error")
                .map(value -> value.toString());
        assertThat(error).isNotNull();
        switch (error) {
            case Result.Success<String,String> ignored -> fail("Should be error");
            case Result.Error<String, String>(String e)  -> {
                assertThat(e).isEqualTo("Error");
            }
        }
    }
}
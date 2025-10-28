package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;

public class BooleanResult_ToOptionalResult_Test {

    @Test
    void toOptionalResult_error_return_error() {
        BooleanResult<String> result = BooleanResult.error("Error");
        OptionalResult<Boolean, String> optionalResult = result.toOptionalResult();
        assertThat(optionalResult).isNotNull();
        switch (optionalResult) {
            case OptionalResult.Empty<Boolean, String> v -> {
                fail("Expected error");
            }
            case OptionalResult.Error<Boolean, String>(String error)  -> {
                assertThat(error).isEqualTo("Error");
            }
            case OptionalResult.Value<Boolean, String> v -> {
                fail("Expected error");
            }
        }
    }
}

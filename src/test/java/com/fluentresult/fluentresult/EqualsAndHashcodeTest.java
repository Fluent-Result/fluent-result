package com.fluentresult.fluentresult;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class EqualsAndHashcodeTest {

    @Test
    void equals_sameObject() {
        Result<String, String> result = Result.success("Success");
        assertThat(result.equals(result)).isTrue();
    }
    @Test
    void error_equals_sameObject() {
        Result<String, String> result = Result.error("Error");
        assertThat(result.equals(result)).isTrue();
    }

    @Test
    void equals_afterConsume() {
        Result<String, String> result = Result.success("Success");
        Result<String, String> consumed = result.consume(val -> {});
        assertThat(result.equals(consumed)).isTrue();
    }

    @Test
    void equals_differentObjectsSameContent() {
        Result<String, String> result = Result.success("Success");
        Result<String, String> result2 = Result.success("Success");
        assertThat(result.equals(result2)).isTrue();
    }

    @Test
    void error_equals_differentObjectsSameContent() {
        Result<String, String> result = Result.error("Error");
        Result<String, String> result2 = Result.error("Error");
        assertThat(result.equals(result2)).isTrue();
    }

    @Test
    void equals_differentObjectsDifferentContent() {
        Result<String, String> result = Result.success("Success");
        Result<String, String> result2 = Result.success("Another success");
        assertThat(result.equals(result2)).isFalse();
    }

    @Test
    void error_equals_differentObjectsDifferentContent() {
        Result<String, String> result = Result.error("Success");
        Result<String, String> result2 = Result.error("Another error");
        assertThat(result.equals(result2)).isFalse();
    }

    @Test
    void equals_sameValueButSuccessAndError() {
        Result<String, String> result = Result.success("Content");
        Result<String, String> result2 = Result.error("Content");
        assertThat(result.equals(result2)).isFalse();
    }

    @Test
    void equals_sameValueButDifferentResultClass() {
        Result<Boolean, String> result = Result.success(true);
        BooleanResult<String> result2 = BooleanResult.success(true);
        assertThat(result.equals(result2)).isFalse();
    }

    @Test
    void error_equals_sameValueButDifferentResultClass() {
        Result<Boolean, String> result = Result.error("Error");
        BooleanResult<String> result2 = BooleanResult.success(true);
        assertThat(result.equals(result2)).isFalse();
    }

    @Test
    void equals_nonResultClass() {
        Result<Boolean, String> result = Result.success(true);
        assertThat(result.equals("Test")).isFalse();
    }

    @Test
    void hashCode_equalForSameValue() {
        Result<String, String> result = Result.success("Hashcode");
        Result<String, String> result2 = Result.success("Hashcode");
        assertThat(result.hashCode()).isEqualTo(result2.hashCode());
    }

    @Test
    void hashCode_notEqualForDifferentValue() {
        Result<String, String> result = Result.success("Hashcode");
        Result<String, String> result2 = Result.success("HashcodeOther");
        assertThat(result.hashCode()).isNotEqualTo(result2.hashCode());
    }

    @Test
    void hashCode_notEqualForDifferentResultClass() {
        Result<Boolean, String> result = Result.success(true);
        BooleanResult<String> result2 = BooleanResult.success(true);
        assertThat(result.hashCode()).isNotEqualTo(result2.hashCode());
    }

    @Test
    void error_hashCode_equalForSameValue() {
        Result<String, String> result = Result.error("Hashcode");
        Result<String, String> result2 = Result.error("Hashcode");
        assertThat(result.hashCode()).isEqualTo(result2.hashCode());
    }

    @Test
    void error_hashCode_notEqualForDifferentValue() {
        Result<String, String> result = Result.error("Hashcode");
        Result<String, String> result2 = Result.error("HashcodeOther");
        assertThat(result.hashCode()).isNotEqualTo(result2.hashCode());
    }

    @Test
    void error_hashCode_notEqualForDifferentResultClass() {
        Result<Boolean, String> result = Result.error("Error");
        BooleanResult<String> result2 = BooleanResult.success(true);
        assertThat(result.hashCode()).isNotEqualTo(result2.hashCode());
    }
}

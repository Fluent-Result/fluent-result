package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class VoidResult_Handle_Test {

    @Test
    void handle_success_shouldBeSuccess() {
        List<String> resultList = new ArrayList<>();
        VoidResult<Exception> result = VoidResult.handle(() -> {});
        result.consumeEither(
                () -> resultList.add("Success"),
                err -> fail("Should not be error")
        );
        assertThat(resultList.get(0)).isEqualTo("Success");
    }

    @Test
    void handle_success_shouldContainException() {
        VoidResult<Exception> result = VoidResult.handle(() -> {
            throw new IOException("Error");
        });
        result.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isInstanceOf(IOException.class)
        );
    }

    @Test
    void handle_nullCallableGivesNPE() {
        assertThatThrownBy(() -> VoidResult.handle(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_interruptedException_shouldContainExceptionAndRestoreInterruptStatus() {
        VoidResult<Exception> result = VoidResult.handle(() -> {
            throw new InterruptedException("Interrupted");
        });
        boolean interrupted = Thread.interrupted(); // reads and clears the flag
        assertThat(interrupted).isTrue();
        result.consumeEither(
                () -> fail("Should not be success"),
                err -> assertThat(err).isInstanceOf(InterruptedException.class)
        );
    }

    @Test
    void handle_otherException_shouldNotInterruptThread() {
        VoidResult.handle(() -> {
            throw new IOException("Error");
        });
        assertThat(Thread.currentThread().isInterrupted()).isFalse();
    }
}

package com.fluentresult;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class BooleanResult_Handle_Test {

    @Test
    void handle_success_shouldContainValue() {
        BooleanResult<Exception> result =
                BooleanResult.handle(() -> true);
        result.consumeEither(
                val -> assertThat(val).isEqualTo(true),
                err -> fail("Should not be error")
        );
    }

    @Test
    void handle_success_shouldContainException() {
        BooleanResult<Exception> result = BooleanResult.handle(() -> {
            throw new IOException("Error");
        });
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isInstanceOf(IOException.class)
        );
    }

    @Test
    void handle_nullCallableGivesNPE() {
        assertThatThrownBy(() -> BooleanResult.handle(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_nullValueFromCallableGivesNPE() {
        assertThatThrownBy(() -> BooleanResult.handle(() -> null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void handle_interruptedException_shouldContainExceptionAndRestoreInterruptStatus() {
        BooleanResult<Exception> result = BooleanResult.handle(() -> {
            throw new InterruptedException("Interrupted");
        });
        boolean interrupted = Thread.interrupted(); // reads and clears the flag
        assertThat(interrupted).isTrue();
        result.consumeEither(
                val -> fail("Should not be success"),
                err -> assertThat(err).isInstanceOf(InterruptedException.class)
        );
    }

    @Test
    void handle_otherException_shouldNotInterruptThread() {
        BooleanResult.handle(() -> {
            throw new IOException("Error");
        });
        assertThat(Thread.currentThread().isInterrupted()).isFalse();
    }
}

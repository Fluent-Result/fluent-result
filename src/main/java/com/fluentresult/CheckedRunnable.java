package com.fluentresult;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}

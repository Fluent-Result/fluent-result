package com.fluentresult.fluentresult;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;
}

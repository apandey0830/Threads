package org.example;

public class OperationReport {

    private int successes;
    private int failures;
    private long totalTime;

    public OperationReport(int successes, int failures, long totalTime) {
        this.successes = successes;
        this.failures = failures;
        this.totalTime = totalTime;
    }

    // Getters for report information
    public int getSuccesses() {
        return successes;
    }

    public int getFailures() {
        return failures;
    }

    public long getTotalTime() {
        return totalTime;
    }


}

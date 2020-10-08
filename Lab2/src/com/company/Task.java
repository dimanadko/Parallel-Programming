package com.company;

public class Task {
    int producerID;
    long complexity;
    private boolean isInterrupted;
    private long timeLeft;

    Task(int producerID, int complexity) {
        this.producerID = producerID;
        this.complexity = complexity;
        this.isInterrupted = false;
        this.timeLeft = complexity;
    }

    public void interrupt() {
        isInterrupted = true;
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setProcessedTime(long processedTime) {
        timeLeft = Math.max(timeLeft - processedTime, 0);
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}

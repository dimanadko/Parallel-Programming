package com.company;

public class CPUProcess extends Thread {
    private final CPUQueue queue;
    private final int id;
    private final int tasksAmount;
    private final int interval;
    private final int complexity;

    CPUProcess(int id, CPUQueue queue, int tasksAmount, int interval, int complexity) {
        this.id = id;
        this.queue = queue;
        this.tasksAmount = tasksAmount;
        this.interval = interval;
        this.complexity = complexity;
    }

    public void run() {
        for (int i = 0; i < tasksAmount; i++) {
            try {
                Thread.sleep(interval);
                queue.put(new Task(id, complexity));
                System.out.println("Generate task of process " + id);
            } catch (InterruptedException ignored) {
            }
        }
    }
}

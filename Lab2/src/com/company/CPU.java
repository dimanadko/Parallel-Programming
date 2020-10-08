package com.company;

public class CPU extends Thread {
    CPUQueue queue;
    private int taskProducerID;

    CPU(CPUQueue queue) {
        this.queue = queue;
        this.taskProducerID = -1;

        this.queue.setCPU(this);
    }

    public void run() {
        while (!isInterrupted()) {
            Task task = queue.get();
            taskProducerID = task.producerID;
            System.out.println("Get task of process " + taskProducerID);
            synchronized (this) {
                long start = System.currentTimeMillis();
                try {
                    wait(task.getTimeLeft());
                } catch (InterruptedException ignored) {
                }
                taskProducerID = -1;
                long end = System.currentTimeMillis();
                task.setProcessedTime(end - start);
                if (task.getTimeLeft() > 0) {
                    task.interrupt();
                    queue.put(task);
                } else {
                    System.out.println("Task of process " + task.producerID + " is finished");
                }
            }
        }
    }

    public int getTaskProducerID() {
        return taskProducerID;
    }

    public boolean isBusy() {
        return taskProducerID == -1;
    }
}

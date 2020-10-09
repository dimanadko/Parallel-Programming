package com.company;

public class CPU2 extends Thread {
    private int id;
    CPUQueue queue;
    private int taskProducerID;

    CPU2(CPUQueue queue, int id) {
        this.id = id;
        this.queue = queue;
        this.taskProducerID = -1;

    }
    public void run() {
        while (!isInterrupted()) {
            Task task = queue.get2();
            taskProducerID = task.producerID;
            System.out.println("CPU 2 Get task of process " + taskProducerID);
            synchronized (this) {
                try {
                    wait(task.getTimeLeft());
                } catch (InterruptedException ignored) {
                }
                taskProducerID = -1;
                System.out.println("CPU 2 Task of process " + task.producerID + " is finished");
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

package com.company;

import java.util.LinkedList;

public class CPUQueue {
    private final LinkedList<Task> queue1 = new LinkedList<>();
    private final LinkedList<Task> queue2 = new LinkedList<>();

    public int queue1MaxSize = 0;
    public int queue2MaxSize = 0;
    public int interruptedTasksAmount = 0;

    private CPU cpu;

    public synchronized Task get() {
        try {
            while (queue1.isEmpty() && queue2.isEmpty()) {
                wait();
            }
        } catch (InterruptedException ignored) {
        }

        Task task;
        if (!queue1.isEmpty()) {
            task = queue1.remove();
        } else {
            task = queue2.remove();
        }

        return task;
    }

    public synchronized void put(Task task) {
        if (task.producerID == 1) {
            queue1.add(task);
            queue1MaxSize = Math.max(queue1MaxSize, queue1.size());
            if (cpu.getTaskProducerID() == 2) {
                synchronized (cpu) {
                    cpu.notify();
                }
            }
        } else if (!task.isInterrupted()) {
            queue2.add(task);
            queue2MaxSize = Math.max(queue2MaxSize, queue2.size());
        } else {
            System.out.println("Task of process " + task.producerID + " is interrupted");
            System.out.println("Time left " + task.getTimeLeft());
            queue2.addFirst(task);
            queue2MaxSize = Math.max(queue2MaxSize, queue2.size());
            interruptedTasksAmount += 1;
        }
        notify();
    }

    public void setCPU(CPU cpu) {
        this.cpu = cpu;
    }

    public int size() {
        return queue1.size() + queue2.size() + queue2.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}

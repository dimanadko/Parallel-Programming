package com.company;

public class Main {

    public static void main(String[] args) {
        int minComplexity1 = 550;
        int maxComplexity1 = 650;
        int minComplexity2 = 950;
        int maxComplexity2 = 1050;
        int minInterval1 = 1150;
        int maxInterval1 = 1250;
        int minInterval2 = 550;
        int maxInterval2 = 650;
        int tasksAmount1 = 10;
        int tasksAmount2 = 5;

        CPUQueue queue = new CPUQueue();
        CPU cpu1 = new CPU(queue, 1);
        CPU2 cpu2 = new CPU2(queue, 2);


        CPUProcess producer1 = new CPUProcess(
                1,
                queue,
                tasksAmount1,
                randValue(minInterval1, maxInterval1),
                randValue(minComplexity1, maxComplexity1));

        CPUProcess producer2 = new CPUProcess(
                2,
                queue,
                tasksAmount2,
                randValue(minInterval2, maxInterval2),
                randValue(minComplexity2, maxComplexity2));

        cpu1.start();
        cpu2.start();
        producer1.start();
        producer2.start();

        while (true) {
            if (!producer1.isAlive() && !producer2.isAlive() && queue.isEmpty() && cpu1.isBusy() && cpu2.isBusy()) {
                cpu1.interrupt();
                cpu2.interrupt();
                System.out.println("========Main finish========");
                System.out.println("Queue 1 max size: " + queue.queue1MaxSize);
                System.out.println("Queue 2 max size: " + queue.queue2MaxSize);
                System.out.println("Interrupted tasks amount: " + queue.interruptedTasksAmount);
                break;
            }
        }
    }

    public static int randValue(int min, int max) {
        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }
}

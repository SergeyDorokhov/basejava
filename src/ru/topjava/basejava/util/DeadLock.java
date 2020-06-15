package ru.topjava.basejava.util;

public class DeadLock {
    private static final String OBJECT_ONE = "objectOne";
    private static final String OBJECT_TWO = "objectTwo";

    public static void main(String[] args) {
        workWithObjects(OBJECT_ONE, OBJECT_TWO);
        workWithObjects(OBJECT_TWO, OBJECT_ONE);
    }

    private static void workWithObjects(String objectOne, String objectTwo) {
        new Thread(() -> {
            synchronized (objectOne) {
                sleep();
                System.out.println(objectOne);
                synchronized (objectTwo) {
                    System.out.println(objectTwo);
                }
            }
        }).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

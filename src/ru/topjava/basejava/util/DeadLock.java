package ru.topjava.basejava.util;

public class DeadLock {
    private static final Student STUDENT = new Student();
    private static final Teacher TEACHER = new Teacher();

    public static class Teacher {

        public String getName() {
            return "Teacher";
        }
    }

    public static class Student {

        public String getName() {
            return "Student";
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (STUDENT) {
                sleep();
                System.out.println(STUDENT.getName());
                synchronized (TEACHER) {
                    System.out.println(TEACHER.getName());
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (TEACHER) {
                sleep();
                System.out.println(TEACHER.getName());
                synchronized (STUDENT) {
                    System.out.println(STUDENT.getName());
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

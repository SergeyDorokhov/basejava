package ru.topjava.basejava.util;

public class DeadLock {
    private static final Student student = new Student();
    private static final Teacher teacher = new Teacher();

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
            synchronized (student) {
                sleep();
                System.out.println(student.getName());
                synchronized (teacher) {
                    System.out.println(teacher.getName());
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (teacher) {
                sleep();
                System.out.println(teacher.getName());
                synchronized (student) {
                    System.out.println(student.getName());
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

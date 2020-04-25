package ru.topjava.basejava;

import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.SortedArrayStorage;
import ru.topjava.basejava.storage.Storage;

/**
 * Test for your ru.topjava.basejava.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    static final Storage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r4 = new Resume();
        r4.setUuid("uuid3");

        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();

        Resume resumeBeforeUpdate = ARRAY_STORAGE.get("uuid3");
        ARRAY_STORAGE.update(r4);
        Resume resumeAfterUpdate = ARRAY_STORAGE.get("uuid3");
        System.out.println("Update is correct if false: " + (resumeBeforeUpdate == resumeAfterUpdate));
        ARRAY_STORAGE.update(r1);

        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
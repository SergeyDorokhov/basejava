package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int resumesNumber;

    public void clear() {
        Arrays.fill(storage, 0, resumesNumber, null);
        resumesNumber = 0;
    }

    public void save(Resume r) {
        if (resumesNumber <= storage.length - 1) {
            if (getResumeIndex(r.getUuid()) == -1) {
                storage[resumesNumber++] = r;
            } else {
                System.out.println("Resume exists");
            }
        } else {
            System.out.println("Storage is full");
        }
    }

    public Resume get(String uuid) {
        int index = getResumeIndex(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.println("Resume not exists");
        }
        return null;
    }

    public void update(Resume r) {
        int index = getResumeIndex(r.getUuid());
        if (index != -1) {
            storage[index] = r;
        } else {
            System.out.println("Resume not exists");
        }
    }

    public void delete(String uuid) {
        int index = getResumeIndex(uuid);
        if (index != -1) {
            storage[index] = storage[resumesNumber - 1];
            storage[resumesNumber - 1] = null;
            resumesNumber--;
        } else {
            System.out.println("Resume not exists");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, resumesNumber);
    }

    public int size() {
        return resumesNumber;
    }

    private int getResumeIndex(String uuid) {
        for (int i = 0; i < resumesNumber; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
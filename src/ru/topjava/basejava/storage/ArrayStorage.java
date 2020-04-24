package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private static final int STORAGE_LIMIT = 10_000;
    private final Resume[] storage = new Resume[STORAGE_LIMIT];
    private int resumesNumber;

    public void clear() {
        Arrays.fill(storage, 0, resumesNumber, null);
        resumesNumber = 0;
    }

    public void save(Resume resume) {
        if (resumesNumber < STORAGE_LIMIT) {
            if (getResumeIndex(resume.getUuid()) == -1) {
                storage[resumesNumber++] = resume;
            } else {
                System.out.println("Resume " + resume.getUuid() + " exists");
            }
        } else {
            System.out.println("Storage is full");
        }
    }

    public Resume get(String uuid) {
        int index = getResumeIndex(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.println("Resume " + uuid + " not exists");
        return null;
    }

    public void update(Resume resume) {
        int index = getResumeIndex(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("Resume " + resume.getUuid() + " not exists");
        }
    }

    public void delete(String uuid) {
        int index = getResumeIndex(uuid);
        if (index != -1) {
            storage[index] = storage[resumesNumber - 1];
            storage[resumesNumber - 1] = null;
            resumesNumber--;
        } else {
            System.out.println("Resume " + uuid + " not exists");
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
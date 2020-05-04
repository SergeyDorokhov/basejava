package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < resumesNumber; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertResumeToArray(Resume resume, int index) {
        storage[resumesNumber] = resume;
    }

    @Override
    protected void deleteResumeFromArray(int index) {
        storage[index] = storage[resumesNumber - 1];
    }
}
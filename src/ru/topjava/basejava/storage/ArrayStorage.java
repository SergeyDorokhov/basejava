package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getPointer(String uuid) {
        for (int i = 0; i < resumesNumber; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void insertToArray(Resume resume, int index) {
        storage[resumesNumber] = resume;
    }

    @Override
    protected void deleteFromArray(int index) {
        storage[index] = storage[resumesNumber - 1];
    }
}
package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        if (resumesNumber < STORAGE_LIMIT) {
            int index = getIndex(resume.getUuid());
            if (index < 0) {
                storage[resumesNumber] = resume;
                resumesNumber++;
            } else {
                System.out.println("Resume " + resume.getUuid() + " exists");
            }
        } else {
            System.out.println("Storage is full");
        }
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < resumesNumber; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
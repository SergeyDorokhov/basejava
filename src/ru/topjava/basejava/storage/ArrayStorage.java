package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public void clear() {
        Arrays.fill(storage, 0, resumesNumber, null);
        resumesNumber = 0;
    }

    @Override
    public void save(Resume resume) {
        if (resumesNumber < STORAGE_LIMIT) {
            if (getIndex(resume.getUuid()) == -1) {
                storage[resumesNumber++] = resume;
            } else {
                System.out.println("Resume " + resume.getUuid() + " exists");
            }
        } else {
            System.out.println("Storage is full");
        }
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("Resume " + resume.getUuid() + " not exists");
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
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
    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, resumesNumber);
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
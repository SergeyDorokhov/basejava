package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int resumesNumber;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, resumesNumber, null);
        resumesNumber = 0;
    }

    @Override
    protected void addToStorage(Resume resume, Object pointer) {
        if (resumesNumber == STORAGE_LIMIT) {
            throw new StorageException("Storage is full, ", resume.getUuid());
        }
        insertToArray(resume, (Integer) pointer);
        resumesNumber++;
    }

    @Override
    protected Resume getFromStorage(Object pointer) {
        return storage[(Integer) pointer];
    }

    @Override
    protected void updateStorage(Resume resume, Object pointer) {
        storage[(Integer) pointer] = resume;
    }

    @Override
    protected void deleteFromStorage(Object pointer) {
        deleteFromArray((Integer) pointer);
        storage[resumesNumber - 1] = null;
        resumesNumber--;
    }

    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, resumesNumber);
    }

    @Override
    public int size() {
        return resumesNumber;
    }

    @Override
    protected boolean isThereResume(Object pointer) {
        return (Integer) pointer >= 0;
    }

    protected abstract void insertToArray(Resume resume, int index);

    protected abstract void deleteFromArray(int index);
}
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
    protected void addToStorage(Resume resume, Object id) {
        if (resumesNumber == STORAGE_LIMIT) {
            throw new StorageException("Storage is full, ", resume.getUuid());
        }
        insertToArray(resume, (Integer) id);
        resumesNumber++;
    }

    @Override
    protected Resume getFromStorage(Object id) {
        return storage[(Integer) id];
    }

    @Override
    protected void updateStorage(Resume resume, Object id) {
        storage[(Integer) id] = resume;
    }

    @Override
    protected void deleteFromStorage(Object id) {
        deleteFromArray((Integer) id);
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

    protected abstract void insertToArray(Resume resume, int index);

    protected abstract void deleteFromArray(int index);
}
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
    protected boolean isExist(String uuid) {
        if (resumesNumber < STORAGE_LIMIT) {
            return getIndex(uuid) >= 0;
        }
        throw new StorageException("Storage is full, ", uuid);
    }

    @Override
    protected void addToStorage(Resume resume) {
        int index = getIndex(resume.getUuid());
        insertToArray(resume, index);
        resumesNumber++;
    }

    @Override
    protected Resume getFromStorage(String uuid) {
        return storage[getIndex(uuid)];
    }

    @Override
    protected void updateStorage(Resume resume) {
        int index = getIndex(resume.getUuid());
        storage[index] = resume;
    }

    @Override
    protected void deleteFromStorage(String uuid) {
        deleteFromArray(getIndex(uuid));
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
package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int resumesNumber;

    @Override
    public void clear() {
        Arrays.fill(storage, 0, resumesNumber, null);
        resumesNumber = 0;
    }

    @Override
    protected void addToStorage(Resume resume, Integer pointer) {
        if (resumesNumber == STORAGE_LIMIT) {
            throw new StorageException("Storage is full, ", resume.getUuid());
        }
        insertToArray(resume, pointer);
        resumesNumber++;
    }

    @Override
    protected Resume getFromStorage(Integer pointer) {
        return storage[pointer];
    }

    @Override
    protected void updateStorage(Resume resume, Integer pointer) {
        storage[pointer] = resume;
    }

    @Override
    protected void deleteFromStorage(Integer pointer) {
        deleteFromArray(pointer);
        storage[resumesNumber - 1] = null;
        resumesNumber--;
    }

    @Override
    public int size() {
        return resumesNumber;
    }

    @Override
    protected boolean isExist(Integer pointer) {
        return pointer >= 0;
    }

    @Override
    public List<Resume> getList() {
        return Arrays.asList(Arrays.copyOf(storage, resumesNumber));
    }

    protected abstract void insertToArray(Resume resume, int index);

    protected abstract void deleteFromArray(int index);
}
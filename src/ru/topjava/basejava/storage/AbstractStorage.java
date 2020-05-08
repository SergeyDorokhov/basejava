package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume resume) {
        addToStorage(resume, isNotExist(resume.getUuid()));
    }

    @Override
    public Resume get(String uuid) {
        return getFromStorage(isExist(uuid));
    }

    @Override
    public void update(Resume resume) {
        updateStorage(resume, isExist(resume.getUuid()));
    }

    @Override
    public void delete(String uuid) {
        deleteFromStorage(isExist(uuid));
    }

    protected Object isNotExist(String uuid) {
        Object pointer = getPointer(uuid);
        if (isThereResume(pointer)) {
            throw new ExistStorageException(uuid);
        }
        return pointer;
    }

    private Object isExist(String uuid) {
        Object pointer = getPointer(uuid);
        if (!isThereResume(pointer)) {
            throw new NotExistStorageException(uuid);
        }
        return pointer;
    }

    protected abstract Object getPointer(String uuid);

    protected abstract boolean isThereResume(Object pointer);

    protected abstract void addToStorage(Resume resume, Object pointer);

    protected abstract Resume getFromStorage(Object pointer);

    protected abstract void updateStorage(Resume resume, Object pointer);

    protected abstract void deleteFromStorage(Object pointer);
}
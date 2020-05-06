package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            addToStorage(resume, index);
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return getFromStorage(uuid, index);
        }
        throw new NotExistStorageException(uuid);
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            updateStorage(resume, index);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            deleteFromStorage(uuid, index);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract void addToStorage(Resume resume, int index);

    protected abstract Resume getFromStorage(String uuid, int index);

    protected abstract void updateStorage(Resume resume, int index);

    protected abstract void deleteFromStorage(String uuid, int index);

    protected abstract int getIndex(String uuid);
}
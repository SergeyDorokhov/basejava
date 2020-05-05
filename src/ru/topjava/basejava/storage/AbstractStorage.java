package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume resume) {
        if (isExist(resume.getUuid())) {
            throw new ExistStorageException(resume.getUuid());
        } else {
            addToStorage(resume);
        }
    }

    @Override
    public Resume get(String uuid) {
        if (isExist(uuid)) {
            return getFromStorage(uuid);
        }
        throw new NotExistStorageException(uuid);
    }

    @Override
    public void update(Resume resume) {
        if (isExist(resume.getUuid())) {
            updateStorage(resume);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public void delete(String uuid) {
        if (isExist(uuid)) {
            deleteFromStorage(uuid);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    protected abstract boolean isExist(String uuid);

    protected abstract void addToStorage(Resume resume);

    protected abstract Resume getFromStorage(String uuid);

    protected abstract void updateStorage(Resume resume);

    protected abstract void deleteFromStorage(String uuid);

    protected abstract int getIndex(String uuid);
}
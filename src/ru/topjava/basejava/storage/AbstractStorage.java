package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {


    @Override
    public void clear() {
        clearStorage();
    }

    @Override
    public void save(Resume resume) {
        if (checkCapacity()) {
            if (checkExistResume(resume.getUuid())) {
                throw new ExistStorageException(resume.getUuid());
            } else {
                addResumeStorage(resume);
            }
        } else {
            throw new StorageException("Storage is full, ", resume.getUuid());
        }
    }

    @Override
    public Resume get(String uuid) {
        if (checkExistResume(uuid)) {
            return getResume(uuid);
        }
        throw new NotExistStorageException(uuid);
    }

    @Override
    public void update(Resume resume) {
        if (checkExistResume(resume.getUuid())) {
            updateResume(resume);
        } else {
            throw new NotExistStorageException(resume.getUuid());
        }
    }

    @Override
    public void delete(String uuid) {
        if (checkExistResume(uuid)) {
            deleteResume(uuid);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    @Override
    public Resume[] getAll() {
        return getAllResumes();
    }

    @Override
    public int size() {
        return countSize();
    }

    protected abstract void clearStorage();

    protected abstract boolean checkCapacity();

    protected abstract boolean checkExistResume(String uuid);

    protected abstract void addResumeStorage(Resume resume);

    protected abstract Resume getResume(String uuid);

    protected abstract void updateResume(Resume resume);

    protected abstract void deleteResume(String uuid);

    protected abstract Resume[] getAllResumes();

    protected abstract int countSize();
}
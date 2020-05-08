package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume resume) {
        Object id = getID(resume.getUuid());
        isExist(resume.getUuid(), id);
        addToStorage(resume, id);
    }


    @Override
    public Resume get(String uuid) {
        Object id = getID(uuid);
        isNotExist(uuid, id);
        return getFromStorage(id);
    }


    @Override
    public void update(Resume resume) {
        Object id = getID(resume.getUuid());
        isNotExist(resume.getUuid(), id);
        updateStorage(resume, id);

    }

    @Override
    public void delete(String uuid) {
        Object id = getID(uuid);
        isNotExist(uuid, id);
        deleteFromStorage(id);
    }

    protected abstract void addToStorage(Resume resume, Object id);

    protected abstract Resume getFromStorage(Object id);

    protected abstract void updateStorage(Resume resume, Object id);

    protected abstract void deleteFromStorage(Object id);

    protected abstract Object getID(String uuid);

    protected void isExist(String uuid, Object id) {
        if ((Integer) id >= 0) {
            throw new ExistStorageException(uuid);
        }
    }

    private void isNotExist(String uuid, Object id) {
        if ((Integer) id < 0) {
            throw new NotExistStorageException(uuid);
        }
    }
}
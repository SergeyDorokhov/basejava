package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume resume) {
        addToStorage(resume, checkNotExist(resume.getUuid()));
    }

    @Override
    public Resume get(String uuid) {
        return getFromStorage(checkExist(uuid));
    }

    @Override
    public void update(Resume resume) {
        updateStorage(resume, checkExist(resume.getUuid()));
    }

    @Override
    public void delete(String uuid) {
        deleteFromStorage(checkExist(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> list = getList();
        list.sort(RESUME_COMPARATOR);
        return list;
    }

    private Object checkNotExist(String uuid) {
        Object pointer = getPointer(uuid);
        if (isExist(pointer)) {
            throw new ExistStorageException(uuid);
        }
        return pointer;
    }

    private Object checkExist(String uuid) {
        Object pointer = getPointer(uuid);
        if (!isExist(pointer)) {
            throw new NotExistStorageException(uuid);
        }
        return pointer;
    }

    protected static final Comparator<Resume> RESUME_COMPARATOR =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    protected abstract Object getPointer(String uuid);

    protected abstract boolean isExist(Object pointer);

    protected abstract void addToStorage(Resume resume, Object pointer);

    protected abstract Resume getFromStorage(Object pointer);

    protected abstract void updateStorage(Resume resume, Object pointer);

    protected abstract void deleteFromStorage(Object pointer);

    protected abstract List<Resume> getList();
}
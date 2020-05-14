package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<P> implements Storage {

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

    private P checkNotExist(String uuid) {
        P pointer = getPointer(uuid);
        if (isExist(pointer)) {
            throw new ExistStorageException(uuid);
        }
        return pointer;
    }

    private P checkExist(String uuid) {
        P pointer = getPointer(uuid);
        if (!isExist(pointer)) {
            throw new NotExistStorageException(uuid);
        }
        return pointer;
    }

    protected static final Comparator<Resume> RESUME_COMPARATOR =
            Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    protected abstract P getPointer(String uuid);

    protected abstract boolean isExist(P pointer);

    protected abstract void addToStorage(Resume resume, P pointer);

    protected abstract Resume getFromStorage(P pointer);

    protected abstract void updateStorage(Resume resume, P pointer);

    protected abstract void deleteFromStorage(P pointer);

    protected abstract List<Resume> getList();
}
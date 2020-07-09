package ru.topjava.basejava.storage;

import ru.topjava.basejava.exception.ExistStorageException;
import ru.topjava.basejava.exception.NotExistStorageException;
import ru.topjava.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<P> implements Storage {
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public void save(Resume resume) {
        LOG.info("Save " + resume);
        addToStorage(resume, checkNotExist(resume.getUuid()));
    }

    @Override
    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return getFromStorage(checkExist(uuid));
    }

    @Override
    public void update(Resume resume) {
        LOG.info("Update " + resume);
        updateStorage(resume, checkExist(resume.getUuid()));
    }

    @Override
    public void delete(String uuid) {
        LOG.info("Delete " + uuid);
        deleteFromStorage(checkExist(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = getList();
        list.sort(RESUME_COMPARATOR);
        return list;
    }

    private P checkNotExist(String uuid) {
        P pointer = getPointer(uuid);
        if (isExist(pointer)) {
            LOG.warning("Resume " + uuid + " exist");
            throw new ExistStorageException(uuid);
        }
        return pointer;
    }

    private P checkExist(String uuid) {
        P pointer = getPointer(uuid);
        if (!isExist(pointer)) {
            LOG.warning("Resume " + uuid + " not exist");
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
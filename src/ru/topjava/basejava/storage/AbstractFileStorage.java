package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File storage;

    public AbstractFileStorage(File storage) {
        Objects.requireNonNull(storage, "storage must not be null");
        if (!storage.isDirectory()) {
            throw new IllegalArgumentException(storage.getAbsolutePath() + " is not directory");
        }
        if (!storage.canRead() || !storage.canWrite()) {
            throw new IllegalArgumentException(storage.getAbsolutePath() + " is not readable/writeable");
        }
        this.storage = storage;
    }

    @Override
    protected File getPointer(String uuid) {
        return new File(storage, uuid);
    }

    @Override
    protected boolean isExist(File pointer) {
        return pointer.exists();
    }

    @Override
    protected void addToStorage(Resume resume, File pointer) {
        try {
            if (pointer.createNewFile()) {
                doWrite(resume, pointer);
            }
        } catch (IOException e) {
            throw new StorageException("IO error", pointer.getName(), e);
        }
    }

    @Override
    protected Resume getFromStorage(File pointer) {
        return doRead(pointer);
    }

    @Override
    protected void updateStorage(Resume resume, File pointer) {
        doWrite(resume, pointer);
    }

    @Override
    protected void deleteFromStorage(File pointer) {
        if (!pointer.delete()) {
            throw new StorageException("Delete error", pointer.getName());
        }
    }

    @Override
    protected List<Resume> getList() {
        File[] files = storage.listFiles();
        if (files == null) {
            throw new StorageException("Storage is invalid", null);
        }
        Resume[] resumes = new Resume[files.length];
        for (int i = 0; i < resumes.length; i++) {
            resumes[i] = doRead(files[i]);
        }
        return Arrays.asList(resumes);
    }

    @Override
    public void clear() {
        File[] files = storage.listFiles();
        if (files == null) {
            throw new StorageException("Storage is invalid", null);
        }
        for (File file : files) {
            deleteFromStorage(file);
        }
    }

    @Override
    public int size() {
        File[] files = storage.listFiles();
        if (files == null) {
            throw new StorageException("Storage is invalid", null);
        }
        return files.length;
    }

    protected abstract void doWrite(Resume resume, File pointer);

    protected abstract Resume doRead(File pointer);
}
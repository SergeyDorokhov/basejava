package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
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
                doWrite(resume, new BufferedOutputStream(new FileOutputStream(pointer)));
            }
        } catch (IOException e) {
            throw new StorageException("IO error", pointer.getName(), e);
        }
    }

    @Override
    protected Resume getFromStorage(File pointer) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(pointer)));
        } catch (IOException e) {
            throw new StorageException("File read error", pointer.getName(), e);
        }
    }

    @Override
    protected void updateStorage(Resume resume, File pointer) {
        try {
            doWrite(resume, new BufferedOutputStream(new FileOutputStream(pointer)));
        } catch (IOException e) {
            throw new StorageException("IO error", pointer.getName(), e);
        }
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
        List<Resume> resumes = new ArrayList<>(files.length);
        for (File resume : files) {
            resumes.add(getFromStorage(resume));
        }
        return resumes;
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

    protected abstract void doWrite(Resume resume, OutputStream os) throws IOException;

    protected abstract Resume doRead(InputStream is) throws IOException;
}
package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File storage;
    private final SerializationStrategy serializationStrategy;

    public FileStorage(File storage, SerializationStrategy serializationStrategy) {
        Objects.requireNonNull(storage, "storage must not be null");
        Objects.requireNonNull(serializationStrategy, "strategy must not be null");
        if (!storage.isDirectory()) {
            throw new IllegalArgumentException(storage.getAbsolutePath() + " is not directory");
        }
        if (!storage.canRead() || !storage.canWrite()) {
            throw new IllegalArgumentException(storage.getAbsolutePath() + " is not readable/writeable");
        }
        this.storage = storage;
        this.serializationStrategy = serializationStrategy;
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
                updateStorage(resume, pointer);
            }
        } catch (IOException e) {
            throw new StorageException("IO error", pointer.getName(), e);
        }
    }

    @Override
    protected Resume getFromStorage(File pointer) {
        try {
            return serializationStrategy.doRead(new BufferedInputStream(new FileInputStream(pointer)));
        } catch (IOException e) {
            throw new StorageException("File read error", pointer.getName(), e);
        }
    }

    @Override
    protected void updateStorage(Resume resume, File pointer) {
        try {
            serializationStrategy.doWrite(resume, new BufferedOutputStream(new FileOutputStream(pointer)));
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
        File[] files = getFiles();
        List<Resume> resumes = new ArrayList<>(files.length);
        for (File resume : files) {
            resumes.add(getFromStorage(resume));
        }
        return resumes;
    }

    @Override
    public void clear() {
        for (File file : getFiles()) {
            deleteFromStorage(file);
        }
    }

    @Override
    public int size() {
        return getFiles().length;
    }

    private File[] getFiles() {
        File[] files = storage.listFiles();
        if (files == null) {
            throw new StorageException("Storage is invalid", null);
        }
        return files;
    }
}
package ru.topjava.basejava.storage;

import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path storage;
    private final SerializationStrategy serializationStrategy;

    public PathStorage(String dir, SerializationStrategy serializationStrategy) {
        Objects.requireNonNull(dir, "storage must not be null");
        Objects.requireNonNull(serializationStrategy, "strategy must not be null");
        this.storage = Paths.get(dir);
        this.serializationStrategy = serializationStrategy;
        if (!Files.isDirectory(storage)) {
            throw new IllegalArgumentException(storage.getFileName() + " is not directory");
        }
        if (!Files.isReadable(storage) || !Files.isWritable(storage)) {
            throw new IllegalArgumentException(storage.getFileName() + " is not readable/writeable");
        }
    }

    @Override
    protected Path getPointer(String uuid) {
        return storage.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path pointer) {
        return Files.exists(pointer);
    }

    @Override
    protected void addToStorage(Resume resume, Path pointer) {
        try {
            updateStorage(resume, Files.createFile(pointer));
        } catch (IOException e) {
            throw new StorageException("Error creating a file", pointer.toString(), e);
        }
    }

    @Override
    protected Resume getFromStorage(Path pointer) {
        try {
            return serializationStrategy.doRead(new BufferedInputStream(Files.newInputStream(pointer)));
        } catch (IOException e) {
            throw new StorageException("Read error", pointer.toString(), e);
        }
    }

    @Override
    protected void updateStorage(Resume resume, Path pointer) {
        try {
            serializationStrategy.doWrite(resume, new BufferedOutputStream(Files.newOutputStream(pointer)));
        } catch (IOException e) {
            throw new StorageException("Deletion error", pointer.toString(), e);
        }
    }

    @Override
    protected void deleteFromStorage(Path pointer) {
        try {
            Files.delete(pointer);
        } catch (IOException e) {
            throw new StorageException("IO error", pointer.toString(), e);
        }
    }

    @Override
    protected List<Resume> getList() {
        return getStreamStorage().map(this::getFromStorage).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        getStreamStorage().forEach(this::deleteFromStorage);
    }

    @Override
    public int size() {
        return (int) getStreamStorage().count();
    }

    private Stream<Path> getStreamStorage() {
        try {
            return Files.list(storage);
        } catch (IOException e) {
            throw new StorageException("IO error", null, e);
        }
    }
}
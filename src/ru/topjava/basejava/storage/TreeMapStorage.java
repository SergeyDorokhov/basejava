package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TreeMapStorage extends AbstractStorage<Resume> {
    private final Map<String, Resume> storage = new TreeMap<>();

    @Override
    protected void addToStorage(Resume resume, Resume pointer) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(Resume pointer) {
        return storage.get(pointer.getUuid());
    }

    @Override
    protected void updateStorage(Resume resume, Resume pointer) {
        storage.replace(pointer.getUuid(), resume);
    }

    @Override
    protected void deleteFromStorage(Resume pointer) {
        storage.remove(pointer.getUuid());
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Resume getPointer(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected boolean isExist(Resume pointer) {
        return pointer != null;
    }

    @Override
    public List<Resume> getList() {
        return Arrays.asList(storage.values().toArray(new Resume[0]));
    }
}
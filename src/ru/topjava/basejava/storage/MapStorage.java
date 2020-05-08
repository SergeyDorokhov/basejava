package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void addToStorage(Resume resume, Object pointer) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(Object pointer) {
        return storage.get((String) pointer);
    }

    @Override
    protected void updateStorage(Resume resume, Object pointer) {
        storage.replace((String) pointer, resume);
    }

    @Override
    protected void deleteFromStorage(Object pointer) {
        storage.remove((String) pointer);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object getPointer(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected boolean isExist(Object pointer) {
        return pointer != null;
    }
}
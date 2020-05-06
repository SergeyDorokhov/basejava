package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void addToStorage(Resume resume, int index) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(String uuid, int index) {
        return storage.get(uuid);
    }

    @Override
    protected void updateStorage(Resume resume, int index) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void deleteFromStorage(String uuid, int index) {
        storage.remove(uuid);
    }

    @Override
    protected int getIndex(String uuid) {
        if (storage.containsKey(uuid)) {
            return 1;
        }
        return -1;
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
}
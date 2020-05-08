package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void addToStorage(Resume resume, Object id) {
        storage.put((String) id, resume);
    }

    @Override
    protected Resume getFromStorage(Object id) {
        return storage.get((String) id);
    }

    @Override
    protected void updateStorage(Resume resume, Object id) {
        storage.replace((String) id, resume);
    }

    @Override
    protected void deleteFromStorage(Object id) {
        storage.remove(id);
    }



/*    @Override
    protected int getIndex(String uuid) {
        if (storage.containsKey(uuid)) {
            return 1;
        }
        return -1;
    }*/

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
    protected Object getID(String uuid) {
        if (storage.containsKey(uuid)) {
            return uuid;
        }
        return null;
    }
}
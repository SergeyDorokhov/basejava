package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage extends AbstractStorage<String> {
    private final Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void addToStorage(Resume resume, String pointer) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected Resume getFromStorage(String pointer) {
        return storage.get(pointer);
    }

    @Override
    protected void updateStorage(Resume resume, String pointer) {
        storage.replace(pointer, resume);
    }

    @Override
    protected void deleteFromStorage(String pointer) {
        storage.remove(pointer);
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
    protected String getPointer(String uuid) {
        return storage.containsKey(uuid) ? uuid : null;
    }

    @Override
    protected boolean isExist(String pointer) {
        return pointer != null;
    }

    @Override
    public List<Resume> getList() {
        return Arrays.asList(storage.values().toArray(new Resume[0]));
    }
}
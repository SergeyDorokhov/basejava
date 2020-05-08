package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void addToStorage(Resume resume, Object pointer) {
        storage.add(resume);
    }

    @Override
    protected Resume getFromStorage(Object pointer) {
        return storage.get((Integer) pointer);
    }

    @Override
    protected void updateStorage(Resume resume, Object pointer) {
        storage.set((Integer) pointer, resume);
    }

    @Override
    protected void deleteFromStorage(Object pointer) {
        storage.remove(((Integer) pointer).intValue());
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Object getPointer(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isThereResume(Object pointer) {
        return (Integer) pointer >= 0;
    }
}
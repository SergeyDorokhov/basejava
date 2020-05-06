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
    protected void addToStorage(Resume resume, int index) {
        storage.add(resume);
    }

    @Override
    protected Resume getFromStorage(String uuid, int index) {
        return storage.get(index);
    }

    @Override
    protected void updateStorage(Resume resume, int index) {
        storage.set(index, resume);
    }

    @Override
    protected void deleteFromStorage(String uuid, int index) {
        storage.remove(new Resume(uuid));
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
    protected int getIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }
}
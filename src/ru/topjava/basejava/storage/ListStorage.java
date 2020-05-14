package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    private final List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void addToStorage(Resume resume, Integer pointer) {
        storage.add(resume);
    }

    @Override
    protected Resume getFromStorage(Integer pointer) {
        return storage.get(pointer);
    }

    @Override
    protected void updateStorage(Resume resume, Integer pointer) {
        storage.set(pointer, resume);
    }

    @Override
    protected void deleteFromStorage(Integer pointer) {
        storage.remove((pointer).intValue());
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Integer getPointer(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Integer pointer) {
        return pointer >= 0;
    }

    @Override
    protected List<Resume> getList() {
        return storage;
    }
}
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
    protected void addToStorage(Resume resume, Object object) {
        storage.add(resume);
    }

    @Override
    protected Resume getFromStorage(Object id) {
        return storage.get((Integer) id);
    }

    @Override
    protected void updateStorage(Resume resume, Object id) {
        storage.set((Integer) id, resume);
    }

    @Override
    protected void deleteFromStorage(Object id) {
        storage.remove(((Integer) id).intValue());
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

/*    @Override
    protected int getIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }*/

    @Override
    protected Object getID(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
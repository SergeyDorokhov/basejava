package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int resumesNumber;

    @Override
    public int size() {
        return resumesNumber;
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            return storage[index];
        }
        System.out.println("Resume " + uuid + " not exists");
        return null;
    }

    protected abstract int getIndex(String uuid);
}

package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

public interface Storage {
    void clear();

    void save(Resume resume);

    Resume get(String uuid);

    void update(Resume resume);

    void delete(String uuid);

    Resume[] getAll();

    int size();
}

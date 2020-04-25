package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * SortedArray based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void clear() {

    }

    @Override
    public void save(Resume resume) {

    }

    @Override
    public void update(Resume resume) {

    }

    @Override
    public void delete(String uuid) {

    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return null;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, STORAGE_LIMIT, searchKey);
    }
}


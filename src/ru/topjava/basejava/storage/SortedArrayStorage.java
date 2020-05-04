package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * SortedArray based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, resumesNumber, searchKey);
    }

    @Override
    protected void insertResumeToArray(Resume resume, int index) {
        int realIndex = -index - 1;
        System.arraycopy(storage, realIndex, storage, realIndex + 1, resumesNumber - realIndex);
        storage[realIndex] = resume;
    }

    @Override
    protected void deleteResumeFromArray(int index) {
        System.arraycopy(storage, index + 1, storage, index, resumesNumber - index - 1);
    }
}
package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

/**
 * SortedArray based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Object getPointer(String uuid) {
        Resume searchKey = new Resume(uuid);
        //return Arrays.binarySearch(storage, 0, resumesNumber, searchKey);
        return Arrays.binarySearch(storage, 0, resumesNumber, searchKey, RESUME_COMPARATOR);
    }

    @Override
    protected void insertToArray(Resume resume, int index) {
        int realIndex = -index - 1;
        System.arraycopy(storage, realIndex, storage, realIndex + 1, resumesNumber - realIndex);
        storage[realIndex] = resume;
    }

    @Override
    protected void deleteFromArray(int index) {
        System.arraycopy(storage, index + 1, storage, index, resumesNumber - index - 1);
    }

    private static final Comparator<Resume> RESUME_COMPARATOR = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());
}
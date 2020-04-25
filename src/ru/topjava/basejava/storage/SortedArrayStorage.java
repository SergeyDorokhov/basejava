package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

/**
 * SortedArray based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume resume) {
        if (resumesNumber < STORAGE_LIMIT) {
            int index = getIndex(resume.getUuid());
            if (index < 0) {
                int realIndex = -1 * index - 1;
                System.arraycopy(storage, realIndex, storage, realIndex + 1, resumesNumber - realIndex);
                storage[realIndex] = resume;
                resumesNumber++;
            } else {
                System.out.println("Resume " + resume.getUuid() + " exists");
            }
        } else {
            System.out.println("Storage is full");
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, resumesNumber, searchKey);
    }
}
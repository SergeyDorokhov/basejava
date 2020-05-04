package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10_000;
    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int resumesNumber;

    @Override
    protected void clearStorage() {
        Arrays.fill(storage, 0, resumesNumber, null);
        resumesNumber = 0;
    }

    @Override
    protected boolean checkCapacity() {
        return resumesNumber < STORAGE_LIMIT;
    }

    @Override
    protected boolean checkExistResume(String uuid) {
        return getIndex(uuid) >= 0;
    }

    @Override
    protected void addResumeStorage(Resume resume) {
        int index = getIndex(resume.getUuid());
        insertResumeToArray(resume, index);
        resumesNumber++;
    }

    @Override
    protected Resume getResume(String uuid) {
        return storage[getIndex(uuid)];
    }

    @Override
    protected void updateResume(Resume resume) {
        int index = getIndex(resume.getUuid());
        storage[index] = resume;
    }

    @Override
    protected void deleteResume(String uuid) {
        deleteResumeFromArray(getIndex(uuid));
        storage[resumesNumber - 1] = null;
        resumesNumber--;
    }

    @Override
    protected Resume[] getAllResumes() {
        return Arrays.copyOf(storage, resumesNumber);
    }

    @Override
    protected int countSize() {
        return resumesNumber;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void insertResumeToArray(Resume resume, int index);

    protected abstract void deleteResumeFromArray(int index);
}
package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    private final ArrayList<Resume> storage = new ArrayList<>();

    @Override
    public void clearStorage() {
        storage.clear();
    }

    @Override
    protected boolean checkCapacity() {
        return true;
    }

    @Override
    protected boolean checkExistResume(String uuid) {
        return storage.contains(new Resume(uuid));
    }

    @Override
    protected void addResumeStorage(Resume resume) {
        storage.add(resume);
    }

    @Override
    protected Resume getResume(String uuid) {
        return storage.get(storage.indexOf(new Resume(uuid)));
    }

    @Override
    protected void updateResume(Resume resume) {
        storage.remove(resume);
        storage.add(resume);
    }

    @Override
    protected void deleteResume(String uuid) {
        storage.remove(new Resume(uuid));
    }

    @Override
    protected Resume[] getAllResumes() {
        return (Resume[]) storage.toArray();
    }

    @Override
    protected int countSize() {
        return storage.size();
    }
}
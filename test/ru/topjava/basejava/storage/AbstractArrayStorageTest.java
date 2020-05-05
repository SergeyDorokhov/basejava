package ru.topjava.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.topjava.basejava.Exception.ExistStorageException;
import ru.topjava.basejava.Exception.NotExistStorageException;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    private final Resume RESUME_1 = new Resume("uuid1");
    private final Resume RESUME_2 = new Resume("uuid2");
    private final Resume RESUME_3 = new Resume("uuid3");
    private final Resume RESUME_4 = new Resume("uuid1");
    private final int STORAGE_REAL_SIZE = 3;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_2);
        storage.save(RESUME_1);
        storage.save(RESUME_3);
    }

    @Test
    public void clearTest() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void saveNewResumeTest() {
        storage.clear();
        storage.save(RESUME_1);
        assertEquals(1, storage.size());
        assertEquals(RESUME_1, storage.get(RESUME_1.getUuid()));

    }

    @Test(expected = ExistStorageException.class)
    public void saveExistResumeTest() {
        storage.save(RESUME_1);
    }

    @Test
    public void getExistResumeTest() {
        assertEquals(RESUME_1, storage.get(RESUME_1.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistResumeTest() {
        storage.get("");
    }

    @Test
    public void updateExistResumeTest() {
        storage.update(RESUME_4);
        assertEquals(RESUME_4, storage.get(RESUME_4.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExistResumeTest() {
        storage.update(new Resume());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteExistResumeTest() {
        storage.delete(RESUME_2.getUuid());
        assertEquals(STORAGE_REAL_SIZE - 1, storage.size());
        storage.get(RESUME_2.getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistResumeTest() {
        storage.delete(new Resume().getUuid());
    }

    @Test
    public void getAllTest() {
        Resume[] resumes = {RESUME_1, RESUME_2, RESUME_3};
        assertEquals(STORAGE_REAL_SIZE, storage.getAll().length);
        checkOrderResumesTest(storage, resumes);
    }

    @Test
    public void sizeTest() {
        assertEquals(storage.getAll().length, storage.size());
    }

    @Test(expected = StorageException.class)
    public void overflowStorageTest() {
        int storageMaxSize = 10_000;
        try {
            for (int i = 0; i < storageMaxSize - STORAGE_REAL_SIZE; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException ex) {
            fail();
        }
        storage.save(new Resume());
    }

    protected abstract void checkOrderResumesTest(Storage storage, Resume[] resumes);
}
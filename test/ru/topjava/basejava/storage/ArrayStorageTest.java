package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.assertEquals;

public class ArrayStorageTest extends AbstractArrayStorageTest {
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Override
    protected void checkOrderResumesTest(Storage storage, Resume RESUME_1, Resume RESUME_2, Resume RESUME_3) {
        assertEquals(RESUME_2, storage.getAll()[0]);
        assertEquals(RESUME_1, storage.getAll()[1]);
        assertEquals(RESUME_3, storage.getAll()[2]);
    }
}

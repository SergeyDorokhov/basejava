package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.assertEquals;

public class ArrayStorageTest extends AbstractArrayStorageTest {
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }

    @Override
    protected void checkOrderResumesTest(Storage storage, Resume[] resumes) {
        assertEquals(resumes[0], storage.getAll()[1]);
        assertEquals(resumes[1], storage.getAll()[0]);
        assertEquals(resumes[2], storage.getAll()[2]);
    }
}

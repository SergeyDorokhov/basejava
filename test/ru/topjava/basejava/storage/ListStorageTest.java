package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.assertEquals;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    protected void checkOrderResumesTest(Storage storage, Resume[] resumes) {
        assertEquals(resumes[0], storage.getAll()[1]);
        assertEquals(resumes[1], storage.getAll()[0]);
        assertEquals(resumes[2], storage.getAll()[2]);
    }
}
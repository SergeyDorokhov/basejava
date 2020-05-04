package ru.topjava.basejava.storage;

import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.assertEquals;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Override
    protected void checkOrderResumesTest(Storage storage, Resume RESUME_1, Resume RESUME_2, Resume RESUME_3) {
        assertEquals(RESUME_1, storage.getAll()[0]);
        assertEquals(RESUME_2, storage.getAll()[1]);
        assertEquals(RESUME_3, storage.getAll()[2]);
    }
}

package ru.topjava.basejava.storage;

import org.junit.Test;
import ru.topjava.basejava.exception.StorageException;
import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void overflowStorageTest() {
        storage.clear();
        int storageMaxSize = 10_000;
        try {
            for (int i = 0; i < storageMaxSize; i++) {
                storage.save(new Resume(""));
            }
        } catch (StorageException ex) {
            fail();
        }
        storage.save(new Resume(""));
    }
}
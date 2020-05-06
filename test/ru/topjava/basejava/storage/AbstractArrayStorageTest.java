package ru.topjava.basejava.storage;

import org.junit.Test;
import ru.topjava.basejava.Exception.StorageException;
import ru.topjava.basejava.model.Resume;

import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
        this.storage = storage;
    }

    @Test(expected = StorageException.class)
    public void overflowStorageTest() {
        storage.clear();
        int storageMaxSize = 10_000;
        try {
            for (int i = 0; i < storageMaxSize; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException ex) {
            fail();
        }
        storage.save(new Resume());
    }
}
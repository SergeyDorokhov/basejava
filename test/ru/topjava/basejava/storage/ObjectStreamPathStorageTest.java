package ru.topjava.basejava.storage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIRECTORY, new ObjectStreamSerialization()));
    }
}
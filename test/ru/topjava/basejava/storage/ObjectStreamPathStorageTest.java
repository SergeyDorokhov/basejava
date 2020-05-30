package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.serializer.ObjectStreamSerialization;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIRECTORY, new ObjectStreamSerialization()));
    }
}
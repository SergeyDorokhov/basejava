package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.serializer.DataStreamSerializer;

public class DataStreamPathStorageTest extends AbstractStorageTest {

    public DataStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIRECTORY, new DataStreamSerializer()));
    }
}
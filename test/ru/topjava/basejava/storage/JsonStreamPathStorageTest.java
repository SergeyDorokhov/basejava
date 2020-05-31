package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.serializer.JsonStreamSerializer;

public class JsonStreamPathStorageTest extends AbstractStorageTest {

    public JsonStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIRECTORY, new JsonStreamSerializer()));
    }
}
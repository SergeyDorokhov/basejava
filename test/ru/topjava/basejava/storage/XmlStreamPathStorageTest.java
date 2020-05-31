package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.serializer.XmlStreamSerializer;

public class XmlStreamPathStorageTest extends AbstractStorageTest {

    public XmlStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIRECTORY, new XmlStreamSerializer()));
    }
}
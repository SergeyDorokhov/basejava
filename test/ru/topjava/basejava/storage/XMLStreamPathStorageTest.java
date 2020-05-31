package ru.topjava.basejava.storage;

import ru.topjava.basejava.storage.serializer.XmlStreamSerializer;

public class XMLStreamPathStorageTest extends AbstractStorageTest {

    public XMLStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIRECTORY, new XmlStreamSerializer()));
    }
}
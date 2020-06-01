package ru.topjava.basejava.storage.serializer;

import ru.topjava.basejava.model.ContactType;
import ru.topjava.basejava.model.Resume;

import java.io.*;
import java.util.Map;

public class DataStreamSerializer implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            int count = resume.getContacts().size();
            dos.writeInt(count);
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                dos.writeUTF(entry.getKey().toString());
                dos.writeUTF(entry.getValue());
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        Resume resume;
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            resume = new Resume(uuid, fullName);
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                ContactType contactType = ContactType.valueOf(dis.readUTF());
                String contact = dis.readUTF();
                resume.addContact(contactType, contact);
            }
        }
        return resume;
    }
}

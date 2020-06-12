package ru.topjava.basejava.storage.serializer;

import ru.topjava.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            Set<Map.Entry<ContactType, String>> collectionContacts = resume.getContacts().entrySet();
            writeData(dos, collectionContacts, entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });
            Set<Map.Entry<SectionType, AbstractSection>> collectionSections = resume.getSections().entrySet();
            writeData(dos, collectionSections, entry -> {
                SectionType sectionType = entry.getKey();
                dos.writeUTF(sectionType.name());
                AbstractSection section = entry.getValue();
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dos.writeUTF(((TextSection) section).getData());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> list = ((ListSection) section).getData();
                        writeData(dos, list, dos::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Experience> experiences = ((ExperienceSection) section).getExperiences();
                        writeData(dos, experiences, experience -> {
                            dos.writeUTF(experience.getEmployerName());
                            writeIfExist(dos, experience.getEmployerSite());
                            List<Experience.Position> positions = experience.getPositions();
                            writeData(dos, positions, position -> {
                                dos.writeUTF(position.getStartDate().toString());
                                dos.writeUTF(position.getFinishDate().toString());
                                dos.writeUTF(position.getPosition());
                                writeIfExist(dos, position.getDescription());
                            });
                        });
                        break;
                    default:
                        break;
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readData(dis, () -> {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            });
            readData(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                resume.addSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }

    private AbstractSection readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                return new TextSection(dis.readUTF());
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return new ListSection(getListData(dis, dis::readUTF));
            case EXPERIENCE:
            case EDUCATION:
                int ExperienceCount = dis.readInt();
                return new ExperienceSection(getExperiences(dis, ExperienceCount));
            default:
                throw new IllegalStateException();
        }
    }

    private <T> List<T> getListData(DataInputStream dis, Reader<T> reader) throws IOException {
        int listCount = dis.readInt();
        List<T> data = new ArrayList<>(listCount);
        for (int i = 0; i < listCount; i++) {
            data.add(reader.readElement());
        }
        return data;
    }

    private List<Experience> getExperiences(DataInputStream dis, int experienceCount) throws IOException {
        List<Experience> experiences = new ArrayList<>(experienceCount);
        for (int j = 0; j < experienceCount; j++) {
            String employerName = dis.readUTF();
            String employerSite = readIfExist(dis);
            int positionsCount = dis.readInt();
            experiences.add(new Experience(employerName, employerSite,
                    getPositions(dis, positionsCount)));
        }
        return experiences;
    }

    private List<Experience.Position> getPositions(DataInputStream dis, int positionsCount) throws IOException {
        List<Experience.Position> positions = new ArrayList<>();
        for (int i = 0; i < positionsCount; i++) {
            LocalDate startDate = getLocalDate(dis);
            LocalDate finishDate = getLocalDate(dis);
            String position = dis.readUTF();
            String description = readIfExist(dis);
            positions.add(new Experience.Position(startDate, finishDate, position, description));
        }
        return positions;
    }

    private void writeIfExist(DataOutputStream dos, String string) throws IOException {
        String stringToWrite = (string != null) ? string : "";
        dos.writeUTF(stringToWrite);
    }

    private String readIfExist(DataInputStream dis) throws IOException {
        String content = dis.readUTF();
        return content.equals("") ? null : content;
    }

    private LocalDate getLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.parse(dis.readUTF());
    }

    private <T> void writeData(DataOutputStream dos, Collection<T> collection
            , Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            writer.writeElement(t);
        }
    }

    private interface Writer<T> {
        void writeElement(T t) throws IOException;
    }

    private interface Reader<T> {
        T readElement() throws IOException;
    }

    private void readData(DataInputStream dis, DoAction doAction) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            doAction.addData();
        }
    }

    private interface DoAction {
        void addData() throws IOException;
    }
}

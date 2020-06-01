package ru.topjava.basejava.storage.serializer;

import ru.topjava.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataStreamSerializer implements SerializationStrategy {

    @Override
    public void doWrite(Resume resume, OutputStream os) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(resume.getUuid());
            dos.writeUTF(resume.getFullName());
            dos.writeInt(resume.getContacts().size());
            for (Map.Entry<ContactType, String> entry : resume.getContacts().entrySet()) {
                dos.writeUTF(entry.getKey().toString());
                dos.writeUTF(entry.getValue());
            }
            dos.writeInt(resume.getSections().size());
            for (Map.Entry<SectionType, AbstractSection> entry : resume.getSections().entrySet()) {
                dos.writeUTF(entry.getKey().toString());
                AbstractSection section = entry.getValue();
                if (section instanceof TextSection) {
                    dos.writeUTF(((TextSection) section).getData());
                }
                if (section instanceof ListSection) {
                    List<String> list = ((ListSection) section).getData();
                    dos.writeInt(list.size());
                    for (String data : list) {
                        dos.writeUTF(data);
                    }
                }
                if (section instanceof ExperienceSection) {
                    List<Experience> experiences = ((ExperienceSection) section).getExperiences();
                    dos.writeInt(experiences.size());
                    for (Experience experience : experiences) {
                        dos.writeUTF(experience.getEmployerName());
                        dos.writeUTF(experience.getEmployerSite());
                        List<Experience.Position> positions = experience.getPositions();
                        dos.writeInt(positions.size());
                        for (Experience.Position position : positions) {
                            dos.writeUTF(position.getStartDate().toString());
                            dos.writeUTF(position.getFinishDate().toString());
                            dos.writeUTF(position.getPosition());
                            dos.writeUTF(position.getDescription());
                        }
                    }
                }
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
            int countContacts = dis.readInt();
            for (int i = 0; i < countContacts; i++) {
                ContactType contactType = ContactType.valueOf(dis.readUTF());
                String contact = dis.readUTF();
                resume.addContact(contactType, contact);
            }
            int countSections = dis.readInt();
            for (int i = 0; i < countSections; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                if (sectionType.equals(SectionType.OBJECTIVE) || sectionType.equals(SectionType.PERSONAL)) {
                    resume.addSection(sectionType, new TextSection(dis.readUTF()));
                }
                if (sectionType.equals(SectionType.ACHIEVEMENT) || sectionType.equals(SectionType.QUALIFICATIONS)) {
                    int listCount = dis.readInt();
                    resume.addSection(sectionType, new ListSection(getListData(dis, listCount)));
                }
                if (sectionType.equals(SectionType.EXPERIENCE) || sectionType.equals(SectionType.EDUCATION)) {
                    int ExperienceCount = dis.readInt();
                    resume.addSection(sectionType, new ExperienceSection(getExperiences(dis, ExperienceCount)));
                }
            }
        }
        return resume;
    }

    private List<String> getListData(DataInputStream dis, int listCount) throws IOException {
        List<String> data = new ArrayList<>();
        for (int j = 0; j < listCount; j++) {
            data.add(dis.readUTF());
        }
        return data;
    }

    private List<Experience> getExperiences(DataInputStream dis, int experienceCount) throws IOException {
        List<Experience> experiences = new ArrayList<>(experienceCount);
        for (int j = 0; j < experienceCount; j++) {
            String employerName = dis.readUTF();
            String employerSite = dis.readUTF();
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
            String description = dis.readUTF();
            positions.add(new Experience.Position(startDate, finishDate, position, description));
        }
        return positions;
    }

    private LocalDate getLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.parse(dis.readUTF());
    }
}

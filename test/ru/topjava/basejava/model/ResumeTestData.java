package ru.topjava.basejava.model;

import java.util.Map;

public class ResumeTestData {
    public static Resume CreateResume() {
        ResumeData data = new ResumeData();
        Resume resume = new Resume(data.getFullName());
        fillContacts(data, resume.getContacts());
        fillSection(data, resume.getSections());
        return resume;
    }

    private static void fillContacts(ResumeData data, Map<ContactType, String> contacts) {
        contacts.put(ContactType.PHONE, data.getPhone());
        contacts.put(ContactType.SKYPE, data.getSkype());
    }

    private static void fillSection(ResumeData data, Map<SectionType, AbstractSection> sections) {
        sections.put(SectionType.OBJECTIVE, new TextSection(data.getObjective()));
        sections.put(SectionType.PERSONAL, new TextSection(data.getPersonal()));
        sections.put(SectionType.ACHIEVEMENT, new ListSection(data.getAchievement()));
        sections.put(SectionType.QUALIFICATIONS, new ListSection(data.getQualification()));
        sections.put(SectionType.EXPERIENCE, new ExperienceSection(data.getExperiences()));
        sections.put(SectionType.EDUCATION, new ExperienceSection(data.getEducation()));
    }

    public static void printContacts(Map<ContactType, String> map) {
        for (Map.Entry<ContactType, String> entry : map.entrySet()) {
            System.out.println(entry.getKey().getTitle() + ": " + entry.getValue());
        }
    }

    public static void printSections(Map<SectionType, AbstractSection> map) {
        for (Map.Entry<SectionType, AbstractSection> entry : map.entrySet()) {
            System.out.println("\n" + entry.getKey().getTitle() + ": " + entry.getValue().toString());
        }
    }

    public static void main(String[] args) {
        Resume resume = CreateResume();
        System.out.println(resume.getFullName());
        printContacts(resume.getContacts());
        printSections(resume.getSections());
    }
}
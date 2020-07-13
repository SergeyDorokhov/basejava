package ru.topjava.basejava.model;

import java.util.Map;

public class ResumeTestData {
    public static Resume createResume(String uuid, String fullName) {
        Resume resume = new Resume(uuid, fullName);
        ResumeData data = new ResumeData();
/*        resume.addContact(ContactType.PHONE, data.getPhone());
        resume.addContact(ContactType.SKYPE, data.getSkype());
        resume.addSection(SectionType.OBJECTIVE, new TextSection(data.getObjective()));
        resume.addSection(SectionType.PERSONAL, new TextSection(data.getPersonal()));
        resume.addSection(SectionType.ACHIEVEMENT, new ListSection(data.getAchievement()));
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(data.getQualification()));*/
/*        resume.addSection(SectionType.EXPERIENCE, new ExperienceSection(data.getExperiences()));
        resume.addSection(SectionType.EDUCATION, new ExperienceSection(data.getEducation()));*/
        return resume;
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
        Resume resume = createResume("uuid6", "Кислин Григорий");
        ResumeData data = new ResumeData();
        resume.addContact(ContactType.PHONE, data.getPhone());
        resume.addContact(ContactType.SKYPE, data.getSkype());
        resume.addSection(SectionType.OBJECTIVE, new TextSection(data.getObjective()));
        resume.addSection(SectionType.PERSONAL, new TextSection(data.getPersonal()));
        resume.addSection(SectionType.ACHIEVEMENT, new ListSection(data.getAchievement()));
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(data.getQualification()));
        resume.addSection(SectionType.EXPERIENCE, new ExperienceSection(data.getExperiences()));
        resume.addSection(SectionType.EDUCATION, new ExperienceSection(data.getEducation()));
        System.out.println(resume.getFullName());
        printContacts(resume.getContacts());
        printSections(resume.getSections());
    }
}
package ru.topjava.basejava.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResumeTestData {
    public static void main(String[] args) {
        String phone = "+7(921) 855-0482";
        String skype = "grigory.kislin";
        String personal = "Аналитический склад ума, сильная логика, " +
                "креативность, инициативность. Пурист кода и архитектуры.";
        String objective = "Ведущий стажировок и корпоративного обучения " +
                "по Java Web и Enterprise технологиям";
        List<String> achievement = Arrays.asList("С 2013 года: разработка проектов \"Разработка Web приложения\","
                + "\"Java Enterprise\", \"Многомодульный maven.\n Многопоточность. XML (JAXB/StAX). "
                + "Веб сервисы (JAX-RS/SOAP). Удаленное взаимодействие (JMS/AKKA)\".\n"
                + " Организация онлайн стажировок и ведение проектов. Более 1000 выпускников.");
        Employer javaOnlineProjects = new Employer("Java Online Projects", "http://javaops.ru/",
                (LocalDate.of(2013, 10, 1)), LocalDate.now(), "Автор проекта",
                "Создание, организация и проведение Java онлайн проектов и стажировок.");
        List<Employer> employers = Arrays.asList(javaOnlineProjects);

        Resume resume = new Resume("Григорий Кислин");
        Map<ContactType, String> contacts = resume.getContacts();
        contacts.put(ContactType.PHONE, phone);
        contacts.put(ContactType.SKYPE, skype);

        Map<SectionType, Section> sections = resume.getSections();
        sections.put(SectionType.OBJECTIVE, new TextSection(objective));
        sections.put(SectionType.PERSONAL, new TextSection(personal));
        sections.put(SectionType.ACHIEVEMENT, new ListSection(achievement));
        sections.put(SectionType.EXPERIENCE, new EmployerSection(employers));

        System.out.println(resume.getFullName());
        printContacts(contacts);
        printSections(sections);
    }

    public static void printContacts(Map<ContactType, String> map) {
        for (Map.Entry<ContactType, String> entry : map.entrySet()) {
            System.out.println(entry.getKey().getTitle() + ": " + entry.getValue());
        }
    }

    public static void printSections(Map<SectionType, Section> map) {
        for (Map.Entry<SectionType, Section> entry : map.entrySet()) {
            System.out.println(entry.getKey().getTitle() + ": " + entry.getValue().toString());
        }
    }
}
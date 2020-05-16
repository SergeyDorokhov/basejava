package ru.topjava.basejava.model;

public class ResumeTestData {
    public static void main(String[] args) {
        String phone = "+7(921) 855-0482";
        String skype = "grigory.kislin";
        String email = "gkislin@yandex.ru";
        String linkedIn = "https://www.linkedin.com/in/gkislin";
        String gitHub = "https://github.com/gkislin";
        String stackoverflow = "https://stackoverflow.com/users/548473/grigory-kislin";
        String site = "http://gkislin.ru/";

        String personal = "Аналитический склад ума, сильная логика, " +
                "креативность, инициативность. Пурист кода и архитектуры.";
        String objective = "Ведущий стажировок и корпоративного обучения " +
                "по Java Web и Enterprise технологиям";

        Resume resume = new Resume("Григорий Кислин");
        Resume.Moderator moderator = new Resume.Moderator(resume);

        moderator.addPhone(phone);
        moderator.addSkype(skype);
        moderator.addEmail(email);
        moderator.addLinkedIn(linkedIn);
        moderator.addGitHub(gitHub);
        moderator.addStackoverflow(stackoverflow);
        moderator.addSite(site);

        moderator.addPersonal(personal);
        moderator.addObjective(objective);

        System.out.println(resume.getFullName());
        printContacts();
        printSections();

    }

    public static void printContacts() {
        for (ContactType contactType : ContactType.values()) {
            if (contactType.getData() != null) {
                System.out.println(contactType.getTitle() + ": " + contactType.getData());
            }

        }
    }

    public static void printSections() {
        for (SectionType sectionType : SectionType.values()) {
            if (sectionType.getData() != null) {
                System.out.println(sectionType.getTitle() + ": " + sectionType.getData());
            }

        }
    }
}
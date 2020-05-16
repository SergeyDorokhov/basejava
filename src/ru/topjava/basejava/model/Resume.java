package ru.topjava.basejava.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    // Unique identifier
    private final String uuid;
    private final String fullName;
    private final Contact contact;
    private final Section section;

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
        this.contact = this.new Contact();
        this.section = this.new Section();
    }


    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Resume resume = (Resume) object;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return uuid + " " + fullName;
    }

    @Override
    public int compareTo(Resume o) {
        return this.uuid.compareTo(o.getUuid());
    }

    public class Contact {
        private final ContactType PHONE;
        private final ContactType SKYPE;
        private final ContactType EMAIL;
        private final ContactType LINKEDIN;
        private final ContactType GITHUB;
        private final ContactType STACKOVERFLOW;
        private final ContactType SITE;

        public Contact() {
            this.PHONE = ContactType.PHONE;
            this.SKYPE = ContactType.SKYPE;
            this.EMAIL = ContactType.EMAIL;
            this.LINKEDIN = ContactType.LINKEDIN;
            this.GITHUB = ContactType.GITHUB;
            this.STACKOVERFLOW = ContactType.STACKOVERFLOW;
            this.SITE = ContactType.SITE;
        }

        public ContactType getPHONE() {
            return PHONE;
        }

        public ContactType getSKYPE() {
            return SKYPE;
        }

        public ContactType getEMAIL() {
            return EMAIL;
        }

        public ContactType getLINKEDIN() {
            return LINKEDIN;
        }

        public ContactType getGITHUB() {
            return GITHUB;
        }

        public ContactType getSTACKOVERFLOW() {
            return STACKOVERFLOW;
        }

        public ContactType getSITE() {
            return SITE;
        }
    }

    public class Section {
        private final SectionType PERSONAL;
        private final SectionType OBJECTIVE;
        private final SectionType ACHIEVEMENT;
        private final SectionType QUALIFICATIONS;
        private final SectionType EXPERIENCE;
        private final SectionType EDUCATION;

        public Section() {
            this.PERSONAL = SectionType.PERSONAL;
            this.OBJECTIVE = SectionType.OBJECTIVE;
            this.ACHIEVEMENT = SectionType.ACHIEVEMENT;
            this.QUALIFICATIONS = SectionType.QUALIFICATIONS;
            this.EXPERIENCE = SectionType.EXPERIENCE;
            this.EDUCATION = SectionType.EDUCATION;
        }

        public SectionType getPERSONAL() {
            return PERSONAL;
        }

        public SectionType getOBJECTIVE() {
            return OBJECTIVE;
        }

        public SectionType getACHIEVEMENT() {
            return ACHIEVEMENT;
        }

        public SectionType getQUALIFICATIONS() {
            return QUALIFICATIONS;
        }

        public SectionType getEXPERIENCE() {
            return EXPERIENCE;
        }

        public SectionType getEDUCATION() {
            return EDUCATION;
        }
    }

    public static class Moderator {
        private final Resume resume;

        public Moderator(Resume resume) {
            this.resume = resume;
        }

        public void addPhone(String phone) {
            resume.contact.getPHONE().setData(phone);

        }

        public void addSkype(String skype) {
            resume.contact.getSKYPE().setData(skype);
        }

        public void addEmail(String email) {
            resume.contact.getEMAIL().setData(email);
        }

        public void addLinkedIn(String linkedIn) {
            resume.contact.getLINKEDIN().setData(linkedIn);
        }

        public void addGitHub(String gitHub) {
            resume.contact.getGITHUB().setData(gitHub);
        }

        public void addStackoverflow(String stackoverflow) {
            resume.contact.getSTACKOVERFLOW().setData(stackoverflow);
        }

        public void addSite(String site) {
            resume.contact.getSITE().setData(site);
        }

        public void addPersonal(String personal) {
            resume.section.getPERSONAL().setContent(personal);
        }

        public void addObjective(String objective) {
            resume.section.getOBJECTIVE().setContent(objective);
        }
    }
}
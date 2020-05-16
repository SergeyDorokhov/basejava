package ru.topjava.basejava.model;

import java.time.LocalDate;

public class Employer {
    private final String name;
    private final String site;
    private final LocalDate startDate;
    private final LocalDate finishDate;
    private final String position;
    private final String description;

    public Employer(String name, String site, LocalDate startDate, LocalDate finishDate, String position, String description) {
        this.name = name;
        this.site = site;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.position = position;
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Employer employer = (Employer) object;

        if (!name.equals(employer.name)) return false;
        if (!site.equals(employer.site)) return false;
        if (!startDate.equals(employer.startDate)) return false;
        if (!finishDate.equals(employer.finishDate)) return false;
        if (!position.equals(employer.position)) return false;
        return description.equals(employer.description);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + site.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + finishDate.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Employer{" +
                "name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

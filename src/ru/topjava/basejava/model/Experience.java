package ru.topjava.basejava.model;

import java.time.LocalDate;

public class Experience {
    private final String employerName;
    private final String employerSite;
    private final LocalDate startDate;
    private final LocalDate finishDate;
    private final String position;
    private final String description;

    public Experience(String employerName, String employerSite, LocalDate startDate,
                      LocalDate finishDate, String position, String description) {
        this.employerName = employerName;
        this.employerSite = employerSite;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.position = position;
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Experience experience = (Experience) object;

        if (!employerName.equals(experience.employerName)) return false;
        if (!employerSite.equals(experience.employerSite)) return false;
        if (!startDate.equals(experience.startDate)) return false;
        if (!finishDate.equals(experience.finishDate)) return false;
        if (!position.equals(experience.position)) return false;
        return description.equals(experience.description);
    }

    @Override
    public int hashCode() {
        int result = employerName.hashCode();
        result = 31 * result + employerSite.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + finishDate.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Experience{" +
                "name='" + employerName + '\'' +
                ", site='" + employerSite + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

package ru.topjava.basejava.model;

import ru.topjava.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Experience implements Serializable {
    private static final long serialVersionUID = 1L;
    private String employerName;
    private String employerSite;
    private List<Position> positions;

    public Experience() {
    }

    public Experience(String employerName, String employerSite, Position... positions) {
        Objects.requireNonNull(employerName);
        Objects.requireNonNull(employerSite);
        Objects.requireNonNull(positions);
        this.employerName = employerName;
        this.employerSite = employerSite;
        this.positions = Arrays.asList(positions);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Experience that = (Experience) object;
        return employerName.equals(that.employerName) &&
                employerSite.equals(that.employerSite) &&
                positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employerName, employerSite, positions);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(employerName + " ").append(employerSite);
        for (Position position : positions) {
            builder.append(position);
        }
        return builder.toString();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate finishDate;
        private String position;
        private String description;

        public Position() {
        }

        public Position(LocalDate startDate, LocalDate finishDate, String position, String description) {
            Objects.requireNonNull(startDate);
            Objects.requireNonNull(finishDate);
            Objects.requireNonNull(description);
            this.startDate = startDate;
            this.finishDate = finishDate;
            this.position = position;
            this.description = description;
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            Position position1 = (Position) object;
            return startDate.equals(position1.startDate) &&
                    finishDate.equals(position1.finishDate) &&
                    position.equals(position1.position) &&
                    description.equals(position1.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(startDate, finishDate, position, description);
        }

        @Override
        public String toString() {
            return "\n" + startDate + " " + finishDate + "\n"
                    + position + " " + description;
        }
    }
}

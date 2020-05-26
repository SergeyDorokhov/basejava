package ru.topjava.basejava.model;

import java.util.List;

public class ExperienceSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private final List<Experience> experiences;

    public ExperienceSection(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ExperienceSection that = (ExperienceSection) object;
        return experiences.equals(that.experiences);
    }

    @Override
    public int hashCode() {
        return experiences.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Experience experience : experiences) {
            builder.append(experience);
        }
        return builder.toString();
    }
}

package ru.topjava.basejava.model;

import java.util.List;

public class EmployerSection extends Section {
    private final List<Employer> employers;

    public EmployerSection(List<Employer> employers) {
        this.employers = employers;
    }

    public List<Employer> getEmployers() {
        return employers;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        EmployerSection that = (EmployerSection) object;

        return employers.equals(that.employers);
    }

    @Override
    public int hashCode() {
        return employers.hashCode();
    }

    @Override
    public String toString() {
        return employers.toString();
    }
}

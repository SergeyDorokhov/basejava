package ru.topjava.basejava.model;

import java.util.List;

public class ListSection extends AbstractSection {
    private final List<String> data;

    public ListSection(List<String> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ListSection that = (ListSection) object;

        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return data.toString();
    }
}

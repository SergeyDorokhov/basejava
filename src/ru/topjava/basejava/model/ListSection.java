package ru.topjava.basejava.model;

import java.util.Arrays;
import java.util.List;

public class ListSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private List<String> data;

    public ListSection() {
    }

    public ListSection(String... strings) {
        this(Arrays.asList(strings));
    }

    public ListSection(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
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
        StringBuilder content = new StringBuilder();
        for (String text : data) {
            content.append(text);
        }
        return content.toString();
    }
}

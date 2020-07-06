package ru.topjava.basejava.model;

public class TextSection extends AbstractSection {
    private static final long serialVersionUID = 1L;
    private String data;

    public TextSection(String data) {
        this.data = data;
    }

    public TextSection() {
    }

    public String getData() {
        return data;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TextSection that = (TextSection) object;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String toString() {
        return data;
    }
}

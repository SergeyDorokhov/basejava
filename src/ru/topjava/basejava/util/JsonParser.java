package ru.topjava.basejava.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.topjava.basejava.model.AbstractSection;
import ru.topjava.basejava.model.Resume;

import java.io.Reader;
import java.io.Writer;

public class JsonParser {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(AbstractSection.class, new JsonSectionAdapter<Resume>())
            .create();

    public static <T> void write(Resume resume, Writer writer) {
        gson.toJson(resume, writer);
    }

    public static <T> T read(Reader reader, Class<T> clazz) {
        return gson.fromJson(reader, clazz);
    }
}

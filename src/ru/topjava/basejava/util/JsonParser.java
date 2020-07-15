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

    public static <T> T read(Reader reader, Class<T> clazz) {
        return gson.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer) {
        gson.toJson(object, writer);
    }

    public static <T> T read(String content, Class<T> clazz) {
        return gson.fromJson(content, clazz);
    }

    public static <T> String write(T object, Class<T> clazz) {
        return gson.toJson(object, clazz);
    }
}

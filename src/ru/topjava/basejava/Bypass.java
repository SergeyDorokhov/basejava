package ru.topjava.basejava;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Bypass {
    public static void findAllFiles(String offset, String path) throws IOException {
        File startFolder = new File(path);
        File[] files = startFolder.listFiles();
        for (File file : Objects.requireNonNull(files)) {
            if (file.isFile()) {
                printFileName(offset + "file: ", file);
            } else {
                printFileName(offset + "dir: ", file);
                findAllFiles(offset + " ", file.getPath());
            }
        }
    }

    private static void printFileName(String offset, File file) throws IOException {
        System.out.println(offset + file.getName());
    }

    public static void main(String[] args) {
        String startFolder = "C:\\Projects\\java\\basejava\\src\\ru\\topjava\\basejava";
        try {
            findAllFiles("", startFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package ru.topjava.basejava;

import java.io.File;
import java.io.IOException;

public class Bypass {
    public static void findAllFiles(String path) throws IOException {
        File startFolder = new File(path);
        File[] files = startFolder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                printFileName(file);
            } else {
                findAllFiles(file.getPath());
            }
        }
    }

    private static void printFileName(File file) throws IOException {
        System.out.println(file.getCanonicalPath());
    }

    public static void main(String[] args) {
        String startFolder = "C:\\Projects\\java\\basejava";
        try {
            findAllFiles(startFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
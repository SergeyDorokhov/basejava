package ru.topjava.basejava.web;

import ru.topjava.basejava.Config;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.SqlStorage;
import ru.topjava.basejava.storage.Storage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private final Storage storage;

    public ResumeServlet() {
        this.storage = new SqlStorage(Config.get().getProperties().getProperty("db.url")
                , Config.get().getProperties().getProperty("db.user")
                , Config.get().getProperties().getProperty("db.password"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setParams(request, response);
        fillStorage();
        printHeader(response);
        printBody(response);
        printFooter(response);
    }

    private void setParams(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
    }

    private void fillStorage() {
        storage.clear();
        storage.save(new Resume("uuid1", "Ivan Ivanov"));
        storage.save(new Resume("uuid2", "Petr Petrov"));
    }

    private void printHeader(HttpServletResponse response) throws IOException {
        response.getWriter().write("<head>" +
                "<link rel=\"stylesheet\" href=\"css/style.css\">" +
                "</head>");
        response.getWriter().write("<table style=\"width:100%\">" +
                "<tr>" +
                "<th>UUID</th>" +
                "<th>Full Name</th>" +
                "</tr>");
    }

    private void printBody(HttpServletResponse response) throws IOException {
        List<Resume> resumes = storage.getAllSorted();
        for (Resume resume : resumes) {
            response.getWriter().write("<tr>" +
                    "<th>" + resume.getUuid() + "</th>" +
                    "<th>" + resume.getFullName() + "</th>" +
                    "</tr>");
        }
    }

    private void printFooter(HttpServletResponse response) throws IOException {
        response.getWriter().write("</table> ");
    }
}

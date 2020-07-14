package ru.topjava.basejava.web;

import ru.topjava.basejava.Config;
import ru.topjava.basejava.model.*;
import ru.topjava.basejava.storage.SqlStorage;
import ru.topjava.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() {
        this.storage = new SqlStorage(Config.get().getProperties().getProperty("db.url")
                , Config.get().getProperties().getProperty("db.user")
                , Config.get().getProperties().getProperty("db.password"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume = storage.get(uuid);
        resume.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType sectionType : SectionType.values()) {
            String value = request.getParameter(sectionType.name());
            if (value != null && value.trim().length() != 0) {
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(sectionType, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> list = Arrays.asList(value.split("\n"));
                        resume.addSection(sectionType, new ListSection(list));
                        break;
                    default:
                        break;
                }
            } else {
                resume.getSections().remove(sectionType);
            }
        }
        storage.update(resume);
        response.sendRedirect("resume");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        String fullName = request.getParameter("fullName");
        Resume resume;
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        switch (action) {
            case "add":
                resume = new Resume(fullName);
                for (ContactType contactType : ContactType.values()) {
                    resume.addContact(contactType, "");
                }
                for (SectionType sectionType : SectionType.values()) {
                    switch (sectionType) {
                        case PERSONAL:
                        case OBJECTIVE:
                            resume.addSection(sectionType, new TextSection(""));
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            resume.addSection(sectionType, new ListSection(""));
                            break;
                        case EDUCATION:
                        case EXPERIENCE:
                            resume.addSection(sectionType, new ExperienceSection(new Experience(""
                                    , "", new Experience.Position())));
                    }
                }
                storage.save(resume);
                break;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                resume = storage.get(uuid);
                break;
            case "edit":
                resume = storage.get(uuid);
                for (SectionType sectionType : SectionType.values()) {
                    AbstractSection section = resume.getSections().get(sectionType);
                    if (section == null) {
                        switch (sectionType) {
                            case PERSONAL:
                            case OBJECTIVE:
                                section = new TextSection("");
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                section = new ListSection("");
                                break;
                            case EDUCATION:
                            case EXPERIENCE:
                                section = new ExperienceSection(new Experience(""
                                        , "", new Experience.Position()));
                        }
                    }
                    resume.addSection(sectionType, section);
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);
    }
}
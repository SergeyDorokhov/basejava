package ru.topjava.basejava.web;

import ru.topjava.basejava.Config;
import ru.topjava.basejava.model.*;
import ru.topjava.basejava.storage.SqlStorage;
import ru.topjava.basejava.storage.Storage;
import ru.topjava.basejava.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;
    //private Resume emptyResume = new Resume();

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
        final boolean isNewResume = (uuid == null || uuid.length() == 0);
        Resume resume;
        if (isNewResume) {
            resume = new Resume(fullName);
        } else {
            resume = storage.get(uuid);
            resume.setFullName(fullName);
        }
        for (ContactType type : ContactType.values()) {
            String data = request.getParameter(type.name());
            if (data != null && data.trim().length() != 0) {
                resume.addContact(type, data);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType sectionType : SectionType.values()) {
            String content = request.getParameter(sectionType.name());
            String[] values = request.getParameterValues(sectionType.name());
            if (content != null && content.trim().length() != 0) {
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(sectionType, new TextSection(content));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> list = Arrays.asList(content.split("\n"));
                        resume.addSection(sectionType, new ListSection(list));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Experience> experiences = new ArrayList<>();
                        String[] organisations = request.getParameterValues(sectionType.name());
                        String[] organisationSites = request.getParameterValues(sectionType.name() + "url");
                        for (int i = 0; i < organisations.length; i++) {
                            List<Experience.Position> positions = new ArrayList<>();
                            String prefix = sectionType.name() + i;
                            String[] startDates = request.getParameterValues(prefix + "startDate");
                            String[] finishDates = request.getParameterValues(prefix + "finishDate");
                            String[] titles = request.getParameterValues(prefix + "title");
                            String[] descriptions = request.getParameterValues(prefix + "description");
                            for (int j = 0; j < titles.length; j++) {
                                if (titles[j] != null) {
                                    positions.add(new Experience.Position((DateUtil.parse(startDates[j]))
                                            , DateUtil.parse(finishDates[j]), titles[j], descriptions[j]));
                                }
                            }
                            experiences.add(new Experience(organisations[i], organisationSites[i], positions));
                        }
                        resume.addSection(sectionType, new ExperienceSection(experiences));
                        break;
                    default:
                        break;
                }
            } else if (values.length < 2) {
                resume.getSections().remove(sectionType);
            }
        }
        if (isNewResume) {
            storage.save(resume);
        } else {
            storage.update(resume);
        }
        response.sendRedirect("resume");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        Resume resume;
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        switch (action) {
            case "add":
               resume = getEmptyResume();
/*                resume = new Resume();
                for (ContactType contactType : ContactType.values()) {
                    resume.addContact(contactType, "");
                }
                for (SectionType sectionType : SectionType.values()) {
                    AbstractSection section = getEmptySections(sectionType);
                    resume.addSection(sectionType, section);
                }*/
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
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = resume.getSections().get(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {
                                section = new TextSection("");
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {
                                section = new ListSection("");
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            ExperienceSection orgSection = (ExperienceSection) section;
                            List<Experience> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(new Experience(""
                                    , "", new Experience.Position()));
                            if (orgSection != null) {
                                for (Experience org : orgSection.getExperiences()) {
                                    List<Experience.Position> emptyFirstPositions = new ArrayList<>();
                                    //emptyFirstPositions.add( new Experience.Position());
                                    emptyFirstPositions.addAll(org.getPositions());
                                    emptyFirstOrganizations.add(new Experience(org.getEmployerName()
                                            , org.getEmployerSite(), emptyFirstPositions));
                                }
                            }
                            section = new ExperienceSection(emptyFirstOrganizations);
                            break;
                    }
                    resume.addSection(type, section);
                }
                break;
              /*  resume = storage.get(uuid);
                for (SectionType sectionType : SectionType.values()) {
                    AbstractSection section = resume.getSections().get(sectionType);
                    if (section == null) {
                        section = getEmptySections(sectionType);
                    } else {
                        switch (sectionType) {
                            case EXPERIENCE:
                            case EDUCATION:
                                ExperienceSection orgSection = (ExperienceSection) section;
                                List<Experience> emptyFirstOrganizations = new ArrayList<>();
                                emptyFirstOrganizations.add(new Experience(""
                                        , "", new Experience.Position()));
                                if (orgSection != null) {
                                    for (Experience org : orgSection.getExperiences()) {
                                        List<Experience.Position> emptyFirstPositions = new ArrayList<>();
                                        emptyFirstPositions.add(new Experience.Position());
                                        emptyFirstPositions.addAll(org.getPositions());
                                        emptyFirstOrganizations.add(new Experience(org.getEmployerName(),
                                                org.getEmployerSite(), emptyFirstPositions));
                                    }
                                }
                                section = new ExperienceSection(emptyFirstOrganizations);
                                break;
                            default:
                                break;
                        }

                    }
                    resume.addSection(sectionType, section);
                }

                break;*/
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
                .forward(request, response);
    }

    private Resume getEmptyResume() {
        Resume resume = new Resume();
        resume.addSection(SectionType.PERSONAL, new TextSection(""));
        resume.addSection(SectionType.OBJECTIVE, new TextSection(""));
        resume.addSection(SectionType.ACHIEVEMENT, new ListSection(""));
        resume.addSection(SectionType.QUALIFICATIONS, new ListSection(""));
        resume.addSection(SectionType.EDUCATION, new ExperienceSection(new Experience(""
                , "", new Experience.Position())));
        resume.addSection(SectionType.EXPERIENCE, new ExperienceSection(new Experience(""
                , "", new Experience.Position())));
        return resume;
    }

/*    private AbstractSection getEmptySections(SectionType sectionType) {
        AbstractSection section;
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
                break;
            default:
                throw new IllegalArgumentException("SectionType " + sectionType + " is illegal");
        }
        return section;
    }*/
}
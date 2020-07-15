<%@ page import="ru.topjava.basejava.model.ContactType" %>
<%@ page import="ru.topjava.basejava.model.ExperienceSection" %>
<%@ page import="ru.topjava.basejava.model.ListSection" %>
<%@ page import="ru.topjava.basejava.model.SectionType" %>
<%@ page import="ru.topjava.basejava.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.topjava.basejava.model.Resume" scope="request"/>

    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <h2>Имя:</h2>
        <dl>
            <input type="text" name="fullName" size=55 value="${resume.fullName}">
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.contacts.get(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <c:forEach var="sectionType" items="<%=SectionType.values()%>">
            <c:set var="section" value="${resume.sections.get(sectionType)}"/>
            <jsp:useBean id="section" type="ru.topjava.basejava.model.AbstractSection"/>
            <h3>${sectionType.title}</h3>
            <c:choose>
                <c:when test="${sectionType=='OBJECTIVE' || sectionType=='PERSONAL'}">
                    <input type='text' name='${sectionType}' size=100 value='<%=section%>'>
                </c:when>
                <c:when test="${sectionType=='QUALIFICATIONS' || sectionType=='ACHIEVEMENT'}">
                    <textarea name='${sectionType}' cols=100
                              rows=4><%=String.join("\n", ((ListSection) section).getData())%></textarea>
                </c:when>
                <c:when test="${sectionType=='EDUCATION' || sectionType=='EXPERIENCE'}">
                    <c:forEach var="experience" items="<%=((ExperienceSection) section).getExperiences()%>"
                    >
                        <p>Организация:</p>
                        <p><input type="text" name='${sectionType}' size=100 value="${experience.employerName}"></p>
                        <p>Сайт:</p>
                        <p><input type="text" name='${sectionType}url' size=100 value="${experience.employerSite}"></p>
                        <p>Период:</p>
                        <c:forEach var="position" items="${experience.positions}">
                            <jsp:useBean id="position" type="ru.topjava.basejava.model.Experience.Position"/>
                            <p>с:
                                <input type="text" name="${sectionType}startDate" size=10
                                       value="<%=(DateUtil.format(position.getStartDate()))%>">
                                по:
                                <input type="text" name="${sectionType}finishDate" size=10
                                       value="<%=(DateUtil.format(position.getFinishDate()))%>">
                            </p>
                            <p>Должность:</p>
                            <input type="text" name='${sectionType}title' size=100 value="${position.position}">
                            <p>Должностные обязанности:</p>
                            <textarea name="${sectionType}$description" rows=4
                                      cols=100>${position.description}</textarea>

                        </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>


            </c:choose>
        </c:forEach>
        <p>
            <button type="submit">Сохранить</button>
            <button onclick="window.history.back()">Отменить</button>
        </p>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
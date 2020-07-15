<%@ page import="ru.topjava.basejava.model.ExperienceSection" %>
<%@ page import="ru.topjava.basejava.model.ListSection" %>
<%@ page import="ru.topjava.basejava.model.TextSection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" scope="request" type="ru.topjava.basejava.model.Resume"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>

    <c:forEach var="contactEntry" items="${resume.contacts}">
        <jsp:useBean id="contactEntry"
                     type="java.util.Map.Entry<ru.topjava.basejava.model.ContactType, java.lang.String>"/>
        <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
    </c:forEach>
    <c:forEach var="sectionEntry" items="${resume.sections}">
        <jsp:useBean id="sectionEntry"
                     type="java.util.Map.Entry<ru.topjava.basejava.model.SectionType
                         , ru.topjava.basejava.model.AbstractSection>"/>

        <c:set var="sectionType" value="${sectionEntry.key}"/>
        <h3>
            <%=sectionEntry.getKey().getTitle()%>
        </h3>
        <c:set var="sectionValue" value="${sectionEntry.value}"/>
        <jsp:useBean id="sectionValue" type="ru.topjava.basejava.model.AbstractSection"/>
        <c:choose>
            <c:when test="${sectionType=='OBJECTIVE' || sectionType=='PERSONAL'}">
                <%=((TextSection) sectionValue).getData()%>
            </c:when>
            <c:when test="${sectionType=='QUALIFICATIONS' || sectionType=='ACHIEVEMENT'}">
                <c:forEach var="item" items="<%=((ListSection) sectionValue).getData()%>">
                    <li>${item}</li>
                </c:forEach>
            </c:when>
            <c:when test="${sectionType=='EXPERIENCE' || sectionType=='EDUCATION'}">
                <c:forEach var="experience" items="<%=((ExperienceSection) sectionValue).getExperiences()%>">

                    <c:forEach var="position" items="${experience.positions}">
                        <jsp:useBean id="position" type="ru.topjava.basejava.model.Experience.Position"/>
                        <div style="margin-left: 15px">
                            <h4><li>${experience.employerName}</li></h4>
                            <div style="margin-left: 15px">
                                <p>Период с: ${position.startDate} по: ${position.finishDate}</p>
                                <strong>Должность: </strong>${position.position}<br>
                                <strong>Обязанности: </strong>${position.description}
                            </div>
                        </div>
                    </c:forEach>

                </c:forEach>

            </c:when>
        </c:choose>

    </c:forEach>

</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

<%@ page import="ru.topjava.basejava.model.ContactType" %>
<%@ page import="ru.topjava.basejava.model.ListSection" %>
<%@ page import="ru.topjava.basejava.model.SectionType" %>
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
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>

                <dd><input type="text" name="${type.name()}" size=30 value="${resume.contacts.get(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="sectionType" items="<%=SectionType.values()%>">
        <%--    <jsp:useBean id="sectionType" type="ru.topjava.basejava.model.SectionType"/>
            <c:set var="sectionValue" value="${resume.getSections().get(sectionType)}"/>--%>
         <%--   <jsp:useBean id="sectionValue" type="ru.topjava.basejava.model.AbstractSection"/>--%>
            <dl>
                <dt>${sectionType.title}</dt>

                <c:choose>

                    <c:when test="${sectionType=='OBJECTIVE' || sectionType=='PERSONAL'}">
                        <c:set var="value" value="${resume.sections.get(sectionType)}"/>
                        <jsp:useBean id="value" type="ru.topjava.basejava.model.AbstractSection"/>
                        <textarea name='${sectionType}' cols=100
                                  rows=4><%=String.join("\n", ((TextSection) value).getData())%></textarea>

                    </c:when>
                    <c:when test="${sectionType=='QUALIFICATIONS' || sectionType=='ACHIEVEMENT'}">
                        <c:set var="sectionValue" value="${resume.sections.get(sectionType)}"/>
                        <jsp:useBean id="sectionValue" type="ru.topjava.basejava.model.AbstractSection"/>
                        <textarea name='${sectionType}' cols=100
                                  rows=4><%=String.join("\n", ((ListSection) sectionValue).getData())%></textarea>
                    </c:when>
                </c:choose>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

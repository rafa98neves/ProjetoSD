<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>DropMusic</title>
</head>
<body>
<h2><a href="<s:url action="dropmusic" />"><span style="color:darkblue">DROPMUSIC</span></a></h2>
<br>
    <c:forEach items="${session.noti}" var="item">
        .${item}
    </c:forEach>
    <a href="dropmusic.jsp" class="button">Back</a>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>DropMusic</title>
</head>
<body>
    <s:form method="GET" action="AuthRegist">
        <s:text name="Username:" />
        <s:textfield name="username" />
        <s:text name="Password:" />
        <s:password name="password" />
        <s:submit />
    </s:form>
</body>
</html>
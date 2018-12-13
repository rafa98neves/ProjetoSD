<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>DropMusic</title>
</head>
<body>
    <h2>REGISTAR</h2>
    <br>
    <s:form method="post" action="AuthRegist">
        <li><s:text name="Username:" />
        <s:textfield name="username" />
        <li><s:text name="Password:" />
        <s:password name="password" />
        <br><s:submit type="button">
            <s:text name="Fazer registo"></s:text>
        </s:submit>
    </s:form>

    <p><a href="login.jsp"><span style="color:black">Fazer Login</span></a></p>
</body>
</html>
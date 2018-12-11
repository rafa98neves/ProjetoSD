<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>DropMusic</title>
</head>
<body>
    <p>LOGIN</p>
    <br>
    <s:form method="post" action="AuthLogin">
        <li><s:text name="Username:" />
        <s:textfield name="username" />
        <li><s:text name="Password:" />
        <s:password name="password" />
        <br><s:submit type="button">
            <s:text name="Fazer Login"></s:text>
        </s:submit>
    </s:form>
    <p><a href="registo.jsp"><span style="color:black">Registar</span></a></p>
</body>
</html>

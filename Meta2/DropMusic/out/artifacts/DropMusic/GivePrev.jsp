<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>DropMusic</title>
</head>
<body>
    <h2><a href="<s:url action="dropmusic" />"><span style="color:darkblue">DROPMUSIC</span></a></h2>

    <s:form method="post" action="GivePermission">
    <li><s:text name="User que pretende dar permissões:" />
            <s:textfield name="username" />
    <br><s:submit type="button">
        <s:text name="Dar permissões"></s:text>
    </s:submit>
    </s:form>
</body>
</html>

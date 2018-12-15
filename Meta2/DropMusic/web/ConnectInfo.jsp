<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
<head>
    <title>DropMusic</title>
</head>
<body>
<h2><a href="<s:url action="dropmusic" />"><span style="color:darkblue">DROPMUSIC</span></a></h2>
<br>
    <p>Precisa primeiro se autenticar na dropbox!</p>
    <a href="<s:url action="LogInDropBox" />">Autenticar</a>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>DropMusic</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<div title="header">
    <p><a href="<s:url action="dropmusic" />"><span style="color:darkblue">DROPMUSIC</span></a></p>
</div>
<div title="main">

    <s:form method="post" action="Criar">
        Nome: <s:textfield name="inputObject.nome" label="MusicName"/>
        <select name='sort'>
            <option value="musica">Música</option>
            <option value="album">Álbum</option>
            <option value="artista">Artista</option>
        </select>
        <s:combobox list=""
        <s:submit type="button">
            <s:text name="Criar"></s:text>
        </s:submit>
    </s:form>
</div>
</body>
</html>

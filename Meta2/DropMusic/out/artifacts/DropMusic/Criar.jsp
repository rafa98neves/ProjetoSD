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
    <h2><a href="<s:url action="dropmusic" />"><span style="color:darkblue">DROPMUSIC</span></a></h2>
</div>
<div title="main">

    <p>O que pretende criar: </p>
    <form action="Criar2.jsp" method="post">
        <select id="info1" name="info1">
            <option value="musica">Música</option>
            <option value="album">Álbum</option>
            <option value="artista">Artista</option>
        </select>
        <input type="submit" value="Select"/>
    </form>

</div>
</body>
</html>

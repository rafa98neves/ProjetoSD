<%--
  Created by IntelliJ IDEA.
  User: santa
  Date: 06/12/2018
  Time: 15:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DropMusic</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
    <s:url action="MusicCreat" var="MusicCreatUrl" />
    <s:url action="AlbumCreat" var="AlbumCreatUrl" />
    <s:url action="ArtistCreat" var="ArtistCreatUrl" />
    <s:url action="GenreCreat" var="GenreCreatUrl" />

    O que queres criar?<br/>
    <s:a href="%{carSearchUrl}" >Musica</s:a> <br/>
    <s:a href="%{laptopSearchUrl}" >Album </s:a>  <br/>
    <s:a href="%{shoesSearchUrl}" >Artista</s:a> <br/>
    <s:a href="%{shoesSearchUrl}" >Genero</s:a> <br/>
</body>
</html>

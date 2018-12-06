<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DropMusic</title>
</head>
<body>
<p><a href="<s:url action="dropmusic" />">DROPMUSIC</a></p>
	<li><p><a href="<s:url action="ToPesquisa" />">Pesquisar</a></p>
	<li><p><a href="<s:url action="ToPlaylist" />">Playlist</a></p>
	<li><p><a href="<s:url action="ToShare" />">Partilhar Música</a></p>
	<li><p><a href="<s:url action="ToDownload" />">Download</a></p>
	<li><p><a href="<s:url action="ToUpload" />">Upload</a></p>
	<%--<br><p><a href="<s:url action="ToCriar" />">DROPMUSIC</a></p>--%>
	<%--<br><p><a href="<s:url action="ToPrev" />">DROPMUSIC</a></p>--%>
</body>
</html>
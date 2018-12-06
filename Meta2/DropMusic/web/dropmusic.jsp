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
<div title="header">
	<p><a href="<s:url action="dropmusic" />"><span style="color:darkblue">DROPMUSIC</span></a></p>
	<s:textfield name="Pesquisa" />
	<s:submit type="button">
		<s:text name="Procurar"></s:text>
	</s:submit>
</div>

<div title="body">
	<li><p><a href="<s:url action="ToPlaylist" />"><span style="color:black">Playlist</span></a></p>
	<li><p><a href="<s:url action="ToShare" />"><span style="color:black">Partilhar Música</span></a></p>
	<li><p><a href="<s:url action="ToDownload" />"><span style="color:black">Download</span></a></p>
	<li><p><a href="<s:url action="ToUpload" />"><span style="color:black">Upload</span></a></p>
	<%--<li><p><a href="<s:url action="ToCriar" />"><span style="color:black">Criar</span></a></p>--%>
	<%--<li><p><a href="<s:url action="ToPrev" />"><span style="color:black">Dar Previlegios</span></a></p></p>--%>
	<li><p><a href="<s:url action="LogOut" />"><span style="color:black">Log out</span></a></p>
</div>
</body>
</html>
<%@ page import="model.interfaces.HeyBean" %>
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
	<h2><a href="<s:url action="dropmusic" />"><span style="color:darkblue">DROPMUSIC</span></a></h2>
    <s:form method="GET" action="Pesquisar">
		<input type='hidden' name='inputObject.flag' value='pesquisar'/>
		<s:textfield name="inputObject.searching" />
			<select id="info1" name="inputObject.tipo">
				<option value="musica">Música</option>
				<option value="album">Álbum</option>
				<option value="artista">Artista</option>
			</select>

        <s:submit type="button">
            <s:text name="Procurar"></s:text>
        </s:submit>
    </s:form>
</div>

<div title="main">
	<li><p><a href="Playlist.jsp"><span style="color:black">Playlists</span></a></p>
	<c:choose>
		<c:when test="${session.InDrop == false}">
			<li><p><a onclick="return alert('Tem que primero fazer log in na DropBox')">Partilhar Música</a></p>
			<li><p><a onclick="return alert('Tem que primero fazer log in na DropBox')">Download</a></p>
			<li><p><a onclick="return alert('Tem que primero fazer log in na DropBox')">Upload</a></p>
		</c:when>
		<c:otherwise>
			<li><p><a href="Partilhar.jsp"><span style="color:black">Partilhar Música</span></a></p>
			<li><p><a href="Download.jsp"><span style="color:black">Download</span></a></p>
			<li><p><a href="Upload.jsp"><span style="color:black">Upload</span></a></p>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${session.editor == true}">
			<li><p><a href="Criar.jsp"><span style="color:black">Criar</span></a></p>
			<li><p><a href="GivePrev.jsp"><span style="color:black">Dar Previlegios</span></a></p>
		</c:when>
	</c:choose>
	<li><p><a href="index.jsp"><span style="color:black">Log out</span></a></p>
	<c:choose>
	<c:when test="${session.InDrop == false}">
	<s:form action="LogInDropBox">
		<s:submit  value="Log in Dropbox" type="button"/>
	</s:form>
	</c:when>
	<c:otherwise>
	<s:form action="LogOutDropBox">
		<s:submit  value="Log out Dropbox" type="button"/>
	</s:form>
	</c:otherwise>
	</c:choose>
</div>
</body>
</html>
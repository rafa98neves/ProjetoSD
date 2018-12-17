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

	<script type="text/javascript">
        var websocket = null;
        window.onload = function() {
            connect('ws://' + window.location.host + '/ws');
        }

        function connect(host) {
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                console.log('Get a real browser which supports WebSocket.');
                return;
            }

            websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
            websocket.send("# " + "${session.username}");
            document.getElementById('Sendto').onkeydown = function(key) {
                if (key.keyCode == 13 && document.getElementById('Sendto').value != '') {
                    doSend();
                }
            };
        }

        function onMessage(message) {
            if(message.data.localeCompare("Parabéns, foi promovido a editor") === 1){
                <%--<s:set var="${session.editor}" value="true" />--%>
			}
			alert(message.data);
        }

        function doSend() {
            var alvo = document.getElementById('Sendto').value;
            if (alvo != '')
                websocket.send(alvo + " : Parabéns, foi promovido a editor!");

            document.getElementById('Sendto').value = '';
        }

        function onClose(event) {
            console.log('WebSocket closed.');
            document.getElementById('Sendto').onkeydown = null;
        }

        function onError(event) {
            console.log('WebSocket error (' + event.data + ').');
            document.getElementById('Sendto').onkeydown = null;
        }

	</script>
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
			<li><p><a href="<s:url action="Download" />"><span style="color:black">Download</span></a></p>
			<li><p><a href="Upload.jsp"><span style="color:black">Upload</span></a></p>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${session.editor == true}">
			<li><p><a href="Criar.jsp"><span style="color:black">Criar</span></a></p>
			Dar permissões: <p><input type="text" placeholder="nome do utilizador" id="Sendto"></p>
		</c:when>
	</c:choose>
	<li><p><a href="Notifications.jsp"><span style="color:black">Notificações[${session.n_notificacoes}]</span></a></p>
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
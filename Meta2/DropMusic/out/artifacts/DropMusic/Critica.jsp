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
        <s:textfield name="inputObject.searching"/>
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

   <h3>Critica</h3>

    <s:form method="post" action="DoCritic">
        Pontuação ao album:
        <select id="p" name="pontuacao">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
        </select>
        <br></br>
        <s:textarea name="critica"/>
        <s:submit type="button">
            <s:text name="Criticar"></s:text>
        </s:submit>
    </s:form>
</div>
</body>
</html>

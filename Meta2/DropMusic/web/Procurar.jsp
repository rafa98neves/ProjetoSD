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

    <c:choose>
        <c:when test="${results == null}">
            <br>
            Não foram encontrados resultados para a pesquisa!
        </c:when>
        <c:when test="${results.isEmpty()}">
            <br>
            Não foram encontrados resultados para a pesquisa!
        </c:when>
        <c:otherwise>
            <br />
            <br>
            <br>
            Resultado da pesquisa:
            <br>
            <c:forEach items="${results}" var="item">
                    <s:url var="alvo" action="GetDetails">
                        <s:param name="inputObject.alvo">${item}</s:param>
                    </s:url>
                    <a href="${alvo}">${item}</a>
                    <br />
            </c:forEach>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>

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
    <s:set var="index" value="1"></s:set>
    <s:set var="index2" value="1"></s:set>
    <c:choose>
        <c:when test="${results == null}">
            Problema durante a pesquisa!
        </c:when>
        <c:when test="${results.isEmpty()}">
            Não foram encontrados detalhes para a pesquisa!
        </c:when>
        <c:otherwise>
            <br><br/>
            Detalhes:
            <br><br />
            <c:choose>
                <c:when test="${session.editor == true}">
                    <s:form method="post" action="AlterarDetalhes">
                            <c:forEach items="${results}" var="item" varStatus="loop">
                                <c:choose>
                                    <c:when test="${loop.index%2== 0}">
                                        <p><font color="#00008b">${item}</font></p>
                                        <input type='hidden' name="item_${index2}" value='${item}'/>
                                        <s:set var="index2" value="%{#index2 + 1}"></s:set>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${item != ' '}">
                                                <s:textfield name="info%{#index}" placeholder="${item}" />
                                                <s:set var="index" value="%{#index + 1}"></s:set>
                                                <br></br>
                                            </c:when>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        <s:submit type="button">
                            <s:text name="Alterar Detalhes"></s:text>
                        </s:submit>
                    </s:form>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${results}" var="item" varStatus="loop">
                        <c:choose>
                            <c:when test="${loop.index%2== 0}">
                                <p><font color="#00008b">${item}</font></p>
                            </c:when>
                            <c:otherwise>
                                ${item}
                                <br></br>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
    <br></br>

    <c:choose>
        <c:when test="${session.LastSearchType == 'album'}">
            <a href="Critica.jsp"><button>Fazer Critica</button></a>
        </c:when>
    </c:choose>
</div>
</body>
</html>

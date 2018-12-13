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

    <s:form method="post" action="Creat">
    <c:choose>
        <c:when test="${param.info1 == 'musica'}">
            <div>
                <input type='hidden' name='info1' value='musica' />

                <li><s:text name="Nome:" />
                        <s:textfield name="creating" />
                <li><s:text name="Artista"/>
                        <s:textfield name="info2" />
                <li><s:text name="Compositor:" />
                        <s:textfield name="info3" />
            </div>
        </c:when>
        <c:when test="${param.info1 == 'album'}">
            <div>
                <input type='hidden' name='info1' value='album' />

                <s:param name="info1">album</s:param>

                <li><s:text name="Nome:" />
                        <s:textfield name="creating" />
                <li><s:text name="Artista"/>
                        <s:textfield name="info2" />
                <li><s:text name="Editor:" />
                        <s:textfield name="info3" />
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <input type='hidden' name='info1' value='artista' />

                <s:param name="info1">artista</s:param>

                <li><s:text name="Nome:" />
                        <s:textfield name="creating" />
                <li><s:text name="Idade:"/>
                        <s:textfield name="info2" />
                <li><s:text name="É compositor?:" />
                        <s:textfield name="info3" />
            </div>
        </c:otherwise>
    </c:choose>
    <div>
        <br><s:submit type="button">
        <s:text name="Criar"></s:text>
    </s:submit>
    </s:form>
    </div>
</div>
</body>
</html>

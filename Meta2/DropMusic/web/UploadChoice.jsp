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
</div>

<div title="main">
    <c:choose>
        <c:when test="${results == null}">
            Problema durante a pesquisa!
        </c:when>
        <c:when test="${results.isEmpty()}">
            Não foram encontradas nenhumas músicas!
        </c:when>
        <c:otherwise>
            <br />
            <br>
            <br>
            Músicas:
            <br>
            <s:form method="post" action="upload">
                <select id="info1" name="inputObject.tipo">
                    <c:forEach items="${results}" var="item">
                        <option value="${item}">${item}</option>
                    </c:forEach>
                </select>
                <s:textfield name="inputObject.dir"/>
                <s:submit type="button">
                    <s:text name="Upload"></s:text>
                </s:submit>
            </s:form>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>

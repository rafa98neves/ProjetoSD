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

<br title="main">
    <br>
    Procure uma música para associar ao seu upload!
    </br>
    <s:form method="GET" action="Pesquisar">
        <input type='hidden' name='inputObject.tipo' value='musica'/>
        <input type='hidden' name='inputObject.flag' value='upload'/>
        <s:textfield name="inputObject.searching"/>
        <s:submit type="button">
            <s:text name="Procurar"></s:text>
        </s:submit>
    </s:form>
</div>
</body>
</html>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<t:genericadminpage>
    <jsp:attribute name="title">
      Easypass - Ajout d'un mot de passe
    </jsp:attribute>
    <jsp:body>
        <h1>Ajout d'un mot de passe</h1>
        <jsp:include page="form.jsp"/>
    </jsp:body>
</t:genericadminpage>
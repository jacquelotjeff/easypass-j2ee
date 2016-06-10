<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<t:genericpage>
    <jsp:attribute name="title">
      Easypass - Inscription
    </jsp:attribute>
    <jsp:body>
        <div class="jumbotron">
            <div class="container">
                <h1>Inscription</h1>
                <jsp:include page="form.jsp"/>
            </div>
        </div>
    </jsp:body>
</t:genericpage>
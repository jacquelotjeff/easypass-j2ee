<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ page import="fr.easypass.servlets.front.FrontGroupServlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<t:genericuserpage>
    <jsp:attribute name="title">
      Easypass - Consulter le groupe <c:out value="${group.getName()}" />
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script src="${pageContext.request.contextPath}/js/password-spoiler.js"></script>
    </jsp:attribute>
    <jsp:body> 
        <h3>Consulter le groupe ${group.getName()}</h3>
        <p>Nom : ${group.getName()}</p>
        <p>Description : ${group.getDescription()}</p>
        <p>
            Logo : 
            <img width=250 class="thumbnail" alt="Logo du groupe ${group.getName()}" src="${pageContext.servletContext.contextPath}/fichier?nom=${group.getLogo()}">
        </p>
        
        <div class="col-sm-6">
            <ul class="list-group">
                <li class="list-group-item title">
                    <i class="fa fa-users"></i>
                    Membres
                </li>
                <c:forEach var="user" items="${groupUsers.values()}">
                 <li class="list-group-item text-left">
                    <img class="img-thumbnail"
                                src="${pageContext.servletContext.contextPath}/fichier?nom=User.png">
                    <label class="name">
                        ${user.getUsername()}<br>
                        <c:if test="${groupAdmins.containsKey(user.getId())}">
                            (Admin)
                        </c:if>
                    </label> 
                    <div class="break"></div>
                  </li>
                </c:forEach>
            </ul>
        </div>
        <div class="col-sm-6">
            <ul class="list-group">
                <li class="list-group-item title">
                    <i class="fa fa-key"></i>
                    Mots de passes
                </li>
                <c:forEach var="password" items="${passwords.values()}">
                        <li class="list-group-item title">
                            <span class="col-sm-2">
                                <c:set var="category"
                                        value="${categories.get(password.getCategory())}" />
                                <img class="thumbnail"
                                    alt="Logo de la catégorie ${category.getName()}"
                                    src="${pageContext.servletContext.contextPath}/fichier?nom=${category.getLogo()}">
                            </span>
                            <div class="col-sm-6">
                                <h4 class="list-group-item-heading">${password.getTitle()}</h4>
                                <p class="list-group-item-text">
                                    ${password.getInformations()}
                                </p>
                            </div>
                            <div class="col-sm-4">
                                <div class="input-group">
                                    <input type="password"
                                            class="form-control password-field"
                                            value="${password.getPassword()}">
                                    <div class="input-group-addon show-password">
                                        <span class="fa fa-eye"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                        </li>
                </c:forEach>
            </ul>
        </div>
        <div class="col-sm-12 text-right">
            <c:url value="groupes/supprimer" var="deleteURL">
                <c:param name="groupId" value="${group.getId()}" />
            </c:url>
            <form action="${deleteURL}" method="POST">
                <c:if test="${groupAdmins.containsKey(sessionScope.user.getId())}">
                    <button type="submit" class="btn btn-danger">Supprimer</button>
                    <c:url value="editer" var="editURL">
                        <c:param name="groupId" value="${group.getId()}" />
                    </c:url>
                    <a class="btn btn-primary" href="${editURL}">Editer</a>
                </c:if>
                <a class="btn btn-success" href="${pageContext.request.contextPath}${FrontGroupServlet.URL_BASE}">Retour</a>
            </form>
        </div>
    </jsp:body>
</t:genericuserpage>
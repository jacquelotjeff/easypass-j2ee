<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="fr.easypass.servlets.back.BackUserServlet"%>
<%@ page import="fr.easypass.servlets.back.BackPasswordServlet"%>
<%@ page import="fr.easypass.servlets.back.BackGroupServlet"%>
<%@ page import="fr.easypass.model.Password"%>

<t:genericadminpage>
    <jsp:attribute name="title">
      Easypass - Consulter le profil de <c:out value="${user.getUsername()}"/>
    </jsp:attribute>
    <jsp:attribute name="scripts">
        <script src="${pageContext.request.contextPath}/js/password-spoiler.js"></script>
    </jsp:attribute>
    <jsp:body>
        <div class="row"> 
            <div class="col-sm-12">
                <div class="user-image text-center">
                    <img src="${pageContext.servletContext.contextPath}/fichier?nom=User.png" alt="Utilisateur" title="Utilisateur" class="img-circle" width="100">
                </div>
                <div class="user-info-block">
                    <div class="user-heading">
                        <div class="col-sm-12">
                            <div>
                                <h3>${user.getUsername()}</h3>
                                <span class="help-block">${user.getEmail()}</span>
                            </div>
                            <div>
                                <c:url value="supprimer" var="deleteURL">
                                    <c:param name="userId"   value="${user.getId()}" />
                                </c:url>
                                <form action="${deleteURL}" method="POST" class="form-group">
                                    <c:url value="editer" var="editURL">
                                        <c:param name="userId"   value="${user.getId()}" />
                                    </c:url>
                                    <a class="btn btn-primary" href="${editURL}">Editer</a>
                                    <button type="submit" class="btn btn-danger">Supprimer</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <ul class="navigation">
                        <li class="active">
                            <a data-toggle="tab" href="#information">
                                <i class="fa fa-user"></i>
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#passwords">
                                <i class="fa fa-key"></i>
                            </a>
                        </li>
                        <li>
                            <a data-toggle="tab" href="#groups">
                                <i class="fa fa-users"></i>
                            </a>
                        </li>
                    </ul>
                    <div class="user-body">
                        <div class="tab-content">
                            <div id="information" class="tab-pane active">
                                <h3>Informations générales</h3>
                                <p>Prenom : <c:out value="${user.getFirstname()}"/></p>
                                <p>Nom : <c:out value="${user.getLastname()}"/></p>
                                <p>Email : <c:out value="${user.getEmail()}"/></p>    
                            </div>
                            <div id="passwords" class="tab-pane">
                                <div class="col-sm-12">
                                    <h3>Mots de passes</h3>
                                    <ul class="list-group">
                                        <c:forEach var="password" items="${passwords}">
                                                <li class="list-group-item title">
                                                    <span class="col-sm-2">
                                                        <c:set var="category" value="${categories.get(password.getCategory())}"/>
                                                        <img class="thumbnail" alt="Logo de la catégorie ${category.getName()}" src="${pageContext.servletContext.contextPath}/fichier?nom=User.png">
                                                    </span>
                                                    <div class="col-sm-6">
                                                        <h4 class="list-group-item-heading">${password.getTitle()}</h4>
                                                        <p class="list-group-item-text">
                                                            ${password.getInformations()}
                                                        </p>
                                                    </div>
                                                    <div class="col-sm-4">
                                                        <div class="input-group">
                                                            <input type="password" class="form-control password-field" value="${password.getPassword()}">
                                                            <div class="input-group-addon show-password">
                                                                <span class="fa fa-eye"></span>
                                                            </div>
                                                            <c:url value="${BackPasswordServlet.URL_BASE}/editer" var="editPasswordURL">
                                                                <c:param name="passwordId" value="${password.getId()}"/>
                                                            </c:url>
                                                            <a href="${editPasswordURL}" class="input-group-addon">
                                                                <i class="fa fa-edit"></i>
                                                            </a>
                                                        </div>
                                                    </div>
                                                    <div class="clearfix"></div>
                                                </li>
                                        </c:forEach>
                                        <c:url value="${BackPasswordServlet.URL_BASE}/creer" var="addPasswordURL">
                                                <c:param name="ownerId"
                                                    value="${user.getId()}" />
                                                <c:param name="ownerType"
                                                    value="${Password.OWNER_TYPE_USER}" />
                                        </c:url>  
                                        <li class="list-group-item title">
                                            <a href="${addPasswordURL}" class="btn btn-md btn-primary">
                                                <i class="fa fa-plus"></i>
                                                Ajouter un mot de passe
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            <div id="groups" class="tab-pane">
                                <div class="col-sm-12">
                                    <h3>Groupes</h3>
                                    <ul class="list-group">
                                        <c:forEach var="group" items="${groups}">
                                            <li class="list-group-item title">
                                                    <span class="col-sm-2">
                                                        <img class="thumbnail" alt="Logo du groupe ${group.getName()}" src="${pageContext.servletContext.contextPath}/fichier?nom=${group.getLogo()}">
                                                    </span>
                                                    <div class="col-sm-6">
                                                        <h4 class="list-group-item-heading">${group.getName()}</h4>
                                                        <p class="list-group-item-text">
                                                            ${group.getDescription()}
                                                        </p>
                                                    </div>
                                                    <c:url value="${BackGroupServlet.URL_BASE}/supprimer" var="deleteURL">
                                                        <c:param name="groupId"   value="${group.getId()}" />
                                                    </c:url>
                                                    <form action="${deleteURL}" method="POST">
                                                        <div class="btn-group">
                                                            <c:url value="${BackGroupServlet.URL_BASE}/voir" var="showGroupURL">
                                                                <c:param name="groupId" value="${group.getId()}"/>
                                                            </c:url>
                                                            <a href="${showGroupURL}" title="Voir le groupe." class="btn btn-success btn-sm">
                                                                <i class="fa fa-eye"></i>
                                                            </a>
                                                            <c:url value="${BackGroupServlet.URL_BASE}/editer" var="editGroupURL">
                                                                <c:param name="groupId" value="${group.getId()}"/>
                                                            </c:url>
                                                            <a href="${editGroupURL}" title="Editer le groupe." class="btn btn-primary btn-sm">
                                                                <i class="fa fa-edit"></i>
                                                            </a>
                                                            <button type="submit"  title="Supprimer le groupe" class="btn btn-sm btn-danger">
                                                                <i class="fa fa-remove"></i>
                                                            </button>
                                                        </div>
                                                    </form>
                                                    <div class="clearfix"></div>
                                                </li>
                                        </c:forEach>
                                        <c:url value="${BackGroupServlet.URL_BASE}/creer" var="addGroupURL">
                                        </c:url>
                                        <li class="list-group-item title">
                                            <a href="${addGroupURL}" class="btn btn-md btn-primary">
                                                <i class="fa fa-plus"></i>
                                                Ajouter un groupe
                                            </a>
                                        </li>  
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-12">
                <div class="pull-right">
                    <a class="btn btn-success" href="${pageContext.request.contextPath}${BackUserServlet.URL_BASE}">Retour</a>
                </div>
            </div>
        </div>        
    </jsp:body>
</t:genericadminpage>
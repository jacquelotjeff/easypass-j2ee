package fr.easypass.servlets.back;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;

import fr.easypass.manager.CategoryManager;
import fr.easypass.manager.GroupManager;
import fr.easypass.manager.UserManager;
import fr.easypass.model.Group;
import fr.easypass.model.User;
import fr.easypass.servlets.BaseServlet;
import fr.easypass.servlets.LoginServlet;

/**
 * Servlet implementation class GroupServlet
 */
@WebServlet(name = "BackGroupServlet", description = "Group Servlet", urlPatterns = { BackGroupServlet.URL_BASE + "",
        BackGroupServlet.URL_BASE + "/voir", BackGroupServlet.URL_BASE + "/editer", BackGroupServlet.URL_BASE + "/creer",
        BackGroupServlet.URL_BASE + "/supprimer", BackGroupServlet.URL_BASE + "/ajouter-utilisateur",
        BackGroupServlet.URL_BASE + "/supprimer-utilisateur", BackGroupServlet.URL_BASE + "/admin-utilisateur" })
public class BackGroupServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    public static final String URL_BASE = "/admin/groupes";
    public static final String viewPathPrefix = "/WEB-INF/html/back/group";
    public final GroupManager groupManager = new GroupManager();
    public final UserManager userManager = new UserManager();
    public final CategoryManager categoryManager = new CategoryManager();
    
    public static final Logger log = Logger.getLogger(CategoryManager.class.getName());
    
    private HashMap<String, String> errors;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BackGroupServlet() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        super.doGet(request, response);

        final String uri = request.getRequestURI();

        if (uri.contains(URL_BASE + "/voir")) {
            this.show(request, response);
        } else if (uri.contains(URL_BASE + "/ajouter-utilisateur")) {
            this.addUser(request, response);
        } else if (uri.contains(URL_BASE + "/supprimer-utilisateur")) {
            this.deleteUser(request, response);
        } else if (uri.contains(URL_BASE + "/admin-utilisateur")) {
            this.adminUser(request, response);
        } else if (uri.contains(URL_BASE + "/creer")) {
            this.create(request, response);
        } else if (uri.contains(URL_BASE + "/editer")) {
            this.edit(request, response);
        } else if (uri.contains(URL_BASE + "/supprimer")) {
            this.delete(request, response);
        } else {
            this.list(request, response);
        }

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Map<Integer, Group> groups = groupManager.getGroups();

        request.setAttribute("groups", groups.values());

        request.getRequestDispatcher(BackGroupServlet.viewPathPrefix + "/list.jsp").forward(request, response);

        return;
    }

    private void show(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Integer groupId = this.checkGroupParam(request, response);

        if (groupId > 0) {

            final Group group = this.groupManager.getGroup(groupId);

            if (group == null) {
                this.alertGroupNotFound(request);
            } else {

                request.setAttribute("group", group);
                request.getRequestDispatcher(BackGroupServlet.viewPathPrefix + "/show.jsp").forward(request, response);

                return;
            }
        }

        response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE);
        return;
    }

    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        final User user = LoginServlet.getCurrentUser(request);

        HttpSession session = request.getSession();
        final String method = request.getMethod();

        if (method == "POST") {

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String logo = request.getParameter("logo");
            List<String> users = new ArrayList<>(Arrays.asList(request.getParameterValues("users")));
            List<String> admins = new ArrayList<>();
            
            Group group = new Group(name, description, logo);
            
            users.add(user.getId().toString());
            admins.add(user.getId().toString());
            
            errors = group.isValid();
            
            if (errors.isEmpty()) {
                
                final Integer success = this.groupManager.insertGroup(name, description, logo, users, admins);

                if (success == 1) {

                    session.setAttribute("alertClass", "alert-success");
                    session.setAttribute("alertMessage", "Le groupe à bien été créé");

                } else {

                    session.setAttribute("alertClass", "alert-danger");
                    session.setAttribute("alertMessage", "Le groupe n'a pas pu être créé");
                }
                
                response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE);
                return;
                
            } else {
                
                request.setAttribute("errors", errors);
            }
            
        }
        
        final Map<Integer, User> users = userManager.getUsers();
        users.remove(LoginServlet.getCurrentUser(request).getId());
        request.setAttribute("users", users.values());
        request.setAttribute("formAction", "creer");
        request.getRequestDispatcher(BackGroupServlet.viewPathPrefix + "/create.jsp").forward(request, response);

        return;

    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        final String method = request.getMethod();

        Integer groupId = this.checkGroupParam(request, response);
        if (groupId > 0) {

            if (method == "GET") {

                final Group group = this.groupManager.getGroup(groupId);

                if (group == null) {
                    this.alertGroupNotFound(request);
                }

                final Map<Integer, User> availableUsers = userManager.getUsersAvailableByGroup(groupId);
                final Map<Integer, User> groupUsers = userManager.getUsersByGroup(groupId).get("groupUsers");
                final Map<Integer, User> groupAdmins = userManager.getUsersByGroup(groupId).get("groupAdmins");

                request.setAttribute("users", availableUsers.values());
                request.setAttribute("groupUsers", groupUsers.values());
                request.setAttribute("groupAdmins", groupAdmins);
                request.setAttribute("group", group);
                request.setAttribute("formAction", "editer");

                request.getRequestDispatcher(BackGroupServlet.viewPathPrefix + "/edit.jsp").forward(request, response);

                return;

            } else {

                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String logo = request.getParameter("logo");

                final Integer success = this.groupManager.editGroup(groupId, name, description, logo);

                if (success == 1) {
                    session.setAttribute("alertClass", "alert-success");
                    session.setAttribute("alertMessage", "Le groupe a bien été édité.");

                } else {
                    session.setAttribute("alertClass", "alert-danger");
                    session.setAttribute("alertMessage", "Le groupe n'a pas pu être édité.");
                }

            }

        }

        response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE);
        return;

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        final String method = request.getMethod();

        Integer groupId = this.checkGroupParam(request, response);

        if (groupId > 0) {

            if (method == "POST") {
                final Integer success = this.groupManager.deleteGroup(groupId);

                if (success == 0) {
                    session.setAttribute("alertClass", "alert-danger");
                    session.setAttribute("alertMessage", "Le groupe n'a pas pu être supprimé.");
                } else {
                    session.setAttribute("alertClass", "alert-success");
                    session.setAttribute("alertMessage", "Le groupe a bien été supprimé.");
                }

            } else {
                session.setAttribute("alertClass", "alert-danger");
                session.setAttribute("alertMessage", "Accès interdit");
            }
        }

        response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE);

        return;
    }

    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        final String method = request.getMethod();

        try {

            if (method == "POST") {

                Integer groupId = NumberUtils.createInteger(request.getParameter("groupId"));
                Integer userId = NumberUtils.createInteger(request.getParameter("userId"));

                final Integer success = this.groupManager.addUser(groupId, userId);

                if (success == 0) {
                    session.setAttribute("alertClass", "alert-danger");
                    session.setAttribute("alertMessage", "L'utilisateur  n'a pas pu être ajouté au groupe.");
                } else {
                    session.setAttribute("alertClass", "alert-success");
                    session.setAttribute("alertMessage", "L'utilisateur a été ajouté au groupe.");

                    response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE + "/editer" + "?groupId=" + groupId);

                    return;
                }

            } else {
                session.setAttribute("alertClass", "alert-danger");
                session.setAttribute("alertMessage", "Accès interdit");
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while adding a user", e);
            session.setAttribute("alertClass", "alert-danger");
            session.setAttribute("alertMessage", "Impossible d'ajouter l'utilisateur.");

        }

        response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE);

        return;

    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        final String method = request.getMethod();

        try {

            Integer groupId = NumberUtils.createInteger(request.getParameter("groupId"));
            Integer userId = NumberUtils.createInteger(request.getParameter("userId"));

            if (method == "POST") {

                final Integer success = this.groupManager.deleteUser(groupId, userId);

                if (success == 0) {
                    session.setAttribute("alertClass", "alert-danger");
                    session.setAttribute("alertMessage", "L'utilisateur n'a pas pu être retiré du groupe");
                } else {
                    session.setAttribute("alertClass", "alert-success");
                    session.setAttribute("alertMessage", "L'utilisateur a été retiré du groupe.");

                    response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE + "/editer" + "?groupId=" + groupId);

                    return;

                }

            } else {
                session.setAttribute("alertClass", "alert-danger");
                session.setAttribute("alertMessage", "Accès interdit");
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while deleting a user", e);
            session.setAttribute("alertClass", "alert-danger");
            session.setAttribute("alertMessage", "Impossible de supprimer l'utilisateur.");

        }

        response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE);

        return;

    }

    private void adminUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        final String method = request.getMethod();

        try {

            if (method == "POST") {

                Integer groupId = NumberUtils.createInteger(request.getParameter("groupId"));
                Integer userId = NumberUtils.createInteger(request.getParameter("userId"));
                Boolean admin = BooleanUtils.toBoolean(request.getParameter("admin"));

                final Integer success = this.groupManager.setUserAdmin(groupId, userId, admin);

                if (success == 0) {
                    session.setAttribute("alertClass", "alert-danger");
                    session.setAttribute("alertMessage", "Le statut de l'utilisateur n'a pas été mis à jour.");
                } else {
                    session.setAttribute("alertClass", "alert-success");
                    session.setAttribute("alertMessage", "Le statut de l'utilisateur a été mis à jour.");

                    response.sendRedirect(this.getServletContext().getContextPath() + BackGroupServlet.URL_BASE + "/editer" + "?groupId=" + groupId);

                    return;
                }

            } else {
                session.setAttribute("alertClass", "alert-danger");
                session.setAttribute("alertMessage", "Accès interdit");
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while changing user status", e);
            session.setAttribute("alertClass", "alert-danger");
            session.setAttribute("alertMessage", "Impossible de changer le statut de l'utilisateur.");

        }

        return;

    }

    private Integer checkGroupParam(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer groupId = 0;

        try {
            groupId = NumberUtils.createInteger(request.getParameter("groupId"));
        } catch (Exception e) {
            this.alertGroupNotFound(request);
        }

        return groupId;
    }

    private void alertGroupNotFound(HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession();
        session.setAttribute("alertClass", "alert-danger");
        session.setAttribute("alertMessage", "Le groupe n'a pas été trouvé.");

        return;

    }

}

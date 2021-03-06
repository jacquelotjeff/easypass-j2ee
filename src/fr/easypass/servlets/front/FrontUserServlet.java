package fr.easypass.servlets.front;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.easypass.manager.CategoryManager;
import fr.easypass.manager.GroupManager;
import fr.easypass.manager.PasswordManager;
import fr.easypass.manager.UserManager;
import fr.easypass.model.User;
import fr.easypass.servlets.BaseServlet;
import fr.easypass.servlets.LoginServlet;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet(name = "FrontUserServlet", description = "User Servlet", urlPatterns = { FrontUserServlet.URL_BASE + "",
        FrontUserServlet.URL_BASE + "/editer" })
public class FrontUserServlet extends BaseServlet {

    private static final long serialVersionUID = 1L;
    public static final String URL_BASE = "/utilisateur";
    public static final String viewPathPrefix = "/WEB-INF/html/front/user";
    public final UserManager userManager = new UserManager();
    public final GroupManager groupManager = new GroupManager();
    public final PasswordManager passwordManager = new PasswordManager();
    public final CategoryManager categoryManager = new CategoryManager();
    private HashMap<String, String> errors;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FrontUserServlet() {
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

        if (uri.contains(URL_BASE + "/editer")) {
            this.edit(request, response);
        } else {
            this.show(request, response);
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

    /**
     * Display a user profile
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void show(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Integer userId = LoginServlet.getCurrentUser(request).getId();

        final User user = this.userManager.getUser(userId);
        request.setAttribute("user", user);
        
        request.getRequestDispatcher(FrontUserServlet.viewPathPrefix + "/show.jsp").forward(request, response);

        return;
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        final String method = request.getMethod();

        Integer userId = LoginServlet.getCurrentUser(request).getId();

        if (method == "GET") {

            final User user = this.userManager.getUser(userId);

            request.setAttribute("user", user);
            request.setAttribute("formAction", "editer");
            request.getRequestDispatcher(FrontUserServlet.viewPathPrefix + "/edit.jsp").forward(request, response);

            return;

        } else {

            User user = this.createUserFromParam(request);
            
            user.setUsername(LoginServlet.getCurrentUser(request).getUsername());
            errors = user.isValidWithoutPassword();
            

            if (errors.isEmpty()) {

                final Integer success = this.userManager.editUserWithoutPassword(userId, user);

                if (success == 1) {
                    session.setAttribute("alertClass", "alert-success");
                    session.setAttribute("alertMessage", "L'utilisateur a bien été édité.");

                } else {
                    session.setAttribute("alertClass", "alert-danger");
                    session.setAttribute("alertMessage", "L'utilisateur n'a pas pu être édité.");
                }

            } else {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(FrontUserServlet.viewPathPrefix + "/edit.jsp").forward(request, response);
                return;
            }
        }

        response.sendRedirect(this.getServletContext().getContextPath() + FrontUserServlet.URL_BASE);
        return;
    }

    private User createUserFromParam(HttpServletRequest request) throws IOException {

        User user = new User(request.getParameter("firstname"), request.getParameter("lastname"),
                request.getParameter("username"), request.getParameter("password"), request.getParameter("email"),
                false);

        return user;

    }
}

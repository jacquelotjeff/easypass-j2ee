package fr.easypass.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.easypass.model.User;
import fr.easypass.servlets.front.FrontUserServlet;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet(name = "HomeServlet", description = "Home Servlet", urlPatterns = {""})
public class HomeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        super.doGet(request, response);
        this.home(request, response);
        
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void home(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	User user = LoginServlet.getCurrentUser(request);
    	
    	if (user != null) {
    		response.sendRedirect(this.getServletContext().getContextPath() + FrontUserServlet.URL_BASE);
    	} else {
    		request.getRequestDispatcher("/WEB-INF/html/front/home.jsp").forward(request, response);
    	}
        

        return;
    }
}

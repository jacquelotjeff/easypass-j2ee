package fr.easypass.filters.params;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;

import fr.easypass.servlets.back.BackGroupServlet;
import fr.easypass.servlets.front.FrontGroupServlet;

public class GroupParamFilter implements Filter {

    public static final Logger log = Logger.getLogger(GroupParamFilter.class.getName());

    @Override
    public void destroy() {
        // Nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        
        if (NumberUtils.isNumber(request.getParameter("groupId"))) {
            
            chain.doFilter(request, response);
            
        } else {
            
            this.restrict(session, response, request);
        }
        
        return;
        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // Nothing to do
    }

    private void restrict(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {

        session.setAttribute("alertClass", "alert-warning");
        session.setAttribute("alertMessage", "Paramètre invalide.");
        
        if (request.getRequestURI().contains(BackGroupServlet.URL_BASE)) {
            response.sendRedirect(session.getServletContext().getContextPath() + BackGroupServlet.URL_BASE);
        } else {
            response.sendRedirect(session.getServletContext().getContextPath() + FrontGroupServlet.URL_BASE);
        }
    }

}

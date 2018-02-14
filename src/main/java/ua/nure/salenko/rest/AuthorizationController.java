package ua.nure.salenko.rest;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.salenko.dao.UserDao;
import ua.nure.salenko.dao.UserDaoImpl;
import ua.nure.salenko.model.User;

public class AuthorizationController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationController.class);

    final UserDao dao = new UserDaoImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RestUtils.setLocale(request);
        HttpSession session = request.getSession();
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        response.setContentType("charset=UTF-8");
        boolean valid = true;
        if (request.getParameter("logout") != null) {
            LOGGER.info("AuthorizationController#doGet?logout");
            HttpSession s = request.getSession();
            s.invalidate();
            valid = false;
        }
        RequestDispatcher dispatcher;
        if (valid && session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("authpage_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("authpage.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s = request.getSession();
        String name = request.getParameter("name");
        String pass = request.getParameter("password");
        User user = null;
        LOGGER.info("AuthorizationController#doPost?name=" + name);
        try {
            user = dao.getUserByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user == null || !user.getPassword().equals(pass)) {
            LOGGER.info("AuthorizationController#doPost?error");
            response.sendRedirect("AuthorizationController?error");
        } else if (user.getBlocked()) {
            LOGGER.info("AuthorizationController#doPost?blocked");
            response.sendRedirect("AuthorizationController?blocked");
        } else {
            LOGGER.info("AuthorizationController#doPost?loggedIn&username=" + name + "&role=" + user.getRole());
            s.setAttribute("username", name);
            s.setAttribute("role", user.getRole());
            s.setAttribute("userId", user.getId());
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

}

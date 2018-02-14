package ua.nure.salenko.rest;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.salenko.dao.UserDao;
import ua.nure.salenko.dao.UserDaoImpl;
import ua.nure.salenko.model.User;

public class RegistrationController extends HttpServlet {

    final UserDao dao = new UserDaoImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RestUtils.setLocale(request);
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        response.setContentType("charset=UTF-8");
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("register_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String pass = request.getParameter("password");
        User user = null;
        try {
            user = dao.getUserByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setPassword(pass);
            try {
                dao.insert(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            response.sendRedirect("RegistrationController?success");
        } else {
            response.sendRedirect("RegistrationController?error");
        }

    }

}

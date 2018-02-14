package ua.nure.salenko.rest;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class StoveMarketController extends AbstractMarketController {

    @Override
    protected void startPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("StovePage_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("StovePage.jsp");
        dispatcher.forward(request, response);
    }

}

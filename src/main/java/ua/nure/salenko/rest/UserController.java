package ua.nure.salenko.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

import ua.nure.salenko.dao.UserDao;
import ua.nure.salenko.dao.UserDaoImpl;
import ua.nure.salenko.model.User;
import ua.nure.salenko.pagination.UserPaginator;

public class UserController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    final UserDao dao = new UserDaoImpl();

    protected Long listCount(String filter) throws SQLException {
        return dao.getCount(filter);
    }

    @SuppressWarnings("unchecked")
    protected List<User> findRow(int startPosition, int maxResults, String sortFields, String sortDirections,
            String filter) throws SQLException {
        return dao.sortedFind(startPosition, maxResults, sortFields, sortDirections, filter);
    }

    protected UserPaginator findRow(UserPaginator wrapper, String filter) throws SQLException {
        wrapper.setTotalResults(listCount(filter));
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(
                findRow(start, wrapper.getPageSize(), wrapper.getSortFields(), wrapper.getSortDirections(), filter));
        return wrapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RestUtils.setLocale(request);
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        response.setContentType("charset=UTF-8");
        HttpSession session = request.getSession();
        if (session.getAttribute("role") == null || !session.getAttribute("role").equals("admin")) {
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            String action = request.getParameter("action");
            try {
                LOGGER.info("UserController#doGet?action=" + action);
                switch (action) {
                case "start":
                    startPage(request, response);
                    break;
                default:
                    listUser(request, response);
                    break;
                }
            } catch (SQLException e) {
                LOGGER.error("UserController#doGet?SQLException", e);
                throw new ServletException(e);
            }
        }
    }

    private void startPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("AdminUserPage_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("AdminUserPage.jsp");
        dispatcher.forward(request, response);

    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        LOGGER.info("UserController#listUser");
        // if userId is empty - we got list of ten users, else - we got user by id
        if (request.getParameter("userId") == "") {
            Integer page;
            String sortFields;
            String sortDirections;

            String filter = "";
            if (Boolean.parseBoolean(request.getParameter("thisIsFilter")))
                filter = request.getParameter("fname");

            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }

            sortFields = request.getParameter("sortFields");
            if (sortFields == null)
                sortFields = "id";

            sortDirections = request.getParameter("sortDirections");
            if (sortDirections == null)
                sortDirections = "asc";

            UserPaginator paginator = new UserPaginator(page, sortFields, sortDirections, 10);
            request.setAttribute("list", findRow(paginator, filter).getList());
            response.setHeader("Content-Type", "text/xml; charset=UTF-8");
            response.setContentType("charset=UTF-8");
            String jsonList = RestUtils.makeJsonUserList(paginator.getCurrentPage(), paginator.getPageSize(),
                    paginator.getTotalResults(), paginator.getSortFields(), paginator.getSortDirections(),
                    paginator.getList());
            PrintWriter pw = response.getWriter();
            pw.println(jsonList);
        } else {
            Long id = Long.parseLong(request.getParameter("userId"));
            try {
                User p = dao.getUserById(id);
                String jsonUser = RestUtils.makeJsonUser(p);
                response.setHeader("Content-Type", "text/xml; charset=UTF-8");
                response.setCharacterEncoding("utf-8");
                PrintWriter pw = response.getWriter();
                pw.println(jsonUser);
            } catch (NullPointerException | MySQLNonTransientConnectionException e) {
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jsonRow = null;
        User user = null;
        BufferedReader reader = request.getReader();
        jsonRow = reader.readLine();
        if (!jsonRow.contains("thisIsFilter")) {
            user = RestUtils.makeUserFromJsonRow(jsonRow);
            try {
                dao.update(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

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

import ua.nure.salenko.dao.DealDao;
import ua.nure.salenko.dao.DealDaoImpl;
import ua.nure.salenko.model.Deal;
import ua.nure.salenko.model.DealItem;
import ua.nure.salenko.pagination.DealPaginator;

public class DealController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DealController.class);

    final DealDao dealDao = new DealDaoImpl();

    protected Long listCount(Long userId, String filter, String filter2) throws SQLException {
        return dealDao.getCount(userId, filter, filter2);
    }

    @SuppressWarnings("unchecked")
    protected List<Deal> findRow(int startPosition, int maxResults, String sortFields, String sortDirections,
            Long userId, boolean admin, String filter, String filter2) throws SQLException {
        // if userId == null || userId == 0 - it will find all Deals
        return dealDao.sortedFindByUserId(startPosition, maxResults, sortFields, sortDirections, userId, admin, filter, filter2);
    }

    protected DealPaginator findRow(DealPaginator wrapper, Long userId, boolean admin, String filter, String filter2)
            throws SQLException {
        wrapper.setTotalResults(listCount(userId, filter, filter2));
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findRow(start, wrapper.getPageSize(), wrapper.getSortFields(), wrapper.getSortDirections(),
                userId, admin, filter, filter2));
        return wrapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RestUtils.setLocale(request);
        response.setHeader("Content-Type", "text/xml; charset=UTF-8");
        response.setContentType("charset=UTF-8");
        HttpSession session = request.getSession();
        if (session.getAttribute("userId") == null || session.getAttribute("userId").equals("")) {
            response.sendRedirect("AuthorizationController");
        } else {
            String action = request.getParameter("action");
            try {
                LOGGER.info("DealController#doGet?action=" + action);
                switch (action) {
                case "myDeals":
                    if (session.getAttribute("role").equals("admin"))
                        session.setAttribute("asAdmin", "false");
                    startUserPage(request, response);
                    break;
                case "deals":
                    if (session.getAttribute("role").equals("admin")) {
                        session.setAttribute("asAdmin", "true");
                        startAdminPage(request, response);
                    } else
                        response.sendRedirect("AuthorizationController");

                    break;
                default:
                    listDeal(request, response);
                    break;
                }
            } catch (SQLException e) {
                LOGGER.error("DealController#doGet?SQLException", e);
                throw new ServletException(e);
            }
        }
    }

    private void startUserPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("UserDealPage_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("UserDealPage.jsp");        
        dispatcher.forward(request, response);
    }

    private void startAdminPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("AdminDealPage_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("AdminDealPage.jsp"); 
        dispatcher.forward(request, response);
    }

    private void listDeal(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        LOGGER.info("DealController#listDeal");
        // if dealId is empty - we got list of ten cart items, else - we got deal by id
        if (request.getParameter("dealId") == "") {
            HttpSession session = request.getSession();
            Integer page;
            String sortFields;
            String sortDirections;

            String filter1 = "";
            String filter2 = "";
            if (Boolean.parseBoolean(request.getParameter("thisIsFilter"))) {
                filter1 = request.getParameter("fname");
                filter2 = request.getParameter("fproduct");
            }

            Long userId;
            if (session.getAttribute("role").equals("admin") && session.getAttribute("asAdmin").equals("true"))
                userId = 0L;
            else {
                try {
                    userId = (Long) session.getAttribute("userId");
                } catch (NumberFormatException e) {
                    userId = -1L;
                }
            }
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

            DealPaginator paginator = new DealPaginator(page, sortFields, sortDirections, 10);
            request.setAttribute("list", findRow(paginator, userId,
                    session.getAttribute("role").equals("admin") && session.getAttribute("asAdmin").equals("true"),
                    filter1, filter2).getList());
            response.setContentType("charset=UTF-8");
            String jsonList = RestUtils.makeJsonDealList(paginator.getCurrentPage(), paginator.getPageSize(),
                    paginator.getTotalResults(), paginator.getSortFields(), paginator.getSortDirections(),
                    paginator.getList());
            PrintWriter pw = response.getWriter();
            pw.println(jsonList);
        } else {
            Long id = Long.parseLong(request.getParameter("dealId"));
            try {
                Deal d = dealDao.getDealById(id);
                List<DealItem> dList = dealDao.getListItems(d.getId());
                String jsonCart = RestUtils.makeJsonDeal(d, dList);
                PrintWriter pw = response.getWriter();
                pw.println(jsonCart);
            } catch (NullPointerException | MySQLNonTransientConnectionException e) {
                e.printStackTrace();
                ;
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jsonRow = null;
        Deal deal = null;
        BufferedReader reader = request.getReader();
        jsonRow = reader.readLine();
        if (!jsonRow.contains("thisIsFilter")) {
            deal = RestUtils.makeDealFromJsonRow(jsonRow);
            try {
                dealDao.update(deal);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

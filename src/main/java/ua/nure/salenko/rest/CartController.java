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

import ua.nure.salenko.dao.CartDao;
import ua.nure.salenko.dao.CartDaoImpl;
import ua.nure.salenko.dao.DealDao;
import ua.nure.salenko.dao.DealDaoImpl;
import ua.nure.salenko.model.Cart;
import ua.nure.salenko.model.Deal;
import ua.nure.salenko.pagination.CartPaginator;

public class CartController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CartController.class);

    final DealDao dealDao = new DealDaoImpl();
    final CartDao cartDao = new CartDaoImpl();

    protected Long listCount(Long userId) throws SQLException {
        return cartDao.getCount(userId);
    }

    @SuppressWarnings("unchecked")
    protected List<Cart> findRow(int startPosition, int maxResults, String sortFields, String sortDirections,
            Long userId) throws SQLException {
        return cartDao.sortedFind(startPosition, maxResults, sortFields, sortDirections, userId);
    }

    protected CartPaginator findRow(CartPaginator wrapper, Long userId) throws SQLException {
        wrapper.setTotalResults(listCount(userId));
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(
                findRow(start, wrapper.getPageSize(), wrapper.getSortFields(), wrapper.getSortDirections(), userId));
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
            LOGGER.info("CartController#doGet?unAuthorized");
            response.sendRedirect("AuthorizationController");
        } else {
            String action = request.getParameter("action");
            try {
                LOGGER.info("CartController#doGet?action=" + action);
                switch (action) {
                case "start":
                    startPage(request, response);
                    break;
                default:
                    listCart(request, response);
                    break;
                }
            } catch (SQLException e) {
                LOGGER.error("PriceChangeController#doGet?SQLException", e);
                throw new ServletException(e);
            }
        }
    }

    private void startPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("CartPage_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("CartPage.jsp");
        dispatcher.forward(request, response);
    }

    private void listCart(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        LOGGER.info("CartController#listCart");
        // if cartId is empty - we got list of ten cart items, else - we got cart by id
        if (request.getParameter("cartId") == "") {
            HttpSession session = request.getSession();
            Integer page;
            String sortFields;
            String sortDirections;

            Long userId;
            try {
                userId = (Long) session.getAttribute("userId");
            } catch (NumberFormatException e) {
                userId = -1L;
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

            CartPaginator paginator = new CartPaginator(page, sortFields, sortDirections, 10);
            request.setAttribute("list", findRow(paginator, userId).getList());
            response.setContentType("charset=UTF-8");
            String jsonList = RestUtils.makeJsonCartList(paginator.getCurrentPage(), paginator.getPageSize(),
                    paginator.getTotalResults(), paginator.getSortFields(), paginator.getSortDirections(),
                    paginator.getList());
            PrintWriter pw = response.getWriter();
            pw.println(jsonList);
        } else {
            Long id = Long.parseLong(request.getParameter("cartId"));
            try {
                Cart c = cartDao.getCartById(id);
                String jsonCart = RestUtils.makeJsonCart(c);
                PrintWriter pw = response.getWriter();
                pw.println(jsonCart);
            } catch (NullPointerException | MySQLNonTransientConnectionException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String jsonRow = null;
        Cart cart = null;
        BufferedReader reader = request.getReader();
        jsonRow = reader.readLine();
        if (!jsonRow.contains("doDeal")) {
            cart = RestUtils.makeCartItemFromJsonRow(jsonRow);
            try {
                cartDao.update(cart);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                List<Cart> cartList = cartDao.listAllByUserId((Long) session.getAttribute("userId"));
                Long totalWorth = 0L;
                for (Cart c : cartList) {
                    totalWorth += c.getTotalWorth();
                }
                Deal d = new Deal();
                d.setUserId((Long) session.getAttribute("userId"));
                d.setWorth(totalWorth);
                boolean doDelete = dealDao.setGroupsForUser(d, cartList);
                if (doDelete) {
                    for (Cart c : cartList)
                        cartDao.delete(c);
                    }
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();                
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            cartDao.deleteCartById(Long.parseLong(request.getParameter("cartId")));
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }

}

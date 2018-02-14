package ua.nure.salenko.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException;

import ua.nure.salenko.dao.CartDao;
import ua.nure.salenko.dao.CartDaoImpl;
import ua.nure.salenko.dao.ProductDao;
import ua.nure.salenko.dao.ProductDaoImpl;
import ua.nure.salenko.model.Cart;
import ua.nure.salenko.model.Filter;
import ua.nure.salenko.model.Product;
import ua.nure.salenko.pagination.ProductPaginator;

public abstract class AbstractMarketController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AbstractMarketController.class);

    final ProductDao productDao = new ProductDaoImpl();
    final CartDao cartDao = new CartDaoImpl();

    protected Long listCount(Filter filter) throws SQLException {
        return productDao.getCount(filter);
    }

    @SuppressWarnings("unchecked")
    protected List<Product> findRow(int startPosition, int maxResults, String sortFields, String sortDirections,
            Filter filter) throws SQLException {
        return productDao.sortedFind(startPosition, maxResults, sortFields, sortDirections, filter);
    }

    protected ProductPaginator findRow(ProductPaginator wrapper, Filter filter) throws SQLException {
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
        HttpSession s = request.getSession();
        String action = request.getParameter("action");
        try {
            LOGGER.info("AbstractMarketController#doGet?action=" + action);
            switch (action) {
            case "start":
                startPage(request, response);
                break;
            default:
                listProduct(request, response);
                break;
            }
        } catch (SQLException e) {
            LOGGER.error("AbstractMarketController#doGet?SQLException", e);
            throw new ServletException(e);
        }
    }

    protected abstract void startPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException;

    protected void listProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        LOGGER.info("AbstractMarketController#listProduct");
        // if productId is empty - we got list of ten products, else - we got product by id
        if (request.getParameter("productId") == "") {
            Integer page;
            String sortFields;
            String sortDirections;

            Filter filter = new Filter();
            if (Boolean.parseBoolean(request.getParameter("thisIsFilter"))) {
                filter.setModel(request.getParameter("fmodel"));
                filter.setDealer(request.getParameter("fdealer"));
                filter.setType(request.getParameter("ftype"));
                filter.setSpecialParameter2(request.getParameter("fspecialParameter2"));
                if (request.getParameter("fworthMin") != null)
                    filter.setWorthMin(Long.parseLong(request.getParameter("fworthMin")));
                if (request.getParameter("fworthMax") != null)
                    filter.setWorthMax(Long.parseLong(request.getParameter("fworthMax")));
                if (request.getParameter("fwidthMin") != null)
                    filter.setWidthMin(Long.parseLong(request.getParameter("fwidthMin")));
                if (request.getParameter("fwidthMax") != null)
                    filter.setWidthMax(Long.parseLong(request.getParameter("fwidthMax")));
                if (request.getParameter("flengthMin") != null)
                    filter.setLengthMin(Long.parseLong(request.getParameter("flengthMin")));
                if (request.getParameter("flengthMax") != null)
                    filter.setLengthMax(Long.parseLong(request.getParameter("flengthMax")));
                if (request.getParameter("fheightMin") != null)
                    filter.setHeightMin(Long.parseLong(request.getParameter("fheightMin")));
                if (request.getParameter("fheightMax") != null)
                    filter.setHeightMax(Long.parseLong(request.getParameter("fheightMax")));
                if (request.getParameter("fspecialParameter1Min") != null)
                    filter.setSpecialParameter1Min(Long.parseLong(request.getParameter("fspecialParameter1Min")));
                if (request.getParameter("fspecialParameter1Max") != null)
                    filter.setSpecialParameter1Max(Long.parseLong(request.getParameter("fspecialParameter1Max")));
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

            ProductPaginator paginator = new ProductPaginator(page, sortFields, sortDirections, 10);
            request.setAttribute("list", findRow(paginator, filter).getList());
            response.setContentType("charset=UTF-8");
            String jsonList = RestUtils.makeJsonProductList(paginator.getCurrentPage(), paginator.getPageSize(),
                    paginator.getTotalResults(), paginator.getSortFields(), paginator.getSortDirections(),
                    paginator.getList());
            PrintWriter pw = response.getWriter();
            pw.println(jsonList);
        } else {
            Long id = Long.parseLong(request.getParameter("productId"));
            LOGGER.info("AbstractMarketController#getProduct?productId=" + id);
            try {
                Product p = productDao.getProductById(id);
                String jsonProduct = RestUtils.makeJsonProduct(p);
                PrintWriter pw = response.getWriter();
                pw.println(jsonProduct);
            } catch (NullPointerException | MySQLNonTransientConnectionException e) {
                LOGGER.error("AbstractMarketController#getProduct?error" + e.getStackTrace());
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
        LOGGER.info("AbstractMarketController#doPost?jsonRow=" + jsonRow);
        if (!jsonRow.contains("thisIsFilter")) {
            if (session.getAttribute("userId") == null || session.getAttribute("userId") == ""
                    || session.getAttribute("userId") == "null") {
                response.sendRedirect("AuthorizationController");
            } else {
                cart = RestUtils.makeCartItemFromJsonRow(jsonRow);
                Long userId = (Long) session.getAttribute("userId");
                cart.setUserId(userId);
                try {
                    cartDao.insert(cart);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

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

import ua.nure.salenko.model.Filter;
import ua.nure.salenko.model.Product;
import ua.nure.salenko.pagination.ProductPaginator;
import ua.nure.salenko.dao.ProductDao;
import ua.nure.salenko.dao.ProductDaoImpl;

public class PriceChangeController extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(PriceChangeController.class);

    final ProductDao dao = new ProductDaoImpl();

    private Long listCount(Filter filter) throws SQLException {
        return dao.getCount(filter);
    }

    @SuppressWarnings("unchecked")
    private List<Product> findRow(int startPosition, int maxResults, String sortFields, String sortDirections,
            Filter filter) throws SQLException {
        return dao.sortedFind(startPosition, maxResults, sortFields, sortDirections, filter);
    }

    private ProductPaginator findRow(ProductPaginator wrapper, Filter filter) throws SQLException {
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
                LOGGER.info("PriceChangeController#doGet?action=" + action);
                switch (action) {
                case "start":
                    startPage(request, response);
                    break;
                default:
                    listProduct(request, response);
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
        if (session.getAttribute("role") == null || session.getAttribute("role").equals("")) {
        } else
            request.setAttribute("logged", "true");
        RequestDispatcher dispatcher;
        if (session.getAttribute("locale").equals("ru"))
            dispatcher = request.getRequestDispatcher("PriceChangePage_ru.jsp");
        else
            dispatcher = request.getRequestDispatcher("PriceChangePage.jsp");
        dispatcher.forward(request, response);

    }

    private void listProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        LOGGER.info("PriceChangeController#listProduct");
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
            String jsonList = RestUtils.makeJsonProductList(paginator.getCurrentPage(), paginator.getPageSize(),
                    paginator.getTotalResults(), paginator.getSortFields(), paginator.getSortDirections(),
                    paginator.getList());
            PrintWriter pw = response.getWriter();
            pw.println(jsonList);
        } else {
            Long id = Long.parseLong(request.getParameter("productId"));
            try {
                Product p = dao.getProductById(id);
                String jsonProduct = RestUtils.makeJsonProduct(p);
                response.setHeader("Content-Type", "text/xml; charset=UTF-8");
                response.setCharacterEncoding("utf-8");
                PrintWriter pw = response.getWriter();
                pw.println(jsonProduct);
            } catch (NullPointerException | MySQLNonTransientConnectionException e) {
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jsonRow = null;
        Product product = null;
        BufferedReader reader = request.getReader();
        jsonRow = reader.readLine();
        if (!jsonRow.contains("thisIsFilter")) {
            product = RestUtils.makeProductFromJsonRow(jsonRow);
            try {
                if (product.getId() == null)
                    dao.insert(product);
                else
                    dao.update(product);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            dao.deleteById(Long.parseLong(request.getParameter("productId")));
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }

    }
}

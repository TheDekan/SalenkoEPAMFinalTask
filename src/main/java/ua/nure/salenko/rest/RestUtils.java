package ua.nure.salenko.rest;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ua.nure.salenko.model.Cart;
import ua.nure.salenko.model.Deal;
import ua.nure.salenko.model.DealItem;
import ua.nure.salenko.model.Product;
import ua.nure.salenko.model.User;

public class RestUtils {
    
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    //16-16-39 hh-mm-ss
    static void setLocale(HttpServletRequest request) {
        HttpSession s = request.getSession();
        
        if (request.getParameter("locale") != null) {
        if (request.getParameter("locale").equals("ru"))
            s.setAttribute("locale", "ru");
        else
            s.setAttribute("locale", "eng");
        }
        else {
            if (s.getAttribute("locale") == null)
                s.setAttribute("locale", "");
        }
    }

    static String makeJsonProductList(Integer currentPage, Integer pageSize, Long totalResults, String sortFields,
            String sortDirections, List<Product> productList) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"currentPage\":" + currentPage + ",\"pageSize\":" + pageSize + ",\"totalResults\":" + totalResults
                + ",\"sortFields\":\"" + sortFields + "\",\"sortDirections\":\"" + sortDirections + "\",\"list\":[ ");
        for (Product p : productList)
            sb.append("{\"id\":" + p.getId() + ",\"model\":\"" + p.getModel() + "\",\"dealer\":\"" + p.getDealer()
                    + "\",\"type\":\"" + p.getType() + "\",\"worth\":" + p.getWorth() + ",\"length\":" + p.getLength()
                    + ",\"width\":" + p.getWidth() + ",\"height\":" + p.getHeight() + ",\"specialParameter1\":"
                    + p.getSpecialParameter1() + ",\"specialParameter2\":\"" + p.getSpecialParameter2()
                    + "\",\"imageURL1\":\"" + p.getImageURL1() + "\",\"imageURL2\":\"" + p.getImageURL2() + "\"},");
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        return sb.toString();
    }

    static String makeJsonProduct(Product p) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":" + p.getId() + ",\"model\":\"" + p.getModel() + "\",\"dealer\":\"" + p.getDealer()
                + "\",\"type\":\"" + p.getType() + "\",\"worth\":" + p.getWorth() + ",\"length\":" + p.getLength()
                + ",\"width\":" + p.getWidth() + ",\"height\":" + p.getHeight() + ",\"specialParameter1\":"
                + p.getSpecialParameter1() + ",\"specialParameter2\":\"" + p.getSpecialParameter2()
                + "\",\"imageURL1\":\"" + p.getImageURL1() + "\",\"imageURL2\":\"" + p.getImageURL2() + "\"}");
        return sb.toString();
    }

    static Product makeProductFromJsonRow(String row) {
        Product p = new Product();
        String[] arguments = row.split("\":|,");
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].replaceAll("[\"}{]", "");
        }
        for (int i = 0; i < arguments.length - 1; i += 2) {
            if (arguments[i].equals("id")) {
                p.setId(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("model")) {
                p.setModel(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("dealer")) {
                p.setDealer(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("type")) {
                p.setType(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("worth")) {
                p.setWorth(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("length")) {
                p.setLength(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("width")) {
                p.setWidth(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("height")) {
                p.setHeight(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("specialParameter1")) {
                p.setSpecialParameter1(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("specialParameter2")) {
                p.setSpecialParameter2(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("imageURL1")) {
                p.setImageURL1(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("imageURL2")) {
                p.setImageURL2(arguments[i + 1]);
                continue;
            }
        }
        return p;
    }

    static Long getCount(String row) {
        String[] arguments = row.split("\":|,");
        Long count = null;
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].replaceAll("[\"}{]", "");
        }
        for (int i = 0; i < arguments.length - 1; i += 2) {
            if (arguments[i].equals("count")) {
                count = Long.parseLong(arguments[i + 1]);
                break;
            }
        }
        return count;
    }

    static Cart makeCartItemFromJsonRow(String row) {
        Cart cart = new Cart();
        String[] arguments = row.split("\":|,");
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].replaceAll("[\"}{]", "");
        }
        for (int i = 0; i < arguments.length - 1; i += 2) {
            if (arguments[i].equals("count")) {
                cart.setCount(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("id")) {
                if (!row.contains("productId"))
                    cart.setProductId(Long.parseLong(arguments[i + 1]));
                else
                    cart.setId(Long.parseLong(arguments[i + 1]));
                continue;
            }
        }
        return cart;
    }

    static String makeJsonCartList(Integer currentPage, Integer pageSize, Long totalResults, String sortFields,
            String sortDirections, List<Cart> cartList) {
        Long checkWorth = 0L;
        StringBuilder sb = new StringBuilder();
        sb.append("{\"currentPage\":" + currentPage + ",\"pageSize\":" + pageSize + ",\"totalResults\":" + totalResults
                + ",\"sortFields\":\"" + sortFields + "\",\"sortDirections\":\"" + sortDirections + "\",\"list\":[ ");
        for (Cart c : cartList) {
            sb.append("{\"id\":" + c.getId() + ",\"userId\":" + c.getUserId() + ",\"productId\":" + c.getProductId()
                    + ",\"productRow\":\"" + c.getProductRow() + "\",\"count\":" + c.getCount() + ",\"totalWorth\":"
                    + c.getTotalWorth() + ",\"worthPerItem\":" + c.getWorthPerItem() + "},");
            checkWorth += c.getTotalWorth();
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]" + ",\"check\":" + checkWorth + "}");
        return sb.toString();
    }

    static String makeJsonCart(Cart c) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"id\":" + c.getId() + ",\"userId\":" + c.getUserId() + ",\"productId\":" + c.getProductId()
                + ",\"productRow\":\"" + c.getProductRow() + "\",\"count\":" + c.getCount() + ",\"totalWorth\":"
                + c.getTotalWorth() + ",\"worthPerItem\":" + c.getWorthPerItem() + "}");
        return sb.toString();
    }

    static Deal makeDealFromJsonRow(String row) {
        Deal deal = new Deal();
        String[] arguments = row.split("\":|,");
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].replaceAll("[\"}{]", "");
        }
        for (int i = 0; i < arguments.length - 1; i += 2) {
            if (arguments[i].equals("id")) {
                deal.setId(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("status")) {
                deal.setStatus(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("worth")) {
                deal.setWorth(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("userId")) {
                deal.setUserId(Long.parseLong(arguments[i + 1]));
                continue;
            }
        }
        return deal;
    }

    static String makeJsonDealList(Integer currentPage, Integer pageSize, Long totalResults, String sortFields,
            String sortDirections, List<Deal> dealList) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"currentPage\":" + currentPage + ",\"pageSize\":" + pageSize + ",\"totalResults\":" + totalResults
                + ",\"sortFields\":\"" + sortFields + "\",\"sortDirections\":\"" + sortDirections + "\",\"list\":[ ");
        for (Deal d : dealList) {
            SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT);
            String date = format1.format(d.getSendDate());
            sb.append("{\"id\":" + d.getId() + ",\"userId\":" + d.getUserId() + ",\"worth\":" + d.getWorth()
                    + ",\"status\":\"" + d.getStatus() + "\",\"sendDate\":\"" + java.sql.Date.valueOf(date)
                    + "\",\"userName\":\"" + d.getUserName() + "\"},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        return sb.toString();
    }

    static String makeJsonDeal(Deal d, List<DealItem> dList) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT);
        String date = format1.format(d.getSendDate());
        sb.append("{\"id\":" + d.getId() + ",\"userId\":" + d.getUserId() + ",\"worth\":" + d.getWorth()
                + ",\"status\":\"" + d.getStatus() + "\",\"sendDate\":\"" + java.sql.Date.valueOf(date)
                + "\",\"userName\":\"" + d.getUserName() + "\",\"list\":[ ");
        for (DealItem dItem : dList)
            sb.append("{\"id\":" + dItem.getId() + ",\"dealId\":" + dItem.getDealId() + ",\"worth\":" + dItem.getWorth()
                    + ",\"productCount\":" + dItem.getProductCount() + ",\"productRow\":\"" + dItem.getProductRow()
                    + "\"},");

        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        return sb.toString();
    }

    public static String makeJsonUserList(Integer currentPage, Integer pageSize, Long totalResults, String sortFields,
            String sortDirections, List<User> userList) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"currentPage\":" + currentPage + ",\"pageSize\":" + pageSize + ",\"totalResults\":" + totalResults
                + ",\"sortFields\":\"" + sortFields + "\",\"sortDirections\":\"" + sortDirections + "\",\"list\":[ ");
        for (User u : userList) {
            SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT);
            String date = format1.format(u.getJoinDate());
            sb.append("{\"id\":" + u.getId() + ",\"name\":\"" + u.getName() + "\",\"role\":\"" + u.getRole()
                    + "\",\"blocked\":\"" + u.getBlocked() + "\",\"joinDate\":\"" + java.sql.Date.valueOf(date)
                    + "\"},");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");
        return sb.toString();
    }

    public static String makeJsonUser(User u) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT);
        String date = format1.format(u.getJoinDate());
        sb.append("{\"id\":" + u.getId() + ",\"name\":\"" + u.getName() + "\",\"role\":\"" + u.getRole()
                + "\",\"blocked\":\"" + u.getBlocked() + "\",\"joinDate\":\"" + java.sql.Date.valueOf(date) + "\"}");
        return sb.toString();
    }

    public static User makeUserFromJsonRow(String row) {
        User user = new User();
        String[] arguments = row.split("\":|,");
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].replaceAll("[\"}{]", "");
        }
        for (int i = 0; i < arguments.length - 1; i += 2) {
            if (arguments[i].equals("id")) {
                user.setId(Long.parseLong(arguments[i + 1]));
                continue;
            }
            if (arguments[i].equals("role")) {
                user.setRole(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("name")) {
                user.setName(arguments[i + 1]);
                continue;
            }
            if (arguments[i].equals("blocked")) {
                user.setBlocked(Boolean.parseBoolean(arguments[i + 1]));
                continue;
            }
        }
        return user;
    }

}

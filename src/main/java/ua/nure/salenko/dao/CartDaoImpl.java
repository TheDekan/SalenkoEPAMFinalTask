package ua.nure.salenko.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.salenko.model.Cart;

public class CartDaoImpl extends AbstractDao implements CartDao {

    private static final Logger LOGGER = Logger.getLogger(CartDaoImpl.class);

    private static final String INSERT_CART = "INSERT INTO cart (userId, productId, count) VALUES (?, ?, ?)";

    private static final String LIST_ALL_CARTS = "SELECT p.*, c.worth, c.model, c.dealer, c.type FROM cart p inner join product c WHERE p.productId = c.id AND userId = ?";

    private static final String DELETE_CART = "DELETE FROM cart WHERE id = ?";

    private static final String UPDATE_CART_COUNT = "UPDATE cart SET count = ? WHERE id = ?";

    private static final String GET_CART_BY_ID = "SELECT p.id, p.userId, p.productId, p.count, c.model, c.dealer, c.type, c.worth FROM cart p inner join product c WHERE p.productID = c.id AND p.id = ?";

    private static final String GET_CART_COUNT = "SELECT count(*) FROM cart WHERE userId = ?";

    private static final String CHECK_CLIENT_ALREADY_GOT_PRODUCT = "SELECT * FROM cart WHERE userId = ? AND productID = ?";

    private static final String SORTED_FIND = "SELECT p.id, p.userId, p.productId, p.count, c.model, c.dealer, c.type, c.worth FROM cart p  inner join product c WHERE p.productId = c.id AND p.userId = ? ORDER BY ";

    @Override
    public boolean insert(Cart cart) throws SQLException {
        connect();

        boolean existed = false;
        PreparedStatement statement = jdbcConnection.prepareStatement(CHECK_CLIENT_ALREADY_GOT_PRODUCT);
        statement.setLong(1, cart.getUserId());
        statement.setLong(2, cart.getProductId());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            Long count = resultSet.getLong("count");
            Long id = resultSet.getLong("id");
            cart.setCount(cart.getCount() + count);
            cart.setId(id);
            existed = true;
        }

        statement.close();

        boolean rowInserted = false;
        if (!existed) {
            PreparedStatement statement2 = jdbcConnection.prepareStatement(INSERT_CART);
            statement2.setLong(1, cart.getUserId());
            statement2.setLong(2, cart.getProductId());
            statement2.setLong(3, cart.getCount());

            LOGGER.info("CartDaoImpl#insertCart?userId=" + cart.getUserId() + "&productId=" + cart.getProductId());
            rowInserted = statement2.executeUpdate() > 0;
            statement2.close();
            disconnect();
        } else
            rowInserted = this.update(cart);
        return rowInserted;
    }

    @Override
    public List<Cart> listAllByUserId(Long userId) throws SQLException {
        List<Cart> listCart = new ArrayList<>();
        LOGGER.info("CartDaoImpl#listAllByUserId?userId=" + userId);

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(LIST_ALL_CARTS);
        statement.setLong(1, userId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            Long productId = resultSet.getLong("productId");
            Long count = resultSet.getLong("count");
            Long worth = resultSet.getLong("worth");
            Long totalWorth = worth * count;
            String productRow = resultSet.getString("type") + " " + resultSet.getString("dealer") + " "
                    + resultSet.getString("model");

            Cart cart = this.buildCart(id, userId, productId, count, totalWorth, productRow, worth);
            listCart.add(cart);
        }

        resultSet.close();
        statement.close();

        disconnect();

        return listCart;
    }

    @Override
    public boolean delete(Cart cart) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(DELETE_CART);
        statement.setLong(1, cart.getId());

        LOGGER.info("CartDaoImpl#deleteCart?id=" + cart.getId());
        boolean rowDeleted = statement.executeUpdate() > 0;

        statement.close();
        disconnect();
        return rowDeleted;
    }

    @Override
    public boolean deleteCartById(Long id) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(DELETE_CART);
        statement.setLong(1, id);

        LOGGER.info("CartDaoImpl#deleteCart?id=" + id);
        boolean rowDeleted = statement.executeUpdate() > 0;

        statement.close();
        disconnect();
        return rowDeleted;
    }

    @Override
    public boolean update(Cart cart) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(UPDATE_CART_COUNT);
        statement.setLong(1, cart.getCount());
        statement.setLong(2, cart.getId());

        LOGGER.info("CartDaoImpl#updateCart?&id=" + cart.getId());
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    @Override
    public Cart getCartById(Long id) throws SQLException {
        Cart cart = null;

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_CART_BY_ID);
        statement.setLong(1, id);

        LOGGER.info("CartDaoImpl#getCartById?id=" + id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Long userId = resultSet.getLong("userId");
            Long productId = resultSet.getLong("productId");
            Long count = resultSet.getLong("count");
            Long worthPerItem = resultSet.getLong("worth");
            Long totalWorth = worthPerItem * count;
            String productRow = resultSet.getString("type") + " " + resultSet.getString("dealer") + " "
                    + resultSet.getString("model");

            cart = this.buildCart(id, userId, productId, count, totalWorth, productRow, worthPerItem);
        }

        resultSet.close();
        statement.close();
        disconnect();
        return cart;
    }

    @Override
    public List<Cart> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections,
            Long userId) throws SQLException {
        connect();

        LOGGER.info("CartDaoImpl#sortedFind?userId=" + userId + "&startPosition=" + startPosition + "&maxResults="
                + maxResults + "&sortFields=" + sortFields + "&sortDirections=" + sortDirections);

        List<Cart> listCart = new ArrayList<>();
        if (sortFields.equals("productRow"))
            sortFields = "c.type";
        else if (sortFields.equals("worthPerItem"))
            sortFields = "c.worth";
        PreparedStatement statement = jdbcConnection.prepareStatement(SORTED_FIND + sortFields + " " + sortDirections);
        //PreparedStatement statement = jdbcConnection.prepareStatement(String.format("%s%s %s", SORTED_FIND, sortFields, sortDirections));
        statement.setLong(1, userId);
        ResultSet resultSet = statement.executeQuery();
        int counter = 0;
        while (counter < startPosition) {
            resultSet.next();
            counter++;
        }
        while (resultSet.next() && counter < maxResults + startPosition) {
            Long id = resultSet.getLong("id");
            Long productId = resultSet.getLong("productId");
            Long count = resultSet.getLong("count");
            Long worthPerItem = resultSet.getLong("worth");
            Long totalWorth = worthPerItem * count;
            String productRow = resultSet.getString("type") 
                        + " " + resultSet.getString("dealer") 
                        + " " + resultSet.getString("model");

            Cart cart = this.buildCart(id, userId, productId, count, totalWorth, productRow, worthPerItem);
            listCart.add(cart);
            counter++;
        }

        return listCart;
    }

    private Cart buildCart(Long id, Long userId, Long productId, Long count, Long totalWorth, String productRow,
            Long worthPerItem) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserId(userId);
        cart.setProductId(productId);
        cart.setCount(count);
        cart.setTotalWorth(totalWorth);
        cart.setProductRow(productRow);
        cart.setWorthPerItem(worthPerItem);
        return cart;
    }

    @Override
    public Long getCount(Long userId) throws SQLException {
        connect();

        LOGGER.info("CartDaoImpl#getCount?userId=" + userId);

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_CART_COUNT);
        statement.setLong(1, userId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getLong(1);
        }
        return 0L;
    }

}

package ua.nure.salenko.dao;

import java.sql.SQLException;
import java.util.List;

import ua.nure.salenko.model.Cart;

public interface CartDao {

    boolean insert(Cart cart) throws SQLException;

    List<Cart> listAllByUserId(Long userId) throws SQLException;

    boolean delete(Cart cart) throws SQLException;

    boolean deleteCartById(Long id) throws SQLException;

    boolean update(Cart cart) throws SQLException;

    Cart getCartById(Long id) throws SQLException;

    Long getCount(Long userId) throws SQLException;

    List<Cart> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections, Long id)
            throws SQLException;

}

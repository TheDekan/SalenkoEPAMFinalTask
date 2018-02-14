package ua.nure.salenko.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import ua.nure.salenko.model.Cart;
import ua.nure.salenko.model.Deal;
import ua.nure.salenko.model.DealItem;

public interface DealDao {

    boolean setGroupsForUser(Deal deal, List<Cart> carts) throws SQLException;

    boolean insert(Deal deal) throws SQLException;

    boolean update(Deal deal) throws SQLException;

    Deal getDealById(Long id) throws SQLException;

    Long getCount(Long userId, String filter, String filter2) throws SQLException;

    List<Deal> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections, String filter, String filter2)
            throws SQLException;

    List<Deal> sortedFindByUserId(int startPosition, int maxResults, String sortFields, String sortDirections,
            Long userId, boolean admin, String filter, String filter2) throws SQLException;

    Deal getDealByProperties(Date date, Long worth, Long userId) throws SQLException;

    List<DealItem> getListItems(Long dealId) throws SQLException;

}

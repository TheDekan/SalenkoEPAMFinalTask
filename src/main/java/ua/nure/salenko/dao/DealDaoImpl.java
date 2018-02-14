package ua.nure.salenko.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.salenko.model.Cart;
import ua.nure.salenko.model.Deal;
import ua.nure.salenko.model.DealItem;
import ua.nure.salenko.rest.RestUtils;

public class DealDaoImpl extends AbstractDao implements DealDao {

    private static final Logger LOGGER = Logger.getLogger(DealDaoImpl.class);

    private static final String INSERT_INTO_DEALLIST = "INSERT INTO deallist (dealId, productRow, productCount, worth) VALUES (?, ?, ?, ?)";

    private static final String INSERT_DEAL = "INSERT INTO deal (userId, worth, status, sendDate) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_DEAL = "UPDATE deal SET status = ? WHERE id = ?";

    private static final String GET_DEAL_BY_ID = "SELECT p.id, p.userId, p.worth, p.sendDate, p.status, c.productRow, c.productCount, c.worth FROM deal p inner join deallist c WHERE p.id = c.dealId AND p.id = ?";

    private static final String GET_DEAL_BY_SEND_DATE = "SELECT * FROM deal WHERE sendDate = ? AND worth = ? AND userId = ?";

    private static final String GET_DEAL_COUNT_FROM_ADMIN = "SELECT count(*) FROM deal p inner join user c WHERE p.userId = c.id AND c.name LIKE ?";

    private static final String GET_DEAL_COUNT = "SELECT count(*) FROM deal p inner join user c WHERE p.userId = ? AND p.userId = c.id AND c.name LIKE ?";

    private static final String SORTED_FIND_FOR_USER = "SELECT p.id, p.userId, p.worth, p.sendDate, p.status, c.name FROM deal p inner join user c WHERE p.userId = c.id AND p.userId = ? ORDER BY ";

    private static final String SORTED_FIND_FOR_ADMIN = "SELECT p.id, p.userId, p.worth, p.sendDate, p.status, c.name FROM deal p inner join user c WHERE p.userId = c.id AND c.name LIKE ? ORDER BY ";

    private static final String GET_DEALLIST_BY_DEAL_ID = "SELECT * FROM deallist WHERE dealId = ?";

    private static final String GET_DEALLIST_BY_DEAL_ID_FILTERED = "SELECT * FROM deallist WHERE dealId = ? AND productRow LIKE ?";

    @Override
    public boolean setGroupsForUser(Deal deal, List<Cart> carts) throws SQLException {
        boolean success = true;
        if (carts == null || carts.size() == 0)
            return false;
        connect();
        // setAutoCommitOff();
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        try {
            statement1 = jdbcConnection.prepareStatement(INSERT_DEAL);
            statement1.setLong(1, deal.getUserId());
            statement1.setLong(2, deal.getWorth());
            statement1.setString(3, deal.getStatus());
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String a = format1.format(deal.getSendDate());
            statement1.setDate(4, java.sql.Date.valueOf(a));
            // commit();
            statement1.executeUpdate();
            statement1.close();
            disconnect();

            deal = getDealByProperties(deal.getSendDate(), deal.getWorth(), deal.getUserId());
            connect();
            setAutoCommitOff();
            statement2 = jdbcConnection.prepareStatement(INSERT_INTO_DEALLIST);
            statement2.setLong(1, deal.getId());
            for (Cart c : carts) {
                statement2.setString(2, c.getProductRow());
                statement2.setLong(3, c.getCount());
                statement2.setLong(4, c.getTotalWorth());
                commit();
                statement2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
            rollback();
        } finally {
            setAutoCommitOn();
            statement1.close();
            statement2.close();
            disconnect();
        }
        return success;
    }

    @Override
    public boolean insert(Deal deal) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(INSERT_DEAL);
        statement.setLong(1, deal.getUserId());
        statement.setLong(2, deal.getWorth());
        statement.setString(3, deal.getStatus());
        SimpleDateFormat format1 = new SimpleDateFormat(RestUtils.DATE_FORMAT);
        String a = format1.format(deal.getSendDate());
        statement.setDate(4, java.sql.Date.valueOf(a));

        LOGGER.info("DealDaoImpl#insertDeal?userId=" + deal.getUserId());
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    @Override
    public boolean update(Deal deal) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(UPDATE_DEAL);
        statement.setString(1, deal.getStatus());
        statement.setLong(2, deal.getId());

        LOGGER.info("DealDaoImpl#updateDeal?&id=" + deal.getId());
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    @Override
    public Deal getDealById(Long id) throws SQLException {
        Deal deal = null;

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_DEAL_BY_ID);
        statement.setLong(1, id);

        LOGGER.info("DealDaoImpl#getDealById?id=" + id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Long userId = resultSet.getLong("userId");
            Long worth = resultSet.getLong("worth");
            Date sendDate = resultSet.getDate("sendDate");
            String status = resultSet.getString("status");

            deal = this.buildDeal(id, userId, worth, sendDate, status, "");
        }

        resultSet.close();
        statement.close();
        disconnect();
        return deal;
    }

    @Override
    public Deal getDealByProperties(Date date, Long worth, Long userId) throws SQLException {
        Deal deal = null;

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_DEAL_BY_SEND_DATE);
        SimpleDateFormat format1 = new SimpleDateFormat(RestUtils.DATE_FORMAT);
        String a = format1.format(date);
        statement.setDate(1, java.sql.Date.valueOf(a));
        statement.setLong(2, worth);
        statement.setLong(3, userId);

        LOGGER.info("DealDaoImpl#getDealBySendDate?date=" + a);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Long id = resultSet.getLong("id");
            Date sendDate = resultSet.getDate("sendDate");
            String status = resultSet.getString("status");

            deal = this.buildDeal(id, userId, worth, sendDate, status, "");
        }

        resultSet.close();
        statement.close();
        disconnect();
        return deal;
    }

    @Override
    public Long getCount(Long userId, String filter, String filter2) throws SQLException {
        connect();
        PreparedStatement statement = null;
        if (userId != 0) {
            statement = jdbcConnection.prepareStatement(GET_DEAL_COUNT);
            LOGGER.info("DealDaoImpl#getCount?userId=" + userId + "&filter=" + filter);
            statement.setLong(1, userId);
            if (filter != null && filter != "")
                statement.setString(2, "%" + filter + "%");
            else
                statement.setString(2, "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } else {
            statement = jdbcConnection.prepareStatement(GET_DEAL_COUNT_FROM_ADMIN);
            LOGGER.info("DealDaoImpl#getCount?asAdmin&filter=" + filter);
            if (filter != null && filter != "")
                statement.setString(1, "%" + filter + "%");
            else
                statement.setString(1, "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return 0L;
    }

    @Override
    public List<Deal> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections,
            String filter, String filter2) throws SQLException {
        connect();

        LOGGER.info("CartDaoImpl#sortedFind?filter=" + filter + "&startPosition=" + startPosition + "&maxResults="
                + maxResults + "&sortFields=" + sortFields + "&sortDirections=" + sortDirections);

        List<Deal> listDeal = new ArrayList<>();
        PreparedStatement statement = jdbcConnection
                .prepareStatement(SORTED_FIND_FOR_ADMIN + sortFields + " " + sortDirections);
        if (filter != null && filter != "")
            statement.setString(1, "%" + filter + "%");
        else
            statement.setString(1, "%");
        ResultSet resultSet = statement.executeQuery();
        int counter = 0;
        while (counter < startPosition) {
            resultSet.next();
            Long id = resultSet.getLong("id");
            List<DealItem> dList = getListItemsFiltered(id, filter2);
            if (dList.size() > 0) 
            counter++;
        }
        while (resultSet.next() && counter < maxResults + startPosition) {
            Long id = resultSet.getLong("id");
            List<DealItem> dList = getListItemsFiltered(id, filter2);
            if (dList.size() > 0) {
                Long userId = resultSet.getLong("userId");
                Long worth = resultSet.getLong("worth");
                Date sendDate = resultSet.getDate("sendDate");
                String status = resultSet.getString("status");
                String userName = resultSet.getString("name");

                Deal deal = this.buildDeal(id, userId, worth, sendDate, status, userName);
                listDeal.add(deal);
                counter++;
            }
        }

        return listDeal;
    }

    @Override
    public List<Deal> sortedFindByUserId(int startPosition, int maxResults, String sortFields, String sortDirections,
            Long userId, boolean admin, String filter, String filter2) throws SQLException {
        if (sortFields.equals("userName")) sortFields = "c.name";
        if (admin) {
            return sortedFind(startPosition, maxResults, sortFields, sortDirections, filter, filter2);
        } else {
            connect();

            LOGGER.info("CartDaoImpl#sortedFindByUserId?filter=" + filter + "&startPosition=" + startPosition
                    + "&maxResults=" + maxResults + "&sortFields=" + sortFields + "&sortDirections=" + sortDirections
                    + "&userId=" + userId);

            List<Deal> listDeal = new ArrayList<>();
            PreparedStatement statement = jdbcConnection
                    .prepareStatement(SORTED_FIND_FOR_USER + sortFields + " " + sortDirections);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            int counter = 0;
            while (counter < startPosition) {
                resultSet.next();
                Long id = resultSet.getLong("id");
                List<DealItem> dList = getListItemsFiltered(id, filter2);
                if (dList.size() > 0) 
                    counter++;
            }
            while (resultSet.next() && counter < maxResults + startPosition) {
                Long id = resultSet.getLong("id");
                List<DealItem> dList = getListItemsFiltered(id, filter2);
                if (dList.size() > 0) {
                    Long worth = resultSet.getLong("worth");
                    Date sendDate = resultSet.getDate("sendDate");
                    String status = resultSet.getString("status");
                    String userName = resultSet.getString("name");

                    Deal deal = this.buildDeal(id, userId, worth, sendDate, status, userName);
                    listDeal.add(deal);
                    counter++;
                }
            }

            return listDeal;
        }
    }

    public List<DealItem> getListItems(Long dealId) throws SQLException {
        connect();

        LOGGER.info("CartDaoImpl#getListItems?dealId=" + dealId);

        List<DealItem> listDealItem = new ArrayList<>();
        PreparedStatement statement = jdbcConnection.prepareStatement(GET_DEALLIST_BY_DEAL_ID);
        statement.setLong(1, dealId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            Long productCount = resultSet.getLong("productCount");
            Long worth = resultSet.getLong("worth");
            String productRow = resultSet.getString("productRow");

            DealItem dealItem = this.buildDealItem(id, dealId, productCount, worth, productRow);
            listDealItem.add(dealItem);
        }

        return listDealItem;
    }

    private List<DealItem> getListItemsFiltered(Long dealId, String filter) throws SQLException {
        connect();

        LOGGER.info("CartDaoImpl#getListItems?dealId=" + dealId);

        List<DealItem> listDealItem = new ArrayList<>();
        PreparedStatement statement = jdbcConnection.prepareStatement(GET_DEALLIST_BY_DEAL_ID_FILTERED);
        statement.setLong(1, dealId);
        if (filter != null && filter != "")
            statement.setString(2, "%" + filter + "%");
        else
            statement.setString(2, "%");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            Long productCount = resultSet.getLong("productCount");
            Long worth = resultSet.getLong("worth");
            String productRow = resultSet.getString("productRow");

            DealItem dealItem = this.buildDealItem(id, dealId, productCount, worth, productRow);
            listDealItem.add(dealItem);
        }

        return listDealItem;
    }

    private Deal buildDeal(Long id, Long userId, Long worth, Date sendDate, String status, String userName) {
        Deal deal = new Deal();
        deal.setId(id);
        deal.setUserId(userId);
        deal.setWorth(worth);
        deal.setStatus(status);
        deal.setSendDate(sendDate);
        deal.setUserName(userName);
        return deal;
    }

    private DealItem buildDealItem(Long id, Long dealId, Long productCount, Long worth, String productRow) {
        DealItem dealItem = new DealItem();
        dealItem.setId(id);
        dealItem.setDealId(dealId);
        dealItem.setProductCount(productCount);
        dealItem.setWorth(worth);
        dealItem.setProductRow(productRow);
        return dealItem;
    }

}

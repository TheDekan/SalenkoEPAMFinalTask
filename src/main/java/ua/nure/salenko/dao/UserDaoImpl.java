package ua.nure.salenko.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.salenko.model.User;
import ua.nure.salenko.rest.RestUtils;

public class UserDaoImpl extends AbstractDao implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

    private static final String INSERT_USER = "INSERT INTO user (name, password, role, joindate, blocked) VALUES (?, ?, ?, ?, ?)";

    private static final String LIST_ALL_USERS = "SELECT id, name, role, joinDate, blocked FROM user";

    private static final String DELETE_USER = "DELETE FROM user WHERE id = ?";

    //private static final String UPDATE_USER_PASSWORD = "UPDATE user SET password = ? WHERE id = ?";

    private static final String UPDATE_USER_STATUS = "UPDATE user SET blocked = ? WHERE id = ?";

    private static final String GET_USER_BY_ID = "SELECT * FROM user WHERE id = ?";

    private static final String GET_USER_BY_NAME = "SELECT * FROM user WHERE name = ?";

    private static final String GET_COUNT = "SELECT count(*) FROM user WHERE name LIKE ?";

    private static final String SORTED_FIND = "SELECT id, name, role, joinDate, blocked FROM user WHERE name LIKE ? ORDER BY ";

    public boolean insert(User user) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(INSERT_USER);
        statement.setString(1, user.getName());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getRole());
        SimpleDateFormat format1 = new SimpleDateFormat(RestUtils.DATE_FORMAT);
        String a = format1.format(user.getJoinDate());
        statement.setDate(4, java.sql.Date.valueOf(a));
        statement.setLong(5, user.getBlocked() ? 1 : 0);

        LOGGER.info("UserDaoImpl#insertUser?name=" + user.getName());
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public List<User> listAll() throws SQLException {
        List<User> listUser = new ArrayList<User>();
        LOGGER.info("UserDaoImpl#listAllUsers");

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(LIST_ALL_USERS);

        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String role = resultSet.getString("role");
            Date joinDate = resultSet.getDate("joindate");
            Long blocked = resultSet.getLong("blocked");

            User user = this.buildUser(id, name, role, joinDate, blocked);
            listUser.add(user);
        }

        resultSet.close();
        statement.close();

        disconnect();

        return listUser;
    }

    public boolean delete(User user) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(DELETE_USER);
        statement.setLong(1, user.getId());

        LOGGER.info("UserDaoImpl#deleteUser?id=" + user.getId());
        boolean rowDeleted = statement.executeUpdate() > 0;

        statement.close();
        disconnect();
        return rowDeleted;
    }

    public boolean update(User user) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(UPDATE_USER_STATUS);
        statement.setLong(1, user.getBlocked() ? 1 : 0);
        statement.setLong(2, user.getId());

        LOGGER.info("UserDaoImpl#updateUserPassword?&id=" + user.getId());
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    public User getUserById(Long id) throws SQLException {
        User user = null;

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_USER_BY_ID);
        statement.setLong(1, id);

        LOGGER.info("UserDaoImpl#getUserById?id=" + id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String name = resultSet.getString("name");
            String password = resultSet.getString("password");
            String role = resultSet.getString("role");
            Date joinDate = resultSet.getDate("joindate");
            Long blocked = resultSet.getLong("blocked");

            user = this.buildUser(id, name, role, joinDate, blocked);
            user.setPassword(password);
        }

        resultSet.close();
        statement.close();
        disconnect();

        return user;
    }

    public User getUserByName(String name) throws SQLException {
        User user = null;

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_USER_BY_NAME);
        statement.setString(1, name);

        LOGGER.info("UserDaoImpl#getUserByName?name=" + name);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {

            Long id = resultSet.getLong("id");
            String password = resultSet.getString("password");
            String role = resultSet.getString("role");
            Date joinDate = resultSet.getDate("joindate");
            Long blocked = resultSet.getLong("blocked");

            user = this.buildUser(id, name, role, joinDate, blocked);
            user.setPassword(password);
        }

        resultSet.close();
        statement.close();
        disconnect();

        return user;
    }

    private User buildUser(Long id, String name, String role, Date joinDate, Long blocked) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setRole(role);
        user.setJoinDate(joinDate);
        user.setBlocked(blocked == 1);
        return user;
    }

    @Override
    public Long getCount(String filter) throws SQLException {
        connect();

        LOGGER.info("UserDaoImpl#getCount?filter=" + filter);

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_COUNT);
        if (filter != null && filter != "")
            statement.setString(1, "%" + filter + "%");
        else
            statement.setString(1, "%");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getLong(1);
        }
        return 0L;
    }

    @Override
    public List<User> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections,
            String filter) throws SQLException {
        connect();

        LOGGER.info("UserDaoImpl#sortedFind?startPosition=" + startPosition + "&maxResults=" + maxResults
                + "&sortFields=" + sortFields + "&sortDirections=" + sortDirections + "&filter=" + filter);

        List<User> listUser = new ArrayList<>();
        PreparedStatement statement = jdbcConnection.prepareStatement(SORTED_FIND + sortFields + " " + sortDirections);
        if (filter != null && filter != "")
            statement.setString(1, "%" + filter + "%");
        else
            statement.setString(1, "%");
        ResultSet resultSet = statement.executeQuery();
        int counter = 0;
        while (counter < startPosition) {
            resultSet.next();
            counter++;
        }
        while (resultSet.next() && counter < maxResults + startPosition) {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String role = resultSet.getString("role");
            Date joinDate = resultSet.getDate("joindate");
            Long blocked = resultSet.getLong("blocked");

            User user = this.buildUser(id, name, role, joinDate, blocked);
            listUser.add(user);
            counter++;
        }

        return listUser;
    }

}

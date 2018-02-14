package ua.nure.salenko.dao;

import java.sql.SQLException;
import java.util.List;

import ua.nure.salenko.model.User;

public interface UserDao {

    boolean insert(User user) throws SQLException;

    List<User> listAll() throws SQLException;

    boolean delete(User user) throws SQLException;

    boolean update(User user) throws SQLException;

    User getUserById(Long id) throws SQLException;

    User getUserByName(String name) throws SQLException;

    Long getCount(String filter) throws SQLException;

    List<User> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections, String filter)
            throws SQLException;

}

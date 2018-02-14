package ua.nure.salenko.dao;

import java.sql.SQLException;
import java.util.List;

import ua.nure.salenko.model.Filter;
import ua.nure.salenko.model.Product;

public interface ProductDao {

    boolean insert(Product product) throws SQLException;

    List<Product> listAll() throws SQLException;

    boolean delete(Product product) throws SQLException;

    boolean deleteById(Long id) throws SQLException;

    boolean update(Product product) throws SQLException;

    Product getProductById(Long id) throws SQLException;

    long getCount(Filter f) throws SQLException;

    List<Product> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections, Filter f)
            throws SQLException;

}

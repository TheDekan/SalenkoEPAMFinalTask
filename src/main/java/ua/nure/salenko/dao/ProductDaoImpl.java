package ua.nure.salenko.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ua.nure.salenko.model.Filter;
import ua.nure.salenko.model.Product;

public class ProductDaoImpl extends AbstractDao implements ProductDao {

    private static final Logger LOGGER = Logger.getLogger(ProductDaoImpl.class);

    private static final String INSERT_PRODUCT = "INSERT INTO product (model, dealer, type, worth, length , width, height, specialParameter1, specialParameter2, imageURL1, imageURL2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String LIST_ALL_PRODUCTS = "SELECT * FROM product";

    private static final String DELETE_PRODUCT = "DELETE FROM product WHERE id = ?";

    private static final String UPDATE_PRODUCT = "UPDATE product SET model = ?, dealer = ?, type = ?, worth = ?, length = ?, width = ?, height = ?, specialParameter1 = ?, specialParameter2 = ?, imageURL1 = ?, imageURL2 = ? WHERE id = ?";

    private static final String GET_PRODUCT_BY_ID = "SELECT * FROM product WHERE id = ?";

    private static final String GET_PRODUCT_COUNT = "SELECT count(*) FROM product WHERE model LIKE ? AND dealer LIKE ? AND type LIKE ? AND specialParameter2 LIKE ? AND worth >= ? AND worth <= ? AND length >= ? AND length <= ? AND width >= ? AND width <= ? AND height >= ? AND height <= ? AND specialParameter1 >= ? AND specialParameter1 <= ?";

    private static final String FILTERED_SORTED_FIND = "SELECT * FROM product WHERE model LIKE ? AND dealer LIKE ? AND type LIKE ? AND specialParameter2 LIKE ? AND worth >= ? AND worth <= ? AND length >= ? AND length <= ? AND width >= ? AND width <= ? AND height >= ? AND height <= ? AND specialParameter1 >= ? AND specialParameter1 <= ? ORDER BY ";

    public boolean insert(Product product) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(INSERT_PRODUCT);
        statement.setString(1, product.getModel());
        statement.setString(2, product.getDealer());
        statement.setString(3, product.getType());
        statement.setLong(4, product.getWorth());
        statement.setLong(5, product.getLength());
        statement.setLong(6, product.getWidth());
        statement.setLong(7, product.getHeight());
        statement.setLong(8, product.getSpecialParameter1());
        statement.setString(9, product.getSpecialParameter2());
        statement.setString(10, product.getImageURL1());
        statement.setString(11, product.getImageURL2());

        LOGGER.info("ProductDaoImpl#insertProduct?model=" + product.getModel());
        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public List<Product> listAll() throws SQLException {
        List<Product> listProduct = new ArrayList<>();
        LOGGER.info("ProductDaoImpl#listAllProducts");

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(LIST_ALL_PRODUCTS);

        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String model = resultSet.getString("model");
            String dealer = resultSet.getString("dealer");
            String type = resultSet.getString("type");
            Long worth = resultSet.getLong("worth");
            Long length = resultSet.getLong("length");
            Long width = resultSet.getLong("width");
            Long height = resultSet.getLong("height");
            Long specialParameter1 = resultSet.getLong("specialParameter1");
            String specialParameter2 = resultSet.getString("specialParameter2");
            String imageURL1 = resultSet.getString("imageURL1");
            String imageURL2 = resultSet.getString("imageURL2");

            Product prod = this.buildProduct(id, model, dealer, type, worth, length, width, height, specialParameter1,
                    specialParameter2, imageURL1, imageURL2);
            listProduct.add(prod);
        }

        resultSet.close();
        statement.close();

        disconnect();

        return listProduct;
    }

    public boolean delete(Product product) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(DELETE_PRODUCT);
        statement.setLong(1, product.getId());

        LOGGER.info("ProductDaoImpl#deleteProduct?id=" + product.getId());
        boolean rowDeleted = statement.executeUpdate() > 0;

        statement.close();
        disconnect();
        return rowDeleted;
    }

    public boolean deleteById(Long id) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(DELETE_PRODUCT);
        statement.setLong(1, id);

        LOGGER.info("ProductDaoImpl#deleteProductById?id=" + id);
        boolean rowDeleted = statement.executeUpdate() > 0;

        statement.close();
        disconnect();
        return rowDeleted;
    }

    public boolean update(Product product) throws SQLException {
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(UPDATE_PRODUCT);
        statement.setString(1, product.getModel());
        statement.setString(2, product.getDealer());
        statement.setString(3, product.getType());
        statement.setLong(4, product.getWorth());
        statement.setLong(5, product.getLength());
        statement.setLong(6, product.getWidth());
        statement.setLong(7, product.getHeight());
        statement.setLong(8, product.getSpecialParameter1());
        statement.setString(9, product.getSpecialParameter2());
        statement.setString(10, product.getImageURL1());
        statement.setString(11, product.getImageURL2());
        statement.setLong(12, product.getId());

        LOGGER.info("ProductDaoImpl#updateProduct?&id=" + product.getId());
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    public Product getProductById(Long id) throws SQLException {
        Product product = null;

        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_PRODUCT_BY_ID);
        statement.setLong(1, id);

        LOGGER.info("ProductDaoImpl#getProductById?id=" + id);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String model = resultSet.getString("model");
            String dealer = resultSet.getString("dealer");
            String type = resultSet.getString("type");
            Long worth = resultSet.getLong("worth");
            Long length = resultSet.getLong("length");
            Long width = resultSet.getLong("width");
            Long height = resultSet.getLong("height");
            Long specialParameter1 = resultSet.getLong("specialParameter1");
            String specialParameter2 = resultSet.getString("specialParameter2");
            String imageURL1 = resultSet.getString("imageURL1");
            String imageURL2 = resultSet.getString("imageURL2");

            product = this.buildProduct(id, model, dealer, type, worth, length, width, height, specialParameter1,
                    specialParameter2, imageURL1, imageURL2);
        }

        resultSet.close();
        statement.close();
        disconnect();

        return product;
    }

    public long getCount(Filter f) throws SQLException {
        connect();

        LOGGER.info("ProductDaoImpl#getCount");

        PreparedStatement statement = jdbcConnection.prepareStatement(GET_PRODUCT_COUNT);
        setFilter(statement, f);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public List<Product> sortedFind(int startPosition, int maxResults, String sortFields, String sortDirections,
            Filter f) throws SQLException {
        connect();

        LOGGER.info("ProductDaoImpl#sortedFind?startPosition=" + startPosition + "&maxResults=" + maxResults
                + "&sortFields=" + sortFields + "&sortDirections=" + sortDirections);

        List<Product> listProduct = new ArrayList<Product>();

        PreparedStatement statement = jdbcConnection
                .prepareStatement(FILTERED_SORTED_FIND + sortFields + " " + sortDirections);
        setFilter(statement, f);
        // statement.setString(15, sortFields);
        // statement.setString(16, sortDirections);
        ResultSet resultSet = statement.executeQuery();
        int counter = 0;
        while (counter < startPosition) {
            resultSet.next();
            counter++;
        }
        while (resultSet.next() && counter < maxResults + startPosition) {
            Long id = resultSet.getLong("id");
            String model = resultSet.getString("model");
            String dealer = resultSet.getString("dealer");
            String type = resultSet.getString("type");
            Long worth = resultSet.getLong("worth");
            Long length = resultSet.getLong("length");
            Long width = resultSet.getLong("width");
            Long height = resultSet.getLong("height");
            Long specialParameter1 = resultSet.getLong("specialParameter1");
            String specialParameter2 = resultSet.getString("specialParameter2");
            String imageURL1 = resultSet.getString("imageURL1");
            String imageURL2 = resultSet.getString("imageURL2");

            Product prod = this.buildProduct(id, model, dealer, type, worth, length, width, height, specialParameter1,
                    specialParameter2, imageURL1, imageURL2);
            listProduct.add(prod);
            counter++;
        }

        return listProduct;
    }

    private Product buildProduct(Long id, String model, String dealer, String type, Long worth, Long length, Long width,
            Long height, Long specialParameter1, String specialParameter2, String imageURL1, String imageURL2) {
        Product product = new Product();
        product.setId(id);
        product.setModel(model);
        product.setDealer(dealer);
        product.setType(type);
        product.setWorth(worth);
        product.setLength(length);
        product.setWidth(width);
        product.setHeight(height);
        product.setSpecialParameter1(specialParameter1);
        product.setSpecialParameter2(specialParameter2);
        product.setImageURL1(imageURL1);
        product.setImageURL2(imageURL2);
        return product;
    }

    private void setFilter(PreparedStatement statement, Filter f) throws SQLException {
        if (f.getModel() != null && f.getModel() != "")
            statement.setString(1, "%" + f.getModel() + "%");
        else
            statement.setString(1, "%");
        if (f.getDealer() != null && f.getDealer() != "")
            statement.setString(2, "%" + f.getDealer() + "%");
        else
            statement.setString(2, "%");
        if (f.getType() != null && f.getType() != "")
            statement.setString(3, "%" + f.getType() + "%");
        else
            statement.setString(3, "%");
        if (f.getSpecialParameter2() != null && f.getSpecialParameter2() != "")
            statement.setString(4, "%" + f.getSpecialParameter2() + "%");
        else
            statement.setString(4, "%");
        if (f.getWorthMin() != null)
            statement.setLong(5, f.getWorthMin());
        else
            statement.setLong(5, 1);
        if (f.getWorthMax() != null)
            statement.setLong(6, f.getWorthMax());
        else
            statement.setLong(6, 499999);
        if (f.getLengthMin() != null)
            statement.setLong(7, f.getLengthMin());
        else
            statement.setLong(7, 1);
        if (f.getLengthMax() != null)
            statement.setLong(8, f.getLengthMax());
        else
            statement.setLong(8, 1000);
        if (f.getWidthMin() != null)
            statement.setLong(9, f.getWidthMin());
        else
            statement.setLong(9, 1);
        if (f.getWidthMax() != null)
            statement.setLong(10, f.getWidthMax());
        else
            statement.setLong(10, 1000);
        if (f.getHeightMin() != null)
            statement.setLong(11, f.getHeightMin());
        else
            statement.setLong(11, 1);
        if (f.getHeightMax() != null)
            statement.setLong(12, f.getHeightMax());
        else
            statement.setLong(12, 1000);
        if (f.getSpecialParameter1Min() != null)
            statement.setLong(13, f.getSpecialParameter1Min());
        else
            statement.setLong(13, 1);
        if (f.getSpecialParameter1Max() != null)
            statement.setLong(14, f.getSpecialParameter1Max());
        else
            statement.setLong(14, 1000000);
    }

}

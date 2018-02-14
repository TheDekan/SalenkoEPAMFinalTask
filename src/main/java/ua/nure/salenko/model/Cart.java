package ua.nure.salenko.model;

import org.apache.log4j.Logger;

public class Cart {

    private static final Logger LOGGER = Logger.getLogger(Cart.class);

    private Long id;
    private Long userId;
    private Long productId;
    private Long count;
    private String productRow;
    private Long totalWorth;
    private Long worthPerItem;

    public Cart() {
        LOGGER.info("Cart#constructor");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getProductRow() {
        return productRow;
    }

    public void setProductRow(String productRow) {
        this.productRow = productRow;
    }

    public Long getTotalWorth() {
        return totalWorth;
    }

    public void setTotalWorth(Long totalWorth) {
        this.totalWorth = totalWorth;
    }

    public Long getWorthPerItem() {
        return worthPerItem;
    }

    public void setWorthPerItem(Long worthPerItem) {
        this.worthPerItem = worthPerItem;
    }

    @Override
    public String toString() {
        return "Cart [id=" + id + ", userId=" + userId + ", productId=" + productId + ", count=" + count
                + ", productRow=" + productRow + ", totalWorth=" + totalWorth + ", worthPerItem=" + worthPerItem + "]";
    }

}

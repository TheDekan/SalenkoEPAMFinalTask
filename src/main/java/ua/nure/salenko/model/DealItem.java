package ua.nure.salenko.model;

import org.apache.log4j.Logger;

public class DealItem {

    private static final Logger LOGGER = Logger.getLogger(DealItem.class);

    private Long id;
    private Long dealId;
    private String productRow;
    private Long productCount;
    private Long worth;

    public DealItem() {
        LOGGER.info("DealItem#constructor");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

    public String getProductRow() {
        return productRow;
    }

    public void setProductRow(String productRow) {
        this.productRow = productRow;
    }

    public Long getProductCount() {
        return productCount;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }

    public Long getWorth() {
        return worth;
    }

    public void setWorth(Long worth) {
        this.worth = worth;
    }

}

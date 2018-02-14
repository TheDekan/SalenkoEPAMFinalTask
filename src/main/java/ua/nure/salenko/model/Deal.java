package ua.nure.salenko.model;

import java.util.Date;

import org.apache.log4j.Logger;

public class Deal {

    private static final Logger LOGGER = Logger.getLogger(Deal.class);

    private Long id;
    private Long userId;
    private Long worth;
    private Date sendDate;
    // unchecked, confirmed, rejected
    private String status;
    private String userName;

    public Deal() {
        LOGGER.info("Deal#constructor");
        this.sendDate = new Date();
        this.status = "unchecked";
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

    public Long getWorth() {
        return worth;
    }

    public void setWorth(Long worth) {
        this.worth = worth;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}

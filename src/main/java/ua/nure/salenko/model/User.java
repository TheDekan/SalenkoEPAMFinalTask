package ua.nure.salenko.model;

import java.util.Date;

import org.apache.log4j.Logger;

public class User {

    private static final Logger LOGGER = Logger.getLogger(User.class);

    private Long id;
    private String name;
    private String password;
    private String role;
    private Date joinDate;
    private Boolean blocked;

    public User() {
        LOGGER.info("User#constructor");
        this.joinDate = new Date();
        this.role = "Customer";
        this.blocked = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

}

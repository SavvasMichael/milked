package org.savvas.milked.controller;

public class MilkedUser {
    private Long id;
    private String email;
    private String name;
    private String password;
    private boolean activated = false;
    private String uuid;
    private int balance;
    private Long defaultGroupId;

    public MilkedUser() {
    }

    public MilkedUser(String email, String name, String password, String uuid) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.uuid = uuid;
        balance = 0;
    }

    public MilkedUser(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getUuid() {
        return uuid;
    }

    public int getBalance() {
        return balance;
    }

    public Long getDefaultGroupId() {
        return defaultGroupId;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}

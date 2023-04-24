package model;

import model.enums.Resource;

public class Trade {
    private User apllicant;
    private User requestedUser;
    private int resourceAmount;
    private Resource resourceType;
    private int price;
    private String message;
    private int id;

    public Trade(User apllicant, User requestedUser, int resourceAmount, Resource resourceType, int price, String message) {
        this.apllicant = apllicant;
        this.requestedUser = requestedUser;
        this.resourceAmount = resourceAmount;
        this.resourceType = resourceType;
        this.price = price;
        this.message = message;
    }

    public void setId() {
        //todo
    }
}

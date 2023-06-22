package model.game;

import model.User;
import model.enums.Resource;

import java.util.Random;

public class Trade {
    private User applicant;
    private User receiver;
    private int resourceAmount;
    private Resource resourceType;
    private int price;
    private String message;
    private Boolean request;
    private int id;
    private boolean isAccepted = false;

    public Trade(User applicant, User receiver, int resourceAmount, Resource resourceType, int price, String message
            , Boolean request) {
        this.applicant = applicant;
        this.receiver = receiver;
        this.resourceAmount = resourceAmount;
        this.resourceType = resourceType;
        this.price = price;
        this.message = message;
        this.request = request;
        setId();
    }

    public void setId() {
        Random random = new Random();
        id = random.nextInt(10000);

    }

    public User getApplicant() {
        return applicant;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public Resource getResourceType() {
        return resourceType;
    }

    public int getPrice() {
        return price;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Boolean getRequest() {
        return request;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}

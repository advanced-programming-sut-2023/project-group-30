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
    private int id;

    public Trade(User applicant, User receiver, int resourceAmount, Resource resourceType, int price, String message) {
        this.applicant = applicant;
        this.receiver = receiver;
        this.resourceAmount = resourceAmount;
        this.resourceType = resourceType;
        this.price = price;
        this.message = message;
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
}

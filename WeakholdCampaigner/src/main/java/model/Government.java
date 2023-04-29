package model;

import model.enums.Food;
import model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class Government {
    private int popularity;
    private ArrayList<Food> foods;
    private int foodAmountInStorage;
    private int foodRate;
    private int religionRate;
    private int population;
    private int numOfVillagers;
    private int taxRate;
    private int fearRate;
    //we want main castle but concept is unknown  //TODO
    private ArrayList<Trade> tradeList;
    private ArrayList<Trade> tradeHistory;
    private ArrayList<Trade> tradeNotification;
    private User owner;
    private HashMap<Resource, Integer> resources;

    public void getPopularity() {

    }

    public ArrayList<Trade> getTradeList() {
        return tradeList;
    }

    public ArrayList<Trade> getTradeHistory() {
        return tradeHistory;
    }

    public ArrayList<Trade> getTradeNotification() {
        return tradeNotification;
    }

    public User getOwner() {
        return owner;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public int getFoodAmountInStorage() {
        return foodAmountInStorage;
    }

    public void setFoodAmountInStorage(int foodAmountInStorage) {
        this.foodAmountInStorage = foodAmountInStorage;
    }

    public int getFoodRate() {
        return foodRate;
    }

    public void setFoodRate(int foodRate) {
        this.foodRate = foodRate;
    }

    public int getReligionRate() {
        return religionRate;
    }

    public void setReligionRate(int religionRate) {
        this.religionRate = religionRate;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getNumOfVillagers() {
        return numOfVillagers;
    }

    public void setNumOfVillagers(int numOfVillagers) {
        this.numOfVillagers = numOfVillagers;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getFearRate() {
        return fearRate;
    }

    public void setFearRate(int fearRate) {
        this.fearRate = fearRate;
    }

    public void addToTradeList(Trade trade) {
        tradeList.add(trade);
        tradeNotification.add(trade);
    }

    public void setTradeHistory(ArrayList<Trade> tradeHistory) {
        this.tradeHistory = tradeHistory;
    }

    public void setTradeNotification(ArrayList<Trade> tradeNotification) {
        this.tradeNotification = tradeNotification;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public HashMap<Resource, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }
}

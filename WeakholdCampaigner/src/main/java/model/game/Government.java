package model.game;

import model.User;
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
    private ArrayList<Trade> tradeList = new ArrayList<>();
    private ArrayList<Trade> tradeHistory = new ArrayList<>();
    private ArrayList<Trade> tradeNotification = new ArrayList<>();
    private User owner;
    private final GovernmentColor color;
    private HashMap<Resource, Integer> resources = new HashMap<>();

    private void installResource() {
        resources.put(Resource.GOLD_COIN, 20);
        resources.put(Resource.GOLD, 0);
        resources.put(Resource.BREAD, 0);
        resources.put(Resource.FLOUR, 0);
        resources.put(Resource.GRAIN, 0);
        resources.put(Resource.IRON, 0);
        resources.put(Resource.STONE, 0);
        resources.put(Resource.WHEAT, 0);
        resources.put(Resource.WINE, 0);
        resources.put(Resource.WOOD, 10);
    }

    public Government(User owner, int index) {
        this.owner = owner;
        installResource();
        this.color = GovernmentColor.values()[index];
    }

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

    public void addToTradeHistory(Trade trade) {
        tradeHistory.add(trade);
    }


    public HashMap<Resource, Integer> getResources() {
        return resources;
    }

    public void setResources(HashMap<Resource, Integer> resources) {
        this.resources = resources;
    }

    public boolean purchase(HashMap<Resource, Integer> productionCost) {
        //TODO: return true if there is enough resources and reduce them accordingly. otherwise return false
        return true;
    }

    public GovernmentColor getColor (){
        return this.color;
    }

    public enum GovernmentColor {
        RED,
        GREEN,
        BLUE,
        PURPLE,
        ORANGE,
        CYAN,
        YELLOW,
        GRAY
    }
}

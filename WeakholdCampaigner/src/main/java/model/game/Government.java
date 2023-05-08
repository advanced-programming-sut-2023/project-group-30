package model.game;

import controller.menu_controllers.GameMenuController;
import model.User;
import model.enums.Food;
import model.enums.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Government {
    private int popularity = 0;
    private int foodRate = -2;
    private int religionRate = 0;
    private int population = 2;
    private int numOfVillagers;
    private int taxRate = 0;
    private int fearRate = 0;
    //we want main castle but concept is unknown  //TODO
    private ArrayList<Trade> tradeList;
    private ArrayList<Trade> tradeHistory;
    private ArrayList<Trade> tradeNotification;
    private User owner;
    private HashMap<Resource, Double> resources = new HashMap<>();
    private int foodVariety = 0;
    private HashMap<Food, Double> foods = new HashMap<>();
    private int suitableBuildings = 0;
    private int unSuitableBuildings = 0;

    private final GovernmentColor color;
    private boolean hasPlacedKeep;

    private void installResource() {
        resources.put(Resource.GOLD_COIN, 20.0);
        resources.put(Resource.GOLD, (double) 0);
        resources.put(Resource.BREAD, (double) 0);
        resources.put(Resource.FLOUR, (double) 0);
        resources.put(Resource.GRAIN, (double) 0);
        resources.put(Resource.IRON, (double) 0);
        resources.put(Resource.STONE, (double) 0);
        resources.put(Resource.WHEAT, (double) 0);
        resources.put(Resource.WINE, (double) 0);
        resources.put(Resource.WOOD, 10.0);
    }

    private void installFoods() {
        foods.put(Food.BREAD, (double) 10);
        foods.put(Food.APPLE, (double) 0);
        foods.put(Food.MEAT, (double) 20);
        foods.put(Food.CHEESE, (double) 0);
    }

    public Government(User owner, int index) {
        this.owner = owner;
        tradeList = new ArrayList<>();
        tradeHistory = new ArrayList<>();
        tradeNotification = new ArrayList<>();
        installResource();
        this.color = GovernmentColor.values()[index];
        this.hasPlacedKeep = false;
        installFoods();
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


    public HashMap<Resource, Double> getResources() {
        return resources;
    }

    public void addResources(Resource resource, double amount) {
        resources.put(resource, resources.get(resource) + amount);
    }

    public Double getGold() {
        return GameMenuController.getCurrentGame().getCurrentGovernment().getResources().get(Resource.GOLD_COIN);
    }

    public void addGold(double gold) {
        GameMenuController.getCurrentGame().getCurrentGovernment().getResources().put(Resource.GOLD_COIN,
                getGold() + gold);

    }

    public HashMap<Food, Double> getFoods() {
        return foods;
    }


    public int getPopularityOfFood() {
        if (consumableFood() <= getFoodUnit()) {
        if (foodVariety > 0)
            return foodRate * 4 + (foodVariety - 1);
        return foodRate * 4;}
        return -8;
    }

    public Double consumableFood() {
        return (0.5 * foodRate + 1) * population;
    }

    public int getPopularityOfTax() {
        if (((-1) * getTax()) <= getGold()) {
            if (taxRate >= -3 && taxRate <= 0)
                return (-1 * taxRate * 2) + 1;
            else if (taxRate >= 1 && taxRate <= 4)
                return (-1) * taxRate * 2;
            return (taxRate - 2) * 4;
        }
        return 1;
    }

    public Double getTax() {
        if (taxRate == -3) return (double) (-1 * population);
        if (taxRate == -2) return 0.8 * (-1) * population;
        if (taxRate == -1) return -1 * 0.6 * population;
        if (taxRate == 0) return (double) 0;
        if (taxRate >= 1 && taxRate <= 8) return (0.2 * taxRate + 0.4) * population;
        return null;

    }

    public void addPopularityOfReligion(int amount) {
        religionRate = religionRate + amount;
    }

    public int getFearRate() {
        return fearRate + unSuitableBuildings - suitableBuildings;
    }

    public int getPopularityOfFear() {
        return (-1 * fearRate) - unSuitableBuildings + suitableBuildings;
    }

    public int getPopularity() {
        int sum = getPopularityOfFear() + getReligionRate();
        if (getFoodUnit() < consumableFood())
            sum += (-8);
        else sum += getPopularityOfFood();
        if (getTax() > getGold()) sum += 1;
        else sum += getPopularityOfTax();

        return sum;
    }

    public void setFoodVariety() {
        int i = 0;
        for (Double d : foods.values()) {
            if (!d.equals(0))
                i++;
        }
        foodVariety = i;
    }

    public void decreaseFood() {
        Double foodConsumer = consumableFood();
        String doubleAsString = String.valueOf(foodConsumer);
        int indexOfDecimal = doubleAsString.indexOf(".");
        int part = Integer.parseInt(doubleAsString.substring(0, indexOfDecimal));
        double decimal = Double.parseDouble(doubleAsString.substring(indexOfDecimal));
        decreaseDecimalPartOfConsumedFood(decimal);
        decreasePart(part);
    }

    public void decreaseDecimalPartOfConsumedFood(double decimal) {
        for (Food d : foods.keySet()) {
            if (!foods.get(d).equals(0) && foods.get(d) >= decimal) {
                foods.put(d, foods.get(d) - decimal);
                break;
            }
        }
    }

    public void decreasePart(int part) {
        int i = part;
        while (i != 0) {
            for (Map.Entry<Food, Double> entry :
                    foods.entrySet()) {
                if (entry.getValue() >= 1) {
                    foods.put(entry.getKey(), entry.getValue() - 1);
                    i--;
                    if (i == 0) break;
                }
            }
        }
    }

    public double getFoodUnit() {
        double i = 0;
        for (Map.Entry<Food, Double> entry :
                foods.entrySet()) {
            i += entry.getValue();
        }
        return i;
    }

    public void updateAllForNextTurn() {
        setFoodVariety();
        if (getFoodUnit() >= consumableFood()) decreaseFood();
        popularity += getPopularity();
        if ((getTax() * (-1)) <= getGold()) addGold(getTax());
    }
    public void addPopularity(int amount) {
        popularity += amount;
    }



    public boolean purchase(HashMap<Resource, Integer> productionCost) {
        //TODO: return true if there is enough resources and reduce them accordingly. otherwise return false
        return true;
    }

    public boolean hasPlacedKeep() {
        return hasPlacedKeep;
    }

    public void toggleHasPlacedKeep() {
        hasPlacedKeep = !hasPlacedKeep;
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

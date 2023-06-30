package model.game;

import controller.menu_controllers.GameMenuController;
import model.User;
import model.attributes.building_attributes.Capacity;
import model.enums.Resource;
import model.game.game_entities.Building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Government {
    private int popularity = 0;
    private int foodRate = -2;
    private int religionRate = 0;
    private int population = 2;
    private int popularityOfFood = 0;
    private int popularityOfFear = 0;
    private int popularityOfReligion = 0;
    private int popularityOfTax = 0;
    private int otherPopularity = 0;
    private int numOfVillagers;
    private int taxRate = 0;
    private double fearRate = 0;
    //we want main castle but concept is unknown  //TODO
    private ArrayList<Trade> tradeList;
    private ArrayList<Trade> tradeHistory;
    private ArrayList<Trade> tradeNotification;
    private User owner;
    private HashMap<Resource, Double> resources = new HashMap<>();
    private int foodVariety = 0;
    private HashMap<Resource, Double> foods = new HashMap<>();
    private HashMap<Resource, Integer> weapons = new HashMap<>();
    private int suitableBuildings = 0;
    private int unSuitableBuildings = 0;

    private final GovernmentColor color;
    private boolean hasPlacedKeep;

    private void installResource() {
        resources.put(Resource.GOLD_COIN, 20.0);
        resources.put(Resource.GOLD, (double) 20);
        resources.put(Resource.FLOUR, (double) 0);
        resources.put(Resource.GRAIN, (double) 0);
        resources.put(Resource.IRON, (double) 0);
        resources.put(Resource.STONE, (double) 0);
        resources.put(Resource.WHEAT, (double) 0);
        resources.put(Resource.WINE, (double) 0);
        resources.put(Resource.WOOD, 10.0);
        weapons.put(Resource.ARMOR, 0);
        weapons.put(Resource.SWORD, 0);
        weapons.put(Resource.BOW, 0);
        weapons.put(Resource.SPEAR, 0);

    }

    private void installFoods() {
        foods.put(Resource.BREAD, (double) 0);
        foods.put(Resource.APPLE, (double) 20);
        foods.put(Resource.MEAT, (double) 0);
        foods.put(Resource.CHEESE, (double) 0);
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

    public void setFearRate(double fearRate) {
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


    public void addResources(Resource resource, double amount) {
        if (resource == Resource.APPLE || resource == Resource.MEAT || resource == Resource.BREAD
                || resource == Resource.CHEESE) {
            if (getMaximumResource(Capacity.Stored.FOOD) < (getStoredUnit(Capacity.Stored.FOOD) + amount)) ;
            else
                foods.put(resource, amount + foods.get(resource));
        } else if (resource == Resource.BOW || resource == Resource.SWORD || resource == Resource.ARMOR
                || resource == Resource.SPEAR) {
            if (getMaximumResource(Capacity.Stored.WEAPON) < (getStoredUnit(Capacity.Stored.WEAPON) + amount)) ;
            else
                weapons.put(resource, (int) (amount + weapons.get(resource)));
        } else {
            if (getMaximumResource(Capacity.Stored.RECOURSE) < (getStoredUnit(Capacity.Stored.RECOURSE) + amount)
                    && (!resource.equals(Resource.GOLD) && !resource.equals(Resource.GOLD_COIN))) ;
            else {

                resources.put(resource, resources.get(resource) + amount);
            }
        }

    }

    public double getResources(Resource resource) {
        if (resource == Resource.APPLE || resource == Resource.MEAT || resource == Resource.BREAD
                || resource == Resource.CHEESE)
            return foods.get(resource);
        else if (resource == Resource.BOW || resource == Resource.SWORD || resource == Resource.ARMOR
                || resource == Resource.SPEAR)
            return weapons.get(resource);
        else return resources.get(resource);

    }

    public Capacity.Stored getResourcesCategory(Resource resource) {
        if (resource == Resource.APPLE || resource == Resource.MEAT || resource == Resource.BREAD
                || resource == Resource.CHEESE)
            return Capacity.Stored.FOOD;
        else if (resource == Resource.BOW || resource == Resource.SWORD || resource == Resource.ARMOR
                || resource == Resource.SPEAR)
            return Capacity.Stored.WEAPON;
        else return Capacity.Stored.RECOURSE;

    }

    public Double getGold() {
        return this.getResources(Resource.GOLD_COIN);
    }

    public void addGold(double gold) {
        this.addResources(Resource.GOLD_COIN, gold);
    }

    public HashMap<Resource, Double> getFoods() {
        return foods;
    }


    public int getPopularityOfFood() {
        if (consumableFood() <= getFoodUnit()) {
            if (foodVariety > 0)
                return foodRate * 4 + (foodVariety - 1);
            return foodRate * 4;
        }
        return -8;
    }

    public int getFoodPopularity() {
        return popularityOfFood;
    }

    public int getFearPopularity() {
        return popularityOfFear;
    }

    public int getReligionPopularity() {
        return popularityOfReligion;
    }

    public int getTaxPopularity() {
        return popularityOfTax;
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

    public double getFearRate() {
        return fearRate + unSuitableBuildings - suitableBuildings;
    }

    public double getPopularityOfFear() {
        return (-1 * fearRate) - unSuitableBuildings + suitableBuildings;
    }

    public int getPopularity() {
        popularity += (popularityOfFear + popularityOfTax + popularityOfFood + popularityOfReligion);
        return popularity;
    }
    public int getStaticPopularity() {
        return popularity;
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
        for (Resource d : foods.keySet()) {
            if (!foods.get(d).equals(0) && foods.get(d) >= decimal) {
                addResources(d, -decimal);
                break;
            }
        }
    }

    public void decreasePart(int part) {
        int i = part;
        while (i != 0) {
            for (Map.Entry<Resource, Double> entry :
                    foods.entrySet()) {
                if (entry.getValue() >= 1) {
                    addResources(entry.getKey(), -1);
                    i--;
                    if (i == 0) break;
                }
            }
        }
    }

    public double getFoodUnit() {
        double i = 0;
        for (Map.Entry<Resource, Double> entry :
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
        popularityOfFood += getPopularityOfFood();
        popularityOfFear += getPopularityOfFear();
        popularityOfTax += getPopularityOfTax();
        popularityOfReligion += getReligionRate();
    }

    public void addPopularity(int amount) {
        popularity += amount;
        otherPopularity += amount;
    }

    public int getOtherPopularity() {
        return otherPopularity;
    }

    public double getStoredUnit(Capacity.Stored stored) {
        double sum = 0;
        if (stored == Capacity.Stored.WEAPON) {
            for (Integer i : weapons.values())
                sum += i;
        } else if (stored == Capacity.Stored.FOOD) {
            for (double i : foods.values())
                sum += i;
        } else {
            for (double i : resources.values())
                sum += i;
        }

        return sum;
    }

    public int getMaximumResource(Capacity.Stored stored) {
        //TODO why are we instantiating here?
        System.out.println(this.getColor() + " " + stored);
        if (stored == Capacity.Stored.FOOD)
            return GameMenuController.getCurrentGame().numberOfSpecialBuildingInGovernment(this
                    , Building.getInstance("food store", 0, 0)) * 100;
        else if (stored == Capacity.Stored.WEAPON)
            return GameMenuController.getCurrentGame().numberOfSpecialBuildingInGovernment(this,
                    Building.getInstance("armory", 0, 0)) * 40;
        return GameMenuController.getCurrentGame().numberOfSpecialBuildingInGovernment(this
                , Building.getInstance("store", 0, 0)) * 100;
    }

    public boolean purchase(HashMap<Resource, Integer> productionCost) {
        //returns true if there is enough resources and reduces them accordingly. otherwise returns false.
        HashMap<Resource, Integer> tempStorage = new HashMap<>();
        for (Resource resource :
                productionCost.keySet()) {
            int inStorage = this.resources.get(resource).intValue(), tempPayment = productionCost.get(resource);
            if (inStorage < tempPayment) return false;

            tempStorage.put(resource, inStorage - tempPayment);
        }

        for (Resource resource :
                tempStorage.keySet()) {
            this.resources.replace(resource, tempStorage.get(resource).doubleValue());
        }
        return true;
    }

    public boolean hasPlacedKeep() {
        return hasPlacedKeep;
    }

    public void toggleHasPlacedKeep() {
        hasPlacedKeep = !hasPlacedKeep;
    }

    public GovernmentColor getColor() {
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

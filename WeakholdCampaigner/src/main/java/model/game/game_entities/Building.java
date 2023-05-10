package model.game.game_entities;

import model.attributes.Attribute;
import model.attributes.building_attributes.*;
import model.attributes.building_attributes.Process;
import model.enums.Resource;
import model.game.map.MapCell;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Building extends GameEntity {
    private Category category;
    private BuildingName buildingName;
    private int maxHP, HP;

    protected Building(BuildingName buildingName, HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes,
                       Category category, int maxHP) {
        super(productionCost, attributes);
        this.category = category;
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.buildingName = buildingName;
    }

    @Nullable
    public static Building getInstance(String name) {
        BuildingName buildingName = BuildingName.getBuildingName(name);
        if (buildingName == null) return null;

        HashMap<Resource, Integer> productionCost = new HashMap<>();
        ArrayList<Attribute> attributes = new ArrayList<>();
        Category category = Category.OTHER;
        int HP = 100;

        switch (buildingName) {
            //TODO
            case KEEP:
                HP = 100;
                category = Category.CASTLE;
                attributes.add(new ChangeTaxRate());
                break;
            case SMALL_GATEHOUSE:
                HP = 100;
                category = Category.CASTLE;
                attributes.add(new ChangeTaxRate());
                attributes.add(new HousePeasant(8));
                break;
            case BIG_GATEHOUSE:
                HP = 100;
                productionCost.put(Resource.STONE, 20);
                category = Category.CASTLE;
                attributes.add(new ChangeTaxRate());
                attributes.add(new HousePeasant(10));
                break;
            case DRAWBRIDGE:
                HP = 100;
                productionCost.put(Resource.WOOD, 10);
                attributes.add(new BridgeMobility(true));
                category = Category.CASTLE;
                break;
            case LOOKOUT_TOWER:
            case PERIMETER_TOWER:
                HP = 100;
                productionCost.put(Resource.STONE, 10);
                category = Category.CASTLE;
                attributes.add(new Tower(7, 7));
                break;
            case DEFENCE_TURRET:
                HP = 150;
                productionCost.put(Resource.STONE, 15);
                category = Category.CASTLE;
                attributes.add(new Tower(5, 5));
                break;
            case SQUARE_TOWER:
                HP = 100;
                productionCost.put(Resource.STONE, 35);
                category = Category.CASTLE;
                attributes.add(new Tower(5, 5));
                break;
            case ROUND_TOWER:
                HP = 150;
                productionCost.put(Resource.STONE, 40);
                category = Category.CASTLE;
                attributes.add(new Tower(5, 5));
                break;
            case SHOP:
                HP = 100;
                productionCost.put(Resource.WOOD, 5);
                category = Category.INDUSTRY;
                attributes.add(new NeedWorker(1));
                attributes.add(new Shop());
                break;
            case KILLING_PIT:
                HP = 100;
                category = Category.CASTLE;
                productionCost.put(Resource.WOOD, 6);
                attributes.add(new Trap());
                break;
            case MOTEL:
                HP = 100;
                category = Category.FOOD_PROCESSING;
                productionCost.put(Resource.WOOD, 20);
                productionCost.put(Resource.GOLD_COIN, 100);
                attributes.add(new NeedWorker(1));
                attributes.add(new DrinkServing());
                break;
            case MILL:
                HP = 100;
                category = Category.FOOD_PROCESSING;
                productionCost.put(Resource.WOOD, 20);
                attributes.add(new NeedWorker(3));
                attributes.add(new Process(Resource.WHEAT, Resource.FLOUR));
                break;
            case DESERT_SHRUB:
            case CHERRY_TREE:
            case DATE_TREE:
            case COCONUT_TREE:
            case OLIVE_TREE:
            case ROCK:
                category = Category.OTHER;
                break;
            case IRON_MINE:
                HP = 100;
                category = Category.INDUSTRY;
                productionCost.put(Resource.WOOD, 20);
                attributes.add(new NeedWorker(2));
                attributes.add(new NeedsSpecialPlacement(MapCell.Texture.IRON));
                attributes.add(new Harvesting(Resource.IRON, 1));
                break;
            case STONE_MINE:
                HP = 100;
                category = Category.INDUSTRY;
                productionCost.put(Resource.WOOD, 20);
                attributes.add(new NeedWorker(3));
                attributes.add(new Harvesting(Resource.STONE, 1));
                break;
            case CHURCH:
                HP = 100;
                category = Category.TOWN;
                productionCost.put(Resource.GOLD_COIN, 250);
                attributes.add(new IncreasePopularity(2));
                break;
            case APPLE_GARDEN:
                HP = 100;
                category = Category.FOOD_PROCESSING;
                productionCost.put(Resource.WOOD, 5);
                attributes.add(new Harvesting(Resource.APPLE, 1));
                attributes.add(new NeedWorker(1));
                break;
            case DIARY_FARMER:
                HP = 100;
                category = Category.FOOD_PROCESSING;
                productionCost.put(Resource.WOOD, 10);
                attributes.add(new NeedWorker(1));
                attributes.add(new Harvesting(Resource.CHEESE, 0.5));
                break;
            case WHEAT_FIELD:
                HP = 100;
                category = Category.FARM;
                productionCost.put(Resource.WOOD, 15);
                attributes.add(new NeedWorker(1));
                attributes.add(new Harvesting(Resource.WHEAT, 1));
                break;
            case BAKERY:
                HP = 100;
                category = Category.FOOD_PROCESSING;
                productionCost.put(Resource.WOOD, 10);
                attributes.add(new NeedWorker(1));
                attributes.add(new Process(Resource.FLOUR, Resource.BREAD));
                break;
            case GRAIN_FIELD:
                HP = 100;
                category = Category.FARM;
                productionCost.put(Resource.WOOD, 15);
                attributes.add(new NeedWorker(1));
                attributes.add(new Harvesting(Resource.GRAIN, 1));
                break;
            case BREWING:
                HP = 100;
                category = Category.FOOD_PROCESSING;
                productionCost.put(Resource.WOOD, 10);
                attributes.add(new NeedWorker(1));
                attributes.add(new Process(Resource.GRAIN, Resource.WINE));
                break;
            case ARMORY:
                HP = 100;
                productionCost.put(Resource.WOOD, 5);
                category = Category.CASTLE;
                attributes.add(new Capacity(40, Capacity.Stored.WEAPON));
                break;
            case STORE:
                HP = 100;
                category = Category.INDUSTRY;
                attributes.add(new Capacity(100, Capacity.Stored.RECOURSE));
                break;
            case FOOD_STORE:
                HP = 100;
                category = Category.FOOD_PROCESSING;
                attributes.add(new Capacity(100, Capacity.Stored.FOOD));
                attributes.add(new ChangeFoodRate());
                productionCost.put(Resource.WOOD, 5);
                break;
            case ARMOR:
                HP = 100;
                category = Category.WEAPONS;
                attributes.add(new Process(Resource.IRON, Resource.ARMOR));
                productionCost.put(Resource.GOLD_COIN, 100);
                productionCost.put(Resource.WOOD, 20);
                attributes.add(new NeedWorker(1));
                break;
            case BLACKSMITH:
                HP = 100;
                category = Category.WEAPONS;
                attributes.add(new Process(Resource.IRON, Resource.SWORD));
                productionCost.put(Resource.GOLD_COIN, 100);
                productionCost.put(Resource.WOOD, 20);
                attributes.add(new NeedWorker(1));
                break;
            case POLETURNER:
                HP = 100;
                category = Category.WEAPONS;
                attributes.add(new Process(Resource.IRON, Resource.SPEAR));
                productionCost.put(Resource.GOLD_COIN, 100);
                productionCost.put(Resource.WOOD, 20);
                attributes.add(new NeedWorker(1));
                break;
            case FLETCHER:
                HP = 100;
                category = Category.WEAPONS;
                attributes.add(new Process(Resource.IRON, Resource.BOW));
                productionCost.put(Resource.GOLD_COIN, 100);
                productionCost.put(Resource.WOOD, 20);
                attributes.add(new NeedWorker(1));
                break;
            case HOUSE:
                HP = 100;
                category = Category.TOWN;
                attributes.add(new HousePeasant(8));
                productionCost.put(Resource.WOOD, 6);
                break;
            case WOOD_CUTTER:
                HP = 100;
                category = Category.INDUSTRY;
                productionCost.put(Resource.WOOD, 3);
                attributes.add(new NeedWorker(1));
                attributes.add(new Harvesting(Resource.WOOD, 1));
                break;
            case BARRACKS:
                HP = 100;
                category = Category.CASTLE;
                productionCost.put(Resource.STONE, 15);
                attributes.add(new CreateUnit());
                break;








        }

        return new Building(buildingName, productionCost, attributes, category, HP);
    }

    public BuildingName getBuildingName() {
        return buildingName;
    }

    public enum Category {
        CASTLE,
        INDUSTRY,
        FARM,
        TOWN,
        WEAPONS,
        FOOD_PROCESSING,
        OTHER
    }
}

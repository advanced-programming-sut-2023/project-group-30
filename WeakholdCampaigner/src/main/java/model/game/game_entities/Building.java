package model.game.game_entities;

import model.attributes.Attribute;
import model.attributes.building_attributes.*;
import model.enums.Resource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Building extends GameEntity {
    private Category category;
    private int maxHP, HP;
    private BuildingName buildingName;

    protected Building(BuildingName buildingName,HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes,
                       Category category, int maxHP) {
        super(productionCost, attributes);
        this.category = category;
        this.maxHP = maxHP;
        this.HP = maxHP;
        this.buildingName = buildingName;
    }

    public BuildingName getBuildingName() {
        return buildingName;
    }

    public Category getCategory() {
        return category;
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
                attributes.add(new HousePeasant(8));
                break;
            case BIG_GATEHOUSE:
                HP = 100;
                productionCost.put(Resource.STONE, 20);
                category = Category.CASTLE;
                attributes.add(new HousePeasant(10));
                break;
            case DRAWBRIDGE:
                HP = 100;
                productionCost.put(Resource.WOOD, 10);
                category = Category.CASTLE;
                break;
            case LOOKOUT_TOWER: case PERIMETER_TOWER:
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
            case CHERRY_TREE:
            case OLIVE_TREE:
            case COCONUT_TREE:
            case DATE_TREE:
            case DESERT_SHRUB:
                HP = 50;
                category = Category.TREE;
                break;

        }

        return new Building(buildingName, productionCost, attributes, category, HP);
    }

    public enum Category {
        CASTLE,
        INDUSTRY,
        FARM,
        TOWN,
        WEAPONS,
        FOOD_PROCESSING,
        TREE,
        OTHER
    }
}

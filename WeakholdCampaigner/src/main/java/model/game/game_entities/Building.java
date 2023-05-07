package model.game.game_entities;

import model.attributes.Attribute;
import model.attributes.building_attributes.ChangeTaxRate;
import model.attributes.building_attributes.HousePeasant;
import model.attributes.building_attributes.Tower;
import model.enums.Resource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class Building extends GameEntity {
    private Category category;
    private int maxHP, HP;

    protected Building(HashMap<Resource, Integer> productionCost, ArrayList<Attribute> attributes,
                       Category category, int maxHP) {
        super(productionCost, attributes);
        this.category = category;
        this.maxHP = maxHP;
        this.HP = maxHP;
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
            case ROUND_TOWER:
                HP = 150;
                productionCost.put(Resource.STONE, 40);
                category = Category.CASTLE;
                attributes.add(new Tower(5, 5));
        }

        return new Building(productionCost, attributes, category, HP);
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

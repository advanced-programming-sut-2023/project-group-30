package model.game_entities;

public class Building extends GameEntity{
    private Category category;

    public enum Category{
        CASTLE,
        INDUSTRY,
        FARM,
        TOWN,
        WEAPONS,
        FOOD_PROCESSING
    }
}

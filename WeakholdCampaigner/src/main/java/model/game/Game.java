package model.game;

import controller.menu_controllers.GameMenuController;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.map.Map;
import model.User;
import model.game.map.MapCell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Game {
    private int currentTurn;
    private Map map;
    private Integer mapID;
    private int mapXPosition;
    private int mapYPosition;

    public int getMapXPosition() {
        return mapXPosition;
    }

    public int getMapYPosition() {
        return mapYPosition;
    }

    public void setMapXPosition(int mapXPosition) {
        this.mapXPosition = mapXPosition;
    }

    public void setMapYPosition(int mapYPosition) {
        this.mapYPosition = mapYPosition;
    }

    public Integer getMapID() {
        return mapID;
    }

    private ArrayList<Government> governments;
    private Government currentGovernment;

    public Game(Map map, ArrayList<Government> governments, Integer mapID) {
        this.currentTurn = 0;
        this.map = map;
        this.mapID = mapID;
        this.governments = governments;
        this.currentGovernment = governments.get(0);
    }

    public int getMapX() {
        return map.getWidth();
    }

    public int getMapY() {
        return map.getWidth();
    }

    @Nullable
    public Building getBuilding(int x, int y) {
        return map.getCell(x, y).getBuilding();
    }

    public void dropBuilding(Building building, int x, int y) {
        building.setGovernmentColor(currentGovernment.getColor());
        map.getCell(x, y).setBuilding(building);
    }

    public ArrayList<Unit> getUnits(int x, int y) {
        return map.getCell(x, y).getUnits();
    }
    public void dropUnit(Unit unit, int x, int y) {
        unit.setGovernmentColor(currentGovernment.getColor());
        map.getCell(x, y).addUnit(unit);
    }

    @NotNull
    public MapCell.Texture getTexture(int x, int y) {
        return map.getCell(x, y).getTexture();
    }

    public Government getCurrentGovernment() {
        return currentGovernment;
    }

    public void setTexture(int x, int y, MapCell.Texture texture){
        map.getCell(x, y).setTexture(texture);
    }

    public Map getMap() {
        return map;
    }

    public int[] move(int fromX, int fromY, int toX, int toY, int numOfSteps) {
        if ((fromX == toX && fromY == toY) || numOfSteps == 0)
            return new int[] {fromX, fromX};


        if (toX > fromX) if (Unit.canGoTo(
                map.getCell(fromX + 1, fromY).getTexture()
        )) {
            int[] temp = move(fromX + 1, fromY, toX, toY, numOfSteps - 1);
            if (temp[0] != fromX || temp[1] != fromY) return temp;
        }

        if (toX < fromX) if (Unit.canGoTo(
                map.getCell(fromX - 1, fromY).getTexture()
        )) {
            int[] temp = move(fromX - 1, fromY, toX, toY, numOfSteps - 1);
            if (temp[0] != fromX || temp[1] != fromY) return temp;
        }

        if (toY > fromY) if (Unit.canGoTo(
                map.getCell(fromX, fromY + 1).getTexture()
        ))
            return move(fromX, fromY + 1, toX, toY, numOfSteps - 1);

        if (toY < fromY) if (Unit.canGoTo(
                map.getCell(fromX, fromY - 1).getTexture()
        ))
            return move(fromX, fromY - 1, toX, toY, numOfSteps - 1);

        return new int[] {fromX, fromY};
    }

    public  ArrayList<Government> getGovernments() {
        return governments;
    }
    public Integer numberOfSpecialBuildingInGovernment(Government government, Building building){
        Integer output = 0;
        Map map = GameMenuController.getCurrentGame().getMap();
        for (int i = 0;i < map.getWidth();i++) {
            for(int j = 0; j < map.getWidth();j++) {
                if(map.getCell(i , j).getBuilding().equals(building) &&
                        building.getGovernmentColor().equals(government.getColor())){
                    output++;
                }
            }
        }
        return output;
    }
}

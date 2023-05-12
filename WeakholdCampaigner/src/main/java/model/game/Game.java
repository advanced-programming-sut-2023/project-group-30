package model.game;

import controller.menu_controllers.GameMenuController;
import model.game.game_entities.Building;
import model.game.game_entities.Unit;
import model.game.map.Map;
import model.game.map.MapCell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

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

    public int getMapWidth() {
        return map.getWidth();
    }

    public int getMapHeight() {
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

    @NotNull
    public ArrayList<Unit> getUnits(int x, int y) {
        return map.getCell(x, y).getUnits();
    }

    public void dropUnit(@NotNull Unit unit, int x, int y) {
        unit.setGovernmentColor(currentGovernment.getColor());
        map.getCell(x, y).addUnit(unit);
    }

    public void removeUnit(Unit unit, int x, int y) { //does not actually need x,y
        map.getCell(x, y).removeUnit(unit);
    }

    @NotNull
    public MapCell.Texture getTexture(int x, int y) {
        return map.getCell(x, y).getTexture();
    }

    public Government getCurrentGovernment() {
        return currentGovernment;
    }

    public void setTexture(int x, int y, MapCell.Texture texture) {
        map.getCell(x, y).setTexture(texture);
    }

    public Map getMap() {
        return map;
    }

    public boolean nextGovernment() { //returns true if a full turn has passed.
        int nextGovernmentIndex = governments.indexOf(currentGovernment) + 1;
        if (nextGovernmentIndex >= governments.size()) nextGovernmentIndex = 0;

        this.currentGovernment = governments.get(nextGovernmentIndex);

        return nextGovernmentIndex == 0; //returns true if all players have said "endTurn".
    }

    public int[] move(int fromX, int fromY, int toX, int toY, int numOfSteps) { //does not change anything,
        // simply returns {x, y, numOfRemainingSteps} where x,y is the destination the unit can reach
        // on its way to toX,toY with its numberOfSteps as a limit.
        if ((fromX == toX && fromY == toY) || numOfSteps == 0)
            return new int[]{fromX, fromY, numOfSteps};


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

        return new int[]{fromX, fromY, numOfSteps};
    }

    public ArrayList<Government> getGovernments() {
        return governments;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void incrementTurn() {
        currentTurn++;
    }

    public Integer numberOfSpecialBuildingInGovernment(Government government, Building building) {
        Integer output = 0;
        Map map = GameMenuController.getCurrentGame().getMap();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if (map.getCell(i, j).getBuilding().equals(building) &&
                        building.getGovernmentColor().equals(government.getColor())) {
                    output++;
                }
            }
        }
        return output;
    }


}

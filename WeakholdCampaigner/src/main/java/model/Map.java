package model;

public class Map {
    private MapCell[][] map;

    public Map(int mapWidth) {
        map = new MapCell[mapWidth][mapWidth];
    }

    public MapCell getCell(int x, int y){
        return map[x][y];
    }

    public int getWidth(){
        return map.length;
    }
}

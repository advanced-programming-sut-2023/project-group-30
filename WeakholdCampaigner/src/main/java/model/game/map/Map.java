package model.game.map;

public class Map {
    private MapCell[][] map;


    public Map(int mapWidth) {
        map = new MapCell[mapWidth][mapWidth];
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapWidth; j++) {
                map[i][j] = new MapCell();
            }
        }
    }

    public MapCell getCell(int x, int y) {
        return map[x][y];
    }

    public int getWidth() {
        return map.length;
    }
}

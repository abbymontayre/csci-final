import java.awt.*;

public class Map {
    public enum TileType { WALL, FLOOR }

    private static final TileType W = TileType.WALL;
    private static final TileType F = TileType.FLOOR;

    // 12 rows, 16 columns
    private static final TileType[][] LEVEL1 = {
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,F,F,F,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,F,F,F,W,F,F,F,F,F,F,F,F,F,W},
        {W,F,F,F,F,W,F,F,F,F,F,F,F,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,F,F,F,W},
        {W,F,F,F,F,W,F,F,F,F,F,F,F,F,F,W},
        {W,F,F,F,F,W,F,F,F,F,F,F,F,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
    };

    private static final TileType[][] LEVEL2 = {
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,W,F,F,W,F,F,W,F,F,F,F,F,F,W},
        {W,F,W,F,F,W,F,F,W,F,F,F,F,F,F,W},
        {W,F,W,W,W,W,F,F,F,F,F,F,W,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,W,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,W,F,F,F,F,F,F,F,F,F,F,W},
        {W,F,F,F,W,F,F,F,W,W,W,F,F,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,W,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,W,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
    };

    private static final TileType[][] LEVEL3 = {
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,F,F,F,F,W,F,W,F,F,F,F,F,F,W},
        {W,W,W,W,F,F,W,F,W,F,F,F,F,F,F,W},
        {W,F,F,F,F,F,W,F,W,W,W,W,W,F,F,W},
        {W,F,F,F,F,F,W,F,W,F,F,F,F,F,F,W},
        {W,F,W,F,F,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,W,F,F,F,F,F,W,F,F,W,W,W,W,W},
        {W,F,W,F,F,W,W,W,W,F,F,F,F,F,F,W},
        {W,F,F,F,F,F,F,F,W,F,F,F,W,F,F,W},
        {W,F,F,F,F,F,F,F,W,F,F,F,W,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
    };

    private int tileSize;
    private Image wallImage;
    private Image floorImage;
    private int currentmapID;
    private TileType[][] map;
    
        public Map() {
            tileSize = Constants.GAME_SETTINGS.TILE_SIZE;
            wallImage = new javax.swing.ImageIcon("wall.png").getImage();
            floorImage = new javax.swing.ImageIcon("floor.png").getImage();
            currentmapID = 0;
            updateMap();
    }

    public TileType getTile(int row, int col) {
        if (row < 0 || row >= map.length || col < 0 || col >= map[0].length) return TileType.WALL;
        return map[row][col];
    }

    public void draw(Graphics2D g2d) {
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[0].length; c++) {
                int x = c * tileSize;
                int y = r * tileSize;
                Image img = (map[r][c] == TileType.WALL) ? wallImage : floorImage;
                g2d.drawImage(img, x, y, tileSize, tileSize, null);
            }
        }       }
        public int getMapWidth() { return map[0].length * tileSize; }
        public int getMapHeight() { return map.length * tileSize; }
        
        public int getMapID(){
            return currentmapID;
        }

        public void incrementMapID(){
            currentmapID++;
        }

        private void updateMap(){
            switch (currentmapID) {
                case 0:
                    map = LEVEL1;
                    break;
                case 1:
                    map = LEVEL2;
                    break;
                case 2:
                    map = LEVEL3;
                    break;
            }
        }

        public void setMapID(int id) {
            currentmapID = id;
            updateMap();
        }
    }


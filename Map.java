import java.awt.*;

public class Map {
    public enum TileType { WALL, FLOOR }

    private static final TileType W = TileType.WALL;
    private static final TileType F = TileType.FLOOR;

    // 12 rows, 16 columns
    private static final TileType[][] TILEMAP = {
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,F,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,F,F,F,F,F,W,W,F,F,F,F,F,F,W},
        {W,W,W,W,W,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,W,F,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
        {W,F,F,F,W,F,F,F,W,F,F,F,F,F,F,W},
        {W,F,F,F,W,F,F,W,W,W,F,F,F,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,W,F,F,W},
        {W,F,F,F,F,F,F,F,F,F,F,F,W,F,F,W},
        {W,W,W,W,W,W,W,W,W,W,W,W,W,W,W,W},
    };

    private int tileSize;
    private Image wallImage;
    private Image floorImage;
    private TileType[][] tilemap;

    public Map() {
        tileSize = Constants.GAME_SETTINGS.TILE_SIZE;
        wallImage = new javax.swing.ImageIcon("wall.png").getImage();
        floorImage = new javax.swing.ImageIcon("floor.png").getImage();
        tilemap = TILEMAP;
    }

    // New constructor for custom map
    public Map(int[][] intMap) {
        tileSize = Constants.GAME_SETTINGS.TILE_SIZE;
        wallImage = new javax.swing.ImageIcon("wall.png").getImage();
        floorImage = new javax.swing.ImageIcon("floor.png").getImage();
        tilemap = new TileType[intMap.length][intMap[0].length];
        for (int r = 0; r < intMap.length; r++) {
            for (int c = 0; c < intMap[0].length; c++) {
                tilemap[r][c] = (intMap[r][c] == 1) ? TileType.WALL : TileType.FLOOR;
            }
        }
    }

    public TileType getTile(int row, int col) {
        if (row < 0 || row >= tilemap.length || col < 0 || col >= tilemap[0].length) return TileType.WALL;
        return tilemap[row][col];
    }

    public void draw(Graphics2D g2d) {
        for (int r = 0; r < tilemap.length; r++) {
            for (int c = 0; c < tilemap[0].length; c++) {
                int x = c * tileSize;
                int y = r * tileSize;
                Image img = (tilemap[r][c] == TileType.WALL) ? wallImage : floorImage;
                g2d.drawImage(img, x, y, tileSize, tileSize, null);
            }
        }
    }

    public int getMapWidth() { return tilemap[0].length * tileSize; }
    public int getMapHeight() { return tilemap.length * tileSize; }
}

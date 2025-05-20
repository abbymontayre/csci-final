import java.awt.*;

public class Map {
    public enum TileType { WALL, FLOOR }

    private static final TileType W = TileType.WALL;
    private static final TileType F = TileType.FLOOR;

    // 12 rows, 16 columns
    private static final TileType[][] TILEMAP = {
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

    private int tileSize;
    private Image wallImage;
    private Image floorImage;

    public Map() {
        tileSize = Constants.GAME_SETTINGS.TILE_SIZE;
        wallImage = new javax.swing.ImageIcon("wall.png").getImage();
        floorImage = new javax.swing.ImageIcon("floor.png").getImage();
    }

    public TileType getTile(int row, int col) {
        if (row < 0 || row >= TILEMAP.length || col < 0 || col >= TILEMAP[0].length) return TileType.WALL;
        return TILEMAP[row][col];
    }

    public void draw(Graphics2D g2d) {
        for (int r = 0; r < TILEMAP.length; r++) {
            for (int c = 0; c < TILEMAP[0].length; c++) {
                int x = c * tileSize;
                int y = r * tileSize;
                Image img = (TILEMAP[r][c] == TileType.WALL) ? wallImage : floorImage;
                g2d.drawImage(img, x, y, tileSize, tileSize, null);
            }
        }       }
        public int getMapWidth() { return TILEMAP[0].length * tileSize; }
        public int getMapHeight() { return TILEMAP.length * tileSize; }
    }

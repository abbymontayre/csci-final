/**
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
    @version 22 May 2025

    I have not discussed the Java language code in my program
    with anyone other than my instructor or the teaching assistants
    assigned to this course.

    I have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.

    If any Java language code or documentation used in my program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of my program.
 */
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

    /**
     * Constructor for the Map class.
     * Initializes the tile size, wall and floor images, and the tilemap.
     * The tilemap is a 2D array representing the map layout.
     */

    public Map() {
        tileSize = Constants.GAME_SETTINGS.TILE_SIZE;
        wallImage = new javax.swing.ImageIcon("wall.png").getImage();
        floorImage = new javax.swing.ImageIcon("floor.png").getImage();
        tilemap = TILEMAP;
    }

    /**
     * Constructor for the Map class with a custom tilemap.
     * Initializes the tile size, wall and floor images, and the tilemap.
     * The tilemap is a 2D array representing the map layout.
     */

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

    /**
     * The method getTile returns the tile type at the specified row and column.
     * The wall tile is returned if the row or column is out of bounds.
     */

    public TileType getTile(int row, int col) {
        if (row < 0 || row >= tilemap.length || col < 0 || col >= tilemap[0].length) return TileType.WALL;
        return tilemap[row][col];
    }

    /**
     * The method draw draws the map on the given Graphics2D object.
     * It iterates through the tilemap and draws the appropriate image for each tile.
     */

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

    /**
     * The method getMapWidth returns the width of the map in pixels.
     * It is calculated by multiplying the number of columns by the tile size.
     */

    public int getMapWidth() { return tilemap[0].length * tileSize; }

    /**
     * The method getMapHeight returns the height of the map in pixels.
     * It is calculated by multiplying the number of rows by the tile size.
     */
    
    public int getMapHeight() { return tilemap.length * tileSize; }
}

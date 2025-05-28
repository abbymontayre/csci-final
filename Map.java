import java.awt.*;
/**
    * The Map class is responsible for creating the background of the game.
    * It contains the tilemap, the tile size, the wall and floor images, and the getTile method.
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
    @version 23 May 2025

    We have not discussed the Java language code in our program
    with anyone other than our instructor or the teaching assistants
    assigned to this course.
    
    We have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.

    If any Java language code or documentation used in our program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of our program.
 */
public class Map {
    /***
     * This is the list of constants for the tile types.
     * This is used to determine the type of tile at a given position in the tilemap.
     */
    public enum TileType { WALL, FLOOR }

    private static final TileType W = TileType.WALL;
    private static final TileType F = TileType.FLOOR;

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
     * This is the default constructor for the Map class.
     * It initializes the tile size, wall and floor images, and the tilemap.
     * The tilemap is a 2D array representing the map layout.
     */
    public Map() {
        tileSize = Constants.GAME_SETTINGS.TILE_SIZE;
        wallImage = new javax.swing.ImageIcon("wall.png").getImage();
        floorImage = new javax.swing.ImageIcon("floor.png").getImage();
        tilemap = TILEMAP;
    }

    /**
     * The accessor method getTile returns the tile type at the specified row and column.
     * The wall tile is returned if the row or column is out of bounds.
     * @param row The row of the tile.
     * @param col The column of the tile.
     * @return The tile type at the specified row and column.
     */
    public TileType getTile(int row, int col) {
        if (row < 0 || row >= tilemap.length || col < 0 || col >= tilemap[0].length) return TileType.WALL;
        return tilemap[row][col];
    }

    /**
     * This getter method draws the map on the given Graphics2D object.
     * It goes through the row and column of the tilemap and draws the appropriate image for each tile.
     * @param g2d The Graphics2D object to draw the map on.
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
     * @return The width of the map in pixels.
     */
    public int getMapWidth() { return tilemap[0].length * tileSize; }

    /**
     * The method getMapHeight returns the height of the map in pixels.
     * It is calculated by multiplying the number of rows by the tile size.
     * @return The height of the map in pixels.
     */
    public int getMapHeight() { return tilemap.length * tileSize; }
}

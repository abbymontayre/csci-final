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
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;

public class Player extends Entity {
    private int speed;
    private int playerID;
    private KeyHandler keyHandler;
    private boolean facingLeft = false;  // Track direction
    private Image originalImage;  // Store the original image
    private Map map;  // Reference to the map for collision checks
    private boolean canMove = true;  // New field to control movement

    /**
     * Constructor for the Player class.
     * Initializes the player with a given ID, position, size, and map.
     * Loads the player sprite based on the player ID.
     *
     * @param playerID The ID of the player (1 or 2).
     * @param x The x-coordinate of the player.
     * @param y The y-coordinate of the player.
     * @param width The width of the player.
     * @param height The height of the player.
     * @param map The map object for collision detection.
     */

    public Player(int playerID, double x, double y, int width, int height, Map map) {  
        super(x, y, width, height);
        this.playerID = playerID;
        this.speed = Constants.ENTITY_SETTINGS.MOVE_SPEED;
        this.map = map;
        // Load and store the original image
        ImageIcon icon = playerID == 1 ? new ImageIcon("player_sprite.png") : new ImageIcon("player_sprite2.png");
        this.originalImage = icon.getImage();
    }

    /**
     * The method setKeyHandler sets the key handler for the player.
     * This allows the player to respond to key events.
     */

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    /**
     * The method setCanMove sets whether the player can move.
     * This is used to control player movement.
     */

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * The method draw draws the player on the given Graphics2D object.
     * It uses the original image and applies a transformation
     * to flip the image if the player is facing left.
     * 
     * @param g2d The Graphics2D object to draw on.
     */

    public void draw(Graphics2D g2d) {
        // Save the original transform
        AffineTransform reset = g2d.getTransform();
        
        if (facingLeft) {
            // If facing left, translate to the right edge of the image and flip
            g2d.translate(x + width, y);
            g2d.scale(-1, 1);
            g2d.drawImage(originalImage, 0, 0, width, height, null);
        } else {
            // If facing right, draw normally
            g2d.drawImage(originalImage, (int)x, (int)y, width, height, null);
        }
        
        // Restore the original transform
        g2d.setTransform(reset);
    }

    /**
     * The method update updates the player's position based on key input.
     * It checks for collisions with walls and updates the player's position accordingly.
     * The player can only move if canMove is true.
     */

    @Override
    public void update() {
        if (keyHandler != null && canMove) {  // Only move if canMove is true
            double newX = x;
            double newY = y;

        if (keyHandler.isMoveUp()) {
            newY -= speed;
        }
        if (keyHandler.isMoveDown()) {
            newY += speed;
        }
        if (keyHandler.isMoveLeft()) {
            newX -= speed;
            facingLeft = true;  // Set facing left when moving left
        }
        if (keyHandler.isMoveRight()) {
            newX += speed;
            facingLeft = false;  // Set facing right when moving right
        }

            // Check if the new position is valid (not a wall)
            int tileX = (int) (newX / Constants.GAME_SETTINGS.TILE_SIZE);
            int tileY = (int) (newY / Constants.GAME_SETTINGS.TILE_SIZE);
            int tileX2 = (int) ((newX + width - 1) / Constants.GAME_SETTINGS.TILE_SIZE);
            int tileY2 = (int) ((newY + height - 1) / Constants.GAME_SETTINGS.TILE_SIZE);

            // Only move if none of the tiles we would occupy are walls
            if (map.getTile(tileY, tileX) != Map.TileType.WALL &&
                map.getTile(tileY, tileX2) != Map.TileType.WALL &&
                map.getTile(tileY2, tileX) != Map.TileType.WALL &&
                map.getTile(tileY2, tileX2) != Map.TileType.WALL) {
                x = newX;
                y = newY;
            }
        }
    }

    /**
     * The method isFacingLeft checks if the player is facing left.
     * It returns true if the player is facing left, false otherwise.
     */

    public boolean isFacingLeft() {
        return facingLeft;
    }

    /**
     * The method setFacingLeft sets the player's facing direction.
     * It is used to update the direction the player is facing.
     *
     * @param facingLeft True if the player is facing left, false otherwise.
     */

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
}
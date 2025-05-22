import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
/**
    * The Player class represents the player in the game.
    * It is an extension of the Entity class.
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
    @version 22 May 2025

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
public class Player extends Entity {
    private int speed;
    private int playerID;
    private KeyHandler keyHandler;
    private boolean facingLeft = false;
    private Image originalImage;
    private Map map;
    private boolean canMove = true;

    /**
     * This is the constructor for the Player class.
     * It initializes the player with a given ID, position, size, map, and draws the player sprite based on the player ID.
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
        ImageIcon icon = playerID == 1 ? new ImageIcon("player_sprite.png") : new ImageIcon("player_sprite2.png");
        this.originalImage = icon.getImage();
    }

    /**
     * The mutator method setKeyHandler sets the key handler for the player.
     * This allows the player to respond to key events.
     */
    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    /**
     * The mutator method setCanMove sets whether the player can move.
     * This is mainly used for disabling movement when the player interacts with a guide item.
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * This mutator method is overidden from the Entity class.
     * It draws the player on the given Graphics2D object and flips the image if the player is facing left.
     * @param g2d The Graphics2D object to draw on.
     */
    public void draw(Graphics2D g2d) {
        AffineTransform reset = g2d.getTransform();
        
        if (facingLeft) {
            g2d.translate(x + width, y);
            g2d.scale(-1, 1);
            g2d.drawImage(originalImage, 0, 0, width, height, null);
        } else {
            g2d.drawImage(originalImage, (int)x, (int)y, width, height, null);
        }
        
        g2d.setTransform(reset);
    }

    /**
     * This update method is overidden from the Entity class and moves the player based on the key input.
     * It also checks for collisions with walls and updates the player's position accordingly.
     */
    @Override
    public void update() {
        if (keyHandler != null && canMove) { 
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
            facingLeft = true; 
        }
        if (keyHandler.isMoveRight()) {
            newX += speed;
            facingLeft = false; 
        }

            int tileX = (int) (newX / Constants.GAME_SETTINGS.TILE_SIZE);
            int tileY = (int) (newY / Constants.GAME_SETTINGS.TILE_SIZE);
            int tileX2 = (int) ((newX + width - 1) / Constants.GAME_SETTINGS.TILE_SIZE);
            int tileY2 = (int) ((newY + height - 1) / Constants.GAME_SETTINGS.TILE_SIZE);

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
     * The getter method isFacingLeft checks if the player is facing left.
     * It returns true if the player is facing left, false otherwise.
     */
    public boolean isFacingLeft() {
        return facingLeft;
    }

    /**
     * The mutator method setFacingLeft sets the player's facing direction.
     * It is used to update the player's character sprite based on the direction they are facing.
     * @param facingLeft Flag indicating the direction the player is facing.
     */
    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
}
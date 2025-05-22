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

    public Player(int playerID, double x, double y, int width, int height, Map map) {  
        super(x, y, width, height);
        this.playerID = playerID;
        this.speed = Constants.ENTITY_SETTINGS.MOVE_SPEED;
        this.map = map;
        // Load and store the original image
        ImageIcon icon = playerID == 1 ? new ImageIcon("player_sprite.png") : new ImageIcon("player_sprite2.png");
        this.originalImage = icon.getImage();
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

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

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }
}
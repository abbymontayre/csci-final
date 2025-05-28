import javax.swing.*;
import java.awt.*;
/**
 * This class creates the portal object of the game which extends the Entity abstract class.
 * It contains the portal image, the player object, and the checkCollision method.
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
public class Portal extends Entity {
    private Image portalImage;
    private Player player;

    /**
     * This is the Constructor for the Portal class.
     * It contians the ID corresponding to the playerID, x and y coordinates, and the width and height of the portal.
     * @param id       The ID of the portal.
     * @param x        The x-coordinate of the portal.
     * @param y        The y-coordinate of the portal.
     * @param width    The width of the portal.
     * @param height   The height of the portal.
     */
    public Portal(int id, int x, int y, int width, int height) {
        super(x, y, width, height); 
        portalImage = new ImageIcon("portal.png").getImage();
    }

    /**
     * The mutator method, setPlayer, initializes the player associated with the portal.
     * This allows the portal to check whether the player is colliding with it, which is needed to progress the game.
     * @param player Player object associated with the portal.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * The checkCollision getter method checks if the portal collides with another entity.
     * It uses hitboxes represented by rectangles to check whether the portal is colliding with another entity.
     * @param other Entity object to check for collision with.
     * @return true if there is a collision, false otherwise.
     */
    public boolean checkCollision(Entity other) {
        if (other == null) return false;
        Rectangle portalBounds = new Rectangle((int)x, (int)y, width, height);
        Rectangle otherBounds = new Rectangle((int)other.getX(), (int)other.getY(), other.getWidth(), other.getHeight());
        return portalBounds.intersects(otherBounds);
    }

    /**
     * The update mutator method checks for collisions with the player.
     * If a collision is detected, it triggers the portal's action.
     */
    public void update() {
        if (player != null) {
            checkCollision(player);
        }
    }

    /**
     * The draw mutator method is extended from the Entity abstract class.
     * It uses the Graphics2D object to draw the portal image.
     * @param g2d The Graphics2D object used for drawing.
     */
    public void draw(Graphics2D g2d) {
        g2d.drawImage(portalImage, (int)x, (int)y, width, height, null);
    }

} 
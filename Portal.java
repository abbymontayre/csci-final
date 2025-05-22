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

public class Portal extends Entity {
    private Image portalImage;
    private Player player;

    /**
     * Constructor for the Portal class.
     * 
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
     * The setPlayer method sets the player associated with the portal.
     * This allows the portal to check for collisions with the player.
     * 
     * @param player The player object to be associated with the portal.
     */

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * The checkCollision method checks if the portal collides with another entity.
     * 
     * @param other The other entity to check for collision with.
     * @return true if there is a collision, false otherwise.
     */

    public boolean checkCollision(Entity other) {
        if (other == null) return false;
        Rectangle portalBounds = new Rectangle((int)x, (int)y, width, height);
        Rectangle otherBounds = new Rectangle((int)other.getX(), (int)other.getY(), other.getWidth(), other.getHeight());
        return portalBounds.intersects(otherBounds);
    }

    /**
     * The update method checks for collisions with the player.
     * If a collision is detected, it triggers the portal's action.
     */

    public void update() {
        if (player != null) {
            checkCollision(player);
        }
    }

    /**
     * The draw method renders the portal on the screen.
     * It uses the Graphics2D object to draw the portal image.
     * 
     * @param g2d The Graphics2D object used for drawing.
     */

    
    public void draw(Graphics2D g2d) {
        g2d.drawImage(portalImage, (int)x, (int)y, width, height, null);
    }

} 
import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.geom.Rectangle2D;
/**
    * The KeyHandler class is responsible for handling the key presses and releases of the player.
    * Utilizing the ActionMap, It binds the keys to the actions defined below.
    * @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
    * @version 23 May 2025
    *
    * We have not discussed the Java language code in our program
    * with anyone other than our instructor or the teaching assistants
    * assigned to this course.
    *   
    * We have not used Java language code obtained from another student,
    * or any other unauthorized source, either modified or unmodified.
    *
    * If any Java language code or documentation used in our program
    * was obtained from another source, such as a textbook or website,
    * that has been clearly noted with a proper citation in the comments
    * of our program.
 */
public class GuideItem extends Entity {
    private Popup popup;
    private boolean isInteracting;
    private Player player; 
    private KeyHandler keyHandler;
    /***
     * This is the constructor for the GuideItem class.
     * It initializes the popup, the player, and the key handler.
     * @param x The x coordinate 
     * @param y The y coordinate 
     * @param title The title 
     * @param content The content 
     * @param itemWidth The width of the screen.
     * @param itemHeight The height of the screen.
     */
    public GuideItem(int x, int y, String title, String content, int itemWidth, int itemHeight) {
        super(x, y, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        this.popup = new Popup(title, content, itemWidth, itemHeight);
        isInteracting = false;
    }
    /***
     * This mutator method sets the player and the key handler.
     * This is done so that the guide item can interact with the player and the key handler.
     * @param player The player object.
     * @param keyHandler The key handler object.
     */
    public void setPlayerAndHandler(Player player, KeyHandler keyHandler) {
        this.player = player;
        this.keyHandler = keyHandler;
    }
    /***
     * This method checks if the guide item is colliding with another player.
     * It uses hitboxes represented by rectangles to check whether the guide item is colliding with the player.
     * @param other The other player.
     * @return true if the guide item is colliding with the other player, false otherwise.
     */
    public boolean isCollidingWith(Player other) {
        Rectangle2D.Double thisBounds = new Rectangle2D.Double(x, y, width, height);
        Rectangle2D.Double otherBounds = new Rectangle2D.Double(other.getX(), other.getY(), other.getWidth(), other.getHeight());
        return thisBounds.intersects(otherBounds);
    }

    /***
     * This mutator method updates the guide item and checks if the player is colliding with the guide item and if the player is interacting with the guide item.
     * If the player is colliding with the guide item and the player is interacting with the guide item, the dialog is shown otherwise the dialog is hidden.
     */
    @Override
    public void update() {
        if (player != null && keyHandler != null) {
            boolean isColliding = isCollidingWith(player);
      
            if (isColliding && keyHandler.isInteracting() && !popup.isVisible()) {
                showDialog();
            }
            
            if (popup.isVisible() && keyHandler.shouldCloseDialog()) {
                hideDialog();
            }
        }
    }
    /***
     * This method shows the dialog when the player is interacting with the guide item.
     * It also disables the player's movement when the dialog is shown.
     */
    public void showDialog() {
        popup.show();
        if (player != null) {
            player.setCanMove(false); 
        }
    }
    /***
     * This method hides the dialog when the player is not interacting with the guide item.
     * It also re-enables the player's movement when the dialog is hidden.
     */
    public void hideDialog() {
        popup.hide();
        if (player != null) {
            player.setCanMove(true);   
        }
    }
    /***
     * This method checks if the dialog is visible.
     * This is used to ensure the dialog is drawn on top of all the other elements of the game.
     * @return true if the dialog is visible, false otherwise.
     */
    public boolean isDialogVisible() {
        return popup.isVisible();
    }
    /***
     * This method is responsible for rendering the dialog when it is visible.
     * It calls the draw method of the popup class to draw the dialog.
     * @param g The Graphics object g.
     */
    public void drawPopup(Graphics g) {
        popup.draw(g);
    }

    /***
     * This method draws the guide item.
     * It draws the object using Images.
     * @param g2d The Graphics2D object g2d.
     */
    public void draw(Graphics2D g2d) {
        Image book = new ImageIcon("book.png").getImage();
        g2d.drawImage(book, (int)x, (int)y, width, height, null);
    }
}
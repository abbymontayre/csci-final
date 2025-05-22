import java.awt.*;
import javax.swing.ImageIcon;
import java.awt.geom.Rectangle2D;

public class GuideItem extends Entity {
    private Popup popup;
    private boolean isInteracting = false;
    private Player player;  // Reference to the player
    private KeyHandler keyHandler;  // Reference to the key handler

    public GuideItem(int x, int y, String title, String content, int screenWidth, int screenHeight) {
        super(x, y, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        this.popup = new Popup(title, content, screenWidth, screenHeight);
    }

    public void setPlayerAndHandler(Player player, KeyHandler keyHandler) {
        this.player = player;
        this.keyHandler = keyHandler;
    }

    public boolean isCollidingWith(Entity other) {
        Rectangle2D.Double thisBounds = new Rectangle2D.Double(x, y, width, height);
        Rectangle2D.Double otherBounds = new Rectangle2D.Double(other.getX(), other.getY(), other.getWidth(), other.getHeight());
        return thisBounds.intersects(otherBounds);
    }

    public void handleInteraction(boolean isInteracting) {
        if (isInteracting && !this.isInteracting) {
            showDialog();
        } else if (!isInteracting && this.isInteracting) {
            hideDialog();
        }
        this.isInteracting = isInteracting;
    }

    @Override
    public void update() {
        if (player != null && keyHandler != null) {
            boolean isColliding = isCollidingWith(player);
            
            // Show dialog on E press while colliding
            if (isColliding && keyHandler.isInteracting() && !popup.isVisible()) {
                showDialog();
            }
            
            // Close dialog on SPACE press
            if (popup.isVisible() && keyHandler.shouldCloseDialog()) {
                hideDialog();
            }
        }
    }

    public void showDialog() {
        popup.show();
        if (player != null) {
            player.setCanMove(false);  // Disable movement when dialog is shown
        }
    }

    public void hideDialog() {
        popup.hide();
        if (player != null) {
            player.setCanMove(true);   // Enable movement when dialog is hidden
        }
    }

    public boolean isDialogVisible() {
        return popup.isVisible();
    }

    public void drawPopup(Graphics g) {
        popup.draw(g);
    }


    public void draw(Graphics2D g2d) {
        Image book = new ImageIcon("book.png").getImage();
        g2d.drawImage(book, (int)x, (int)y, width, height, null);
    }
}
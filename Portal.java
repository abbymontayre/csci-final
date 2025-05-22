import java.awt.*;
import javax.swing.ImageIcon;

public class Portal extends Entity {
    private Image portalImage;
    private Player player;

    public Portal(int id, int x, int y, int width, int height) {
        super(x, y, width, height); 
        portalImage = new ImageIcon("portal.png").getImage();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean checkCollision(Entity other) {
        if (other == null) return false;
        Rectangle portalBounds = new Rectangle((int)x, (int)y, width, height);
        Rectangle otherBounds = new Rectangle((int)other.getX(), (int)other.getY(), other.getWidth(), other.getHeight());
        return portalBounds.intersects(otherBounds);
    }

    public void update() {
        if (player != null) {
            checkCollision(player);
        }
    }

    
    public void draw(Graphics2D g2d) {
        g2d.drawImage(portalImage, (int)x, (int)y, width, height, null);
    }

} 
// Player.java
import java.awt.*;

public class Player extends Entity {
    public Player(int id, int x, int y, int width, int height, int speed) {
        super(id, x, y, width, height, speed);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(getId() == 1 ? Color.BLUE : Color.RED);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
        
        // Optional: Add player number indicator
        g.setColor(Color.WHITE);
        g.drawString("P" + getId(), getX() + 15, getY() + 25);
    }
}
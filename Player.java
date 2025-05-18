import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends MyObj {
    private Image sprite;
    private String spritePath;

    public Player(int x, int y, int width, int height, String spritePath) {
        super(x, y, width, height);
        this.spritePath = spritePath;
        loadSprite();
    }

    public boolean playerCollision(Player other) {
        return collisionTester(other);
    }

    public void move(int dx, int dy, Map map, Player other) {
        int oldX = x;
        int oldY = y;

        x += dx;
        y += dy;

        if (!map.inTheMap(this)) {
            System.out.println("Move blocked by map bounds.");
            x = oldX;
            y = oldY;
        } else if (this.playerCollision(other)) {
            System.out.println("Move blocked due to collision with another player.");
            x = oldX;
            y = oldY;
        } else {
            System.out.println("Moved to: " + x + ", " + y);
        }
    }

    private void loadSprite() {
        // Load the sprite image from a file or resource
        try {
            sprite = javax.imageio.ImageIO.read(new java.io.File(spritePath));
            System.out.println("Player sprite loaded: " + sprite.getWidth(null) + "x" + sprite.getHeight(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }
}

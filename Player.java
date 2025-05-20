import java.awt.*;

public class Player extends Entity {
    private int speed;
    private int playerID;
    private KeyHandler keyHandler;

    public Player(int playerID, double x, double y, int width, int height) {  
        super(x, y, width, height);
        this.playerID = playerID;
        this.speed = Constants.ENTITY_SETTINGS.MOVE_SPEED;
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public void draw(Graphics2D g2d) {
        Color color = playerID == 1 ? Color.RED : Color.BLUE;
        g2d.setColor(color);
        g2d.fillRect((int)x, (int)y, width, height);
    }

    @Override
    public void update() {
        if (keyHandler != null) {
            if (keyHandler.isMoveUp() && y > 0) {
                y -= speed;
            }
            if (keyHandler.isMoveDown() && y < Constants.GAME_SETTINGS.SCREEN_HEIGHT - height) {
                y += speed;
            }
            if (keyHandler.isMoveLeft() && x > 0) {
                x -= speed;
            }
            if (keyHandler.isMoveRight() && x < Constants.GAME_SETTINGS.SCREEN_WIDTH - width) {
                x += speed;
            }
        }
    }
}
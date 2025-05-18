import java.io.Serializable;
import java.awt.*;

public abstract class Entity implements Serializable {
    protected int id, x, y, width, height, speed;

    public Entity(int id, int x, int y, int width, int height, int speed) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getSpeed() { return speed; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    // Movement methods
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    // Boundary check method
    public void checkBounds(int maxWidth, int maxHeight) {
        x = Math.max(0, Math.min(maxWidth - width, x));
        y = Math.max(0, Math.min(maxHeight - height, y));
    }

    // Abstract method for rendering (to be implemented by subclasses)
    public abstract void render(Graphics g);

    public boolean checkCollision(Entity other) {
    return this.x < other.x + other.width &&
           this.x + this.width > other.x &&
           this.y < other.y + other.height &&
           this.y + this.height > other.y;
}
}
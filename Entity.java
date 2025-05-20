import java.awt.*;

public abstract class Entity {
    protected double x, y;  // Changed to double for networking precision
    protected int width, height;

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public abstract void update();
    public abstract void draw(Graphics2D g2d);  
    
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }


    
}

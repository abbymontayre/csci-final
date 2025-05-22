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
import java.awt.*;

public abstract class Entity {
    protected double x, y;  // Changed to double for networking precision
    protected int width, height;

    /**
     * Constructor for the Entity class.
     * 
     * @param x      The x-coordinate of the entity.
     * @param y      The y-coordinate of the entity.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Abstract methods to be implemented by subclasses.
     * These methods define the behavior of the entity.
     * The update method is called to update the entity's state,
     * and the draw method is called to render the entity on the screen.
     */

    public abstract void update();
    public abstract void draw(Graphics2D g2d);  

    /**
     * Getters and setters for the entity's position and dimensions.
     * These methods allow access to the entity's properties.
     */

    /** 
     * The getX() method returns the x-coordinate of the entity.
     * This is used to get the horizontal position of the entity on the screen.
    */
    
    public double getX() { return x; }

    /**
     * The getY() method returns the y-coordinate of the entity.
     * This is used to get the vertical position of the entity on the screen.
    */

    public double getY() { return y; }

    /**
     * The setX() method sets the x-coordinate of the entity.
     * This is used to update the horizontal position of the entity on the screen.
    */

    public void setX(double x) { this.x = x; }

    /**
     * The setY() method sets the y-coordinate of the entity.
     * This is used to update the vertical position of the entity on the screen.
    */

    public void setY(double y) { this.y = y; }
    
    /**
     * The getWidth() method returns the width of the entity.
     * This is used to get the width of the entity for collision detection.
     */
    
    public int getWidth() {
        return width;
    }

    /**
     * The getHeight() method returns the height of the entity.
     * This is used to get the height of the entity for collision detection.
     */

    public int getHeight() {
        return height;
    }


    
}

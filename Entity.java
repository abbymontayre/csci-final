import java.awt.*;
/**
 * This abstract class is the parent class of all entities in the game.
 * It contains the necessary variables and methods for all entities.
 *  @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
    @version 22 May 2025

    We have not discussed the Java language code in our program
    with anyone other than our instructor or the teaching assistants
    assigned to this course.
    
    We have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.

    If any Java language code or documentation used in our program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of our program.
 */
public abstract class Entity {
    protected double x, y;
    protected int width, height;

    /**
     * This is the constructor for the Entity abstract class.
     * It initializes the x and y coordinates, and the width and height of the entity.
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
     * This abstract method is responsible for updating the entity's state.
     * This is done so that the entity can be updated in the game loop.
     */
    public abstract void update();

    /**
     * This abstract method is responsible for drawing the entity on the screen.
     * Creating this allows for shorter code in the drawing of all entities as they all bare the Entity type.
     * @param g2d The Graphics2D object used for drawing.
     */
    public abstract void draw(Graphics2D g2d);  


    /** 
     * The getX() getter method returns the x-coordinate of the entity.
     * This is especially useful for the networking implementation.
     * @return The x-coordinate of the entity.
    */
    public double getX() { return x; }

    /**
     * The getY() getter method returns the y-coordinate of the entity.
     * This is especially useful for the networking implementation.
     * @return The y-coordinate of the entity.
    */
    public double getY() { return y; }

    /**
     * The setX() mutator method sets the x-coordinate of the entity.
     * This is used to update the horizontal position of the entity on the screen, most especially in the networking implementation of the Player movement.
     * @param x The new x-coordinate of the entity.
    */
    public void setX(double x) { this.x = x; }

    /**
     * The setY() mutator method sets the y-coordinate of the entity.
     * This is used to update the vertical position of the entity on the screen, most especially in the networking implementation of the Player movement.
     * @param y The new y-coordinate of the entity.
    */
    public void setY(double y) { this.y = y; }
    
    /**
     * The getWidth() getter method returns the width of the entity.
     * This is mainly used to get the width of the entity for collision detection.
     * @return The width of the entity.
     */
    public int getWidth() {
        return width;
    }

    /**
     * The getHeight() getter method returns the height of the entity.
     * This is mainly used to get the height of the entity for collision detection.
     * @return The height of the entity.
     */

    public int getHeight() {
        return height;
    }

}

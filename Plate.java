import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
    * The Plate class represents the pressure plates in the game.
    * It is an extension of the Entity class.
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
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
public class Plate extends Entity {
    private Image plateImage;
    private Player player;
    private KeyHandler keyHandler;
    private boolean isActivated;
    private int sequenceNumber;  
    private static boolean[] activatedPlates;  
    private static int currentSequencePosition = 0; 
    private static final int TOTAL_PLATES = 5; 
    private static ArrayList<Plate> allPlates = new ArrayList<>();
    /***
     * This is the constructor for the Plate class.
     * It initializes the sequence number, the activated state, the plate image, and the static array of activated plates.
     * @param sequenceNumber The sequence number of the plate.
     * @param x The x coordinate of the plate.
     * @param y The y coordinate of the plate.
     * @param width The width of the plate.
     * @param height The height of the plate.
     */
    public Plate(int sequenceNumber, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sequenceNumber = sequenceNumber;
        this.isActivated = false;
        plateImage = new ImageIcon("pressure_plate.png").getImage();
        
        if (activatedPlates == null) {
            activatedPlates = new boolean[TOTAL_PLATES];
            resetSequence();
        }
        
        if (!allPlates.contains(this)) {
            allPlates.add(this);
        }
    }
    /***
     * This mutator method sets the pLayer and keyhandler.
     * Particularly it allows the player to interact with the plate.
     * @param player The player object.
     * @param keyHandler The key handler object.
     */
    public void setPlayerAndHandler(Player player, KeyHandler keyHandler) {
        this.player = player;
        this.keyHandler = keyHandler;
    }
    /***
     * This mutator method resets the sequence of the plates.
     * This is called when the player fails to complete the sequence in order.
     */
    public static void resetSequence() {
        if (activatedPlates != null) {
            for (int i = 0; i < activatedPlates.length; i++) {
                activatedPlates[i] = false;
            }
            currentSequencePosition = 0;
            
            for (Plate plate : allPlates) {
                plate.isActivated = false;
            }
        }
    }
    /***
     * This method is an override of the update method in the Entity class.
     * It checks if the player has interacted with the plate and if the sequence number is correct.
     */
    @Override
    public void update() {
        if (player != null && checkCollision(player) && keyHandler.isInteractPressed() && !isActivated) {
            if (sequenceNumber == currentSequencePosition + 1) {
                activatePlate();
                System.out.println("Plate " + sequenceNumber + " activated! Current sequence: " + currentSequencePosition);
            } else {
                System.out.println("Wrong sequence! Starting over...");
                resetSequence();
            }
        }
        
        if (activatedPlates != null && sequenceNumber <= activatedPlates.length) {
            isActivated = activatedPlates[sequenceNumber - 1];
        }
    }
    /***
     * This private method activates the plate.
     * It sets the activated state to true and updates the activated plates array.
     */
    private void activatePlate() {
        isActivated = true;
        activatedPlates[sequenceNumber - 1] = true;
        currentSequencePosition++;
    }
    /***
     * This getter method checks if the sequence is complete.
     * It returns true if all plates are activated in order.
     * @return True if the sequence is complete, false otherwise.
     */
    public static boolean isSequenceComplete() {
        if (activatedPlates == null) return false;
        
        for (int i = 0; i < TOTAL_PLATES; i++) {
            if (!activatedPlates[i]) return false;
        }
        return currentSequencePosition == TOTAL_PLATES;
    }
    /***
     * This getter method gets the current sequence position.
     * This is used in the networking to ensure the activated plates are correctly updated.
     * @return The current sequence position.
     */
    public static int getCurrentSequencePosition() {
        return currentSequencePosition;
    }
    /***
     * This mutator method sets the current sequence position.
     * This is used in the networking to ensure the activated plates are correctly updated.
     * @param position The position to set the current sequence to.
     */
    public static void setCurrentSequencePosition(int position) {
        currentSequencePosition = position;

        if (activatedPlates != null) {
            for (int i = 0; i < activatedPlates.length; i++) {
                activatedPlates[i] = i < position;
            }
            
            for (Plate plate : allPlates) {
                if (plate.sequenceNumber <= position) {
                    plate.isActivated = true;
                } else {
                    plate.isActivated = false;
                }
            }
        }
    }
    /***
     * This method is an override of the draw method in the Entity class.
     * It draws the plate and overlays a green or white tint based on the activated state.
     * @param g2d The Graphics2D object used for drawing.
     */
    @Override
    public void draw(Graphics2D g2d) {
        if (plateImage != null) {

            if (isActivated) {
                g2d.setColor(new Color(0, 255, 0, 128));
            } else {
                g2d.setColor(new Color(255, 255, 255, 128));
            }
            g2d.drawImage(plateImage, (int)x, (int)y, width, height, null);
            g2d.fillRect((int)x, (int)y, width, height);
        }
    }
    /***
     * This method checks if the plate has collided with another entity.
     * It returns true if the plate has collided with the other entity.
     * @param other The other entity to check collision with.
     * @return True if the plate has collided with the other entity, false otherwise.
     */
    public boolean checkCollision(Entity other) {
        if (other == null) return false;
        Rectangle plateBounds = new Rectangle((int)x, (int)y, width, height);
        Rectangle otherBounds = new Rectangle((int)other.getX(), (int)other.getY(), other.getWidth(), other.getHeight());
        return plateBounds.intersects(otherBounds);
    }
    /***
     * This getter method checks if the plate is activated.
     * It is used in the networking to ensure the activated plates are correctly updated.
     * @return True if the plate is activated, false otherwise.
     */
    public boolean isActivated() {
        return isActivated;
    }
    /***
     * This mutator method sets the activated state of the plate.
     * It mainlyis used in the networking to ensure the activated plates are correctly updated.
     * @param activated The state to set the activated state to.
     */
    public void setActivated(boolean activated) {
        this.isActivated = activated;
        if (sequenceNumber <= activatedPlates.length) {
            activatedPlates[sequenceNumber - 1] = activated;
        }
    }
} 
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Plate extends Entity {
    private Image plateImage;
    private Player player;
    private KeyHandler keyHandler;
    private boolean isActivated;
    private int sequenceNumber;  // The correct position in the sequence
    private static boolean[] activatedPlates;  // Tracks which plates are activated
    private static int currentSequencePosition = 0;  // Tracks current position in sequence
    private static final int TOTAL_PLATES = 5;  // Total number of plates in the sequence
    private static ArrayList<Plate> allPlates = new ArrayList<>();  // Keep track of all plates

    public Plate(int sequenceNumber, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sequenceNumber = sequenceNumber;
        this.isActivated = false;
        plateImage = new ImageIcon("pressure_plate.png").getImage();
        
        // Initialize the static array if this is the first plate
        if (activatedPlates == null) {
            activatedPlates = new boolean[TOTAL_PLATES];
            resetSequence();
        }
        
        // Add this plate to the static list
        if (!allPlates.contains(this)) {
            allPlates.add(this);
        }
    }

    public void setPlayerAndHandler(Player player, KeyHandler keyHandler) {
        this.player = player;
        this.keyHandler = keyHandler;
    }

    public static void resetSequence() {
        if (activatedPlates != null) {
            for (int i = 0; i < activatedPlates.length; i++) {
                activatedPlates[i] = false;
            }
            currentSequencePosition = 0;
            
            // Reset all plates' activated state
            for (Plate plate : allPlates) {
                plate.isActivated = false;
            }
        }
    }

    @Override
    public void update() {
        if (player != null && checkCollision(player) && keyHandler.isInteractPressed() && !isActivated) {
            // Check if this is the next plate in sequence
            if (sequenceNumber == currentSequencePosition + 1) {
                activatePlate();
                System.out.println("Plate " + sequenceNumber + " activated! Current sequence: " + currentSequencePosition);
            } else {
                // Wrong sequence - reset all plates
                System.out.println("Wrong sequence! Starting over...");
                resetSequence();
            }
        }
        
        // Keep local activation state in sync with global state
        if (activatedPlates != null && sequenceNumber <= activatedPlates.length) {
            isActivated = activatedPlates[sequenceNumber - 1];
        }
    }

    private void activatePlate() {
        isActivated = true;
        activatedPlates[sequenceNumber - 1] = true;
        currentSequencePosition++;
    }

    public static boolean isSequenceComplete() {
        if (activatedPlates == null) return false;
        
        // Check if all plates are activated in order
        for (int i = 0; i < TOTAL_PLATES; i++) {
            if (!activatedPlates[i]) return false;
        }
        return currentSequencePosition == TOTAL_PLATES;
    }

    public static int getCurrentSequencePosition() {
        return currentSequencePosition;
    }

    public static void setCurrentSequencePosition(int position) {
        currentSequencePosition = position;
        // Ensure activatedPlates array matches the current sequence position
        if (activatedPlates != null) {
            for (int i = 0; i < activatedPlates.length; i++) {
                activatedPlates[i] = i < position;
            }
            // Update all plates to match the current sequence
            for (Plate plate : allPlates) {
                if (plate.sequenceNumber <= position) {
                    plate.isActivated = true;
                } else {
                    plate.isActivated = false;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (plateImage != null) {
            // Draw different colors based on activation state
            if (isActivated) {
                g2d.setColor(new Color(0, 255, 0, 128)); // Green tint when activated
            } else {
                g2d.setColor(new Color(255, 255, 255, 128)); // White tint when not activated
            }
            g2d.drawImage(plateImage, (int)x, (int)y, width, height, null);
            g2d.fillRect((int)x, (int)y, width, height);
        }
    }

    public boolean checkCollision(Entity other) {
        if (other == null) return false;
        Rectangle plateBounds = new Rectangle((int)x, (int)y, width, height);
        Rectangle otherBounds = new Rectangle((int)other.getX(), (int)other.getY(), other.getWidth(), other.getHeight());
        return plateBounds.intersects(otherBounds);
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        this.isActivated = activated;
        if (sequenceNumber <= activatedPlates.length) {
            activatedPlates[sequenceNumber - 1] = activated;
        }
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
} 
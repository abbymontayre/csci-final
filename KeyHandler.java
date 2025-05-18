import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler {
    private static final int MOVE_SPEED = 3; // Reduced for better diagonal control
    private final JComponent component;
    private final Set<String> activeKeys = new HashSet<>();
    private int xVelocity = 0;
    private int yVelocity = 0;

    public KeyHandler(JComponent component) {
        this.component = component;
        setupKeyBindings();
        startMovementThread();
    }

    private void setupKeyBindings() {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        // Pressed actions
        bindKey(inputMap, actionMap, "up.pressed", KeyStroke.getKeyStroke("pressed UP"), "UP");
        bindKey(inputMap, actionMap, "down.pressed", KeyStroke.getKeyStroke("pressed DOWN"), "DOWN");
        bindKey(inputMap, actionMap, "left.pressed", KeyStroke.getKeyStroke("pressed LEFT"), "LEFT");
        bindKey(inputMap, actionMap, "right.pressed", KeyStroke.getKeyStroke("pressed RIGHT"), "RIGHT");
        bindKey(inputMap, actionMap, "e.pressed", KeyStroke.getKeyStroke("pressed E"), "E");
        bindKey(inputMap, actionMap, "space.pressed", KeyStroke.getKeyStroke("pressed SPACE"), "SPACE");
        
        // Released actions
        bindKey(inputMap, actionMap, "up.released", KeyStroke.getKeyStroke("released UP"), "UP");
        bindKey(inputMap, actionMap, "down.released", KeyStroke.getKeyStroke("released DOWN"), "DOWN");
        bindKey(inputMap, actionMap, "left.released", KeyStroke.getKeyStroke("released LEFT"), "LEFT");
        bindKey(inputMap, actionMap, "right.released", KeyStroke.getKeyStroke("released RIGHT"), "RIGHT");
        
      
        
    }

    private void bindKey(InputMap inputMap, ActionMap actionMap, String name, KeyStroke keyStroke, String direction) {
        inputMap.put(keyStroke, name);
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (name.contains("pressed")) {
                    activeKeys.add(direction);
                } else {
                    activeKeys.remove(direction);
                }
                updateVelocity();
            }
        });
    }

   private void updateVelocity() {
    xVelocity = 0;
    yVelocity = 0;

    if (activeKeys.contains("LEFT")) {
        xVelocity -= MOVE_SPEED;
        xVelocity -= MOVE_SPEED;
    }
    if (activeKeys.contains("RIGHT")) {
        xVelocity += MOVE_SPEED;
        xVelocity += MOVE_SPEED;
    }
    if (activeKeys.contains("UP")) {
        yVelocity -= MOVE_SPEED;
        yVelocity -= MOVE_SPEED;
    }
    if (activeKeys.contains("DOWN")) {
        yVelocity += MOVE_SPEED;
        yVelocity += MOVE_SPEED;
    }
}

    private void startMovementThread() {
        new Thread(() -> {
            while (true) {
                if (component instanceof MovementSender) {
                    ((MovementSender)component).sendMovement(xVelocity, yVelocity);
                }
                try {
                    Thread.sleep(8); // ~60 updates/second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public int getXVelocity() { return xVelocity; }
    public int getYVelocity() { return yVelocity; }

    // In KeyHandler.java, add debug output:
public boolean isKeyPressed(String key) {
    return activeKeys.contains(key);
}
    
    public void clearKey(String key) {
    activeKeys.remove(key);
}

    public interface MovementSender {
        void sendMovement(int xVel, int yVel);
    }

    
}
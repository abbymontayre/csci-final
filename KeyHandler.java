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
import java.awt.event.*;
import javax.swing.*;

public class KeyHandler {
    private InputMap inputMap;
    private ActionMap actionMap;
    private  boolean moveUp, moveDown, moveLeft, moveRight;
    private boolean interactPressed = false;
    private boolean closeDialog = false;

    /**
     * Constructor for the KeyHandler class.
     * Initializes the action and input maps for the given component.
     * 
     * @param cp The JComponent to which the key bindings will be applied.
     * The action map and input map are taken from the component.
     * The boolean variables are initialized to false.
     */

    public KeyHandler(JComponent cp) {
        actionMap = cp.getActionMap();
        inputMap = cp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
        interactPressed = false;
        closeDialog = false;

    }

    /**
     * The UpMove action will be triggered depending on the key pressed.
     * It sets the moveUp variable to true.
     */

    AbstractAction UpMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveUp = true;
        }
    };

    /**
     * The UpRelease action will be triggered depending on the key pressed.
     * It sets the moveUp variable to false.
     */

    AbstractAction UpRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveUp = false;
        }
    };

    /**
     * The DownMove action will be triggered depending on the key pressed.
     * It sets the moveDown variable to true.
     */

    AbstractAction DownMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown = true;
        }
    };

    /**
     * The DownRelease action will be triggered depending on the key pressed.
     * It sets the moveDown variable to false.
     */

    AbstractAction DownRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown = false;
        }
    };

    /**
     * The LeftMove action will be triggered depending on the key pressed.
     * It sets the moveLeft variable to true.
     */

    AbstractAction LeftMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveLeft = true;
        }
    };

    /**
     * The LeftRelease action will be triggered depending on the key pressed.
     * It sets the moveLeft variable to false.
     */

    AbstractAction LeftRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveLeft = false;
        }
    };

    /**
     * The RightMove action will be triggered depending on the key pressed.
     * It sets the moveRight variable to true.
     */

    AbstractAction RightMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveRight = true;
        }
    };

    /**
     * The RightRelease action will be triggered depending on the key pressed.
     * It sets the moveRight variable to false.
     */

    AbstractAction RightRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveRight = false;
        }
    }; 

    /**
     * The InteractPress action will be triggered depending on the key pressed.
     * It sets the interactPressed variable to true.
     */

    AbstractAction InteractPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            interactPressed = true;
        }
    };

    /**
     * The InteractRelease action will be triggered depending on the key pressed.
     * It sets the interactPressed variable to false.
     */

    AbstractAction CloseDialogPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            closeDialog = true;
        }
    };

    /**
     * The isMoveUp method will return a boolean value.
     * When the entity is moving up, it will return true.
     */

    public boolean isMoveUp() {
        return moveUp;
    }

    /**
     * The isMoveDown method will return a boolean value.
     * When the entity is moving down, it will return true.
     */

    public boolean isMoveDown() {
        return moveDown;
    }

    /**
     * The isMoveLeft method will return a boolean value.
     * When the entity is moving left, it will return true.
     */


    public boolean isMoveLeft() {
        return moveLeft;
    }

    /**
     * The isMoveRight method will return a boolean value.
     * When the entity is moving right, it will return true.
     */

    public boolean isMoveRight() {
        return moveRight;
    }

    /**
     * The isInteracting method will return a boolean value.
     * When the entity is interacting, it will return true.
     */

    public boolean isInteracting() {
        boolean result = interactPressed;
        interactPressed = false;
        return result;
    }

    /**
     * The shouldCloseDialog method will return a boolean value.
     * When the entity is closing the dialog, it will return true.
     */

    public boolean shouldCloseDialog() {
        if (closeDialog) {
        closeDialog = false;
            return true;
        }
        return false;
    }

    /**
     * The isInteractPressed method will return a boolean value.
     * When the entity is interacting, it will return true.
     */

    public boolean isInteractPressed() {
        return interactPressed;
    }

    /**
     * The addKeyBinds method will add the key bindings to the action and input maps.
     * It binds the keys to the actions defined above.
     * The key bindings are for movement (WASD) and interaction (E).
     */

    public void addKeyBinds() {
        // Press actions
        actionMap.put("UpMove", UpMove);
        actionMap.put("DownMove", DownMove);
        actionMap.put("LeftMove", LeftMove);
        actionMap.put("RightMove", RightMove);
        
        // Release actions
        actionMap.put("UpRelease", UpRelease);
        actionMap.put("DownRelease", DownRelease);
        actionMap.put("LeftRelease", LeftRelease);
        actionMap.put("RightRelease", RightRelease);

        // Movement Press key bindings
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "UpMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "DownMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "LeftMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "RightMove");
        
        // Movement Release key bindings
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "UpRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "DownRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "LeftRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "RightRelease");

        // Interact key bindings
        Action InteractPress = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                interactPressed = true;
            }
        };
        Action InteractRelease = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                interactPressed = false;
            }
        };

        actionMap.put("InteractPress", InteractPress);
        actionMap.put("InteractRelease", InteractRelease);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "InteractPress");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "InteractRelease");

        // Close dialog key binding
        actionMap.put("CloseDialogPress", CloseDialogPress);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "CloseDialogPress");
    }
}
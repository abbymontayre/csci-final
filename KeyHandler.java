import java.awt.event.*;
import javax.swing.*;
    /**
     * The KeyHandler class is responsible for handling the key presses and releases of the player.
     * Utilizing the ActionMap, It binds the keys to the actions defined below.
     * @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
     * @version 22 May 2025
     *
     * We have not discussed the Java language code in our program
     * with anyone other than our instructor or the teaching assistants
     * assigned to this course.
     *   
     * We have not used Java language code obtained from another student,
     * or any other unauthorized source, either modified or unmodified.
     *
     * If any Java language code or documentation used in our program
     * was obtained from another source, such as a textbook or website,
     * that has been clearly noted with a proper citation in the comments
     * of our program.
     */
    public class KeyHandler {
        private InputMap inputMap;
        private ActionMap actionMap;
        private boolean moveUp, moveDown, moveLeft, moveRight;
        private boolean interactPressed = false;
        private boolean closeDialog = false;

    /**
     * This constructor initializes the action and input maps for the given component.
     * It also sets all movement booleans to false as default.
     * @param cp The JComponent to which the key bindings will be applied.
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
    /***
     * This is the abstract action for the up movement.
     * It sets the moveUp boolean to true.
     */
    AbstractAction UpMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveUp = true;
        }
    };
    /***
     * This is the abstract action for the up release.
     * It sets the moveUp boolean to false.
     */
    AbstractAction UpRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveUp = false;
        }
    };
    /***
     * This is the abstract action for the down movement.
     * It sets the moveDown boolean to true.
     */
    AbstractAction DownMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown = true;
        }
    };
    /***
     * This is the abstract action for the down release.
     * It sets the moveDown boolean to false.
     */
    AbstractAction DownRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown = false;
        }
    };
    /***
     * This is the abstract action for the left movement.
     * It sets the moveLeft boolean to true.
     */
    AbstractAction LeftMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveLeft = true;
        }
    };
    /***
     * This is the abstract action for the left release.
     * It sets the moveLeft boolean to false.
     */
    AbstractAction LeftRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveLeft = false;
        }
    };
    /***
     * This is the abstract action for the right movement.
     * It sets the moveRight boolean to true.
     */
    AbstractAction RightMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveRight = true;
        }
    };
    /***
     * This is the abstract action for the right release.
     * It sets the moveRight boolean to false.
     */
    AbstractAction RightRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveRight = false;
        }
    }; 
    /***
     * This is the abstract action for the interact press.
     * It sets the interactPressed boolean to true.
     */
    AbstractAction InteractPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            interactPressed = true;
        }
    };
    /***
     * This is the abstract action for the close dialog press.
     * It sets the closeDialog boolean to true.
     */
    AbstractAction CloseDialogPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            closeDialog = true;
        }
    };

    /**
     * The isMoveUp getter method will return a boolean value.
     * When the entity is moving up, it will return true.
     * @return moveUp boolean
     */
    public boolean isMoveUp() {
        return moveUp;
    }

    /**
     * The isMoveDown getter method will return a boolean value.
     * When the entity is moving down, it will return true.
     * @return moveDown boolean
     */
    public boolean isMoveDown() {
        return moveDown;
    }

    /**
     * The isMoveLeft getter method will return a boolean value.
     * When the entity is moving left, it will return true.
     * @return moveLeft boolean
     */

    public boolean isMoveLeft() {
        return moveLeft;
    }

    /**
     * The isMoveRight getter method will return a boolean value.
     * When the entity is moving right, it will return true.
     * @return moveRight boolean
     */
    public boolean isMoveRight() {
        return moveRight;
    }

    /**
     * The isInteracting method will return a boolean value.
     * When the entity is interacting with another entity, it will return true.
     * @return interactPressed boolean
     */
    public boolean isInteracting() {
        boolean result = interactPressed;
        interactPressed = false;
        return result;
    }

    /**
     * The shouldCloseDialog method will return a boolean value.
     * When the entity is closing the dialog, it will return true.
     * @return closeDialog boolean
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
     * This method is used for the pressure plate interaction.
     * @return interactPressed boolean
     */
    public boolean isInteractPressed() {
        return interactPressed;
    }

    /**
     * The addKeyBinds method will bind the keys to their specific task using the action and input maps.
     * The key bindings are for movement (WASD) and interaction (E).
     */
    public void addKeyBinds() {
        actionMap.put("UpMove", UpMove);
        actionMap.put("DownMove", DownMove);
        actionMap.put("LeftMove", LeftMove);
        actionMap.put("RightMove", RightMove);
        
        actionMap.put("UpRelease", UpRelease);
        actionMap.put("DownRelease", DownRelease);
        actionMap.put("LeftRelease", LeftRelease);
        actionMap.put("RightRelease", RightRelease);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "UpMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "DownMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "LeftMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "RightMove");
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "UpRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "DownRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "LeftRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "RightRelease");

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

        actionMap.put("CloseDialogPress", CloseDialogPress);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "CloseDialogPress");
    }
}
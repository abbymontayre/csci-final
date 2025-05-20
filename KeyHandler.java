import java.awt.event.*;
import javax.swing.*;

public class KeyHandler {
    private InputMap inputMap;
    private ActionMap actionMap;
    private  boolean moveUp, moveDown, moveLeft, moveRight;
    private boolean interactPressed = false;
    private boolean closeDialog = false;

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

    AbstractAction UpMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveUp = true;
        }
    };

    AbstractAction UpRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveUp = false;
        }
    };

    AbstractAction DownMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown = true;
        }
    };

    AbstractAction DownRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveDown = false;
        }
    };

    AbstractAction LeftMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveLeft = true;
        }
    };

    AbstractAction LeftRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveLeft = false;
        }
    };

    AbstractAction RightMove = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveRight = true;
        }
    };

    AbstractAction RightRelease = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            moveRight = false;
        }
    };

    AbstractAction InteractPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            interactPressed = true;
        }
    };

    AbstractAction CloseDialogPress = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            closeDialog = true;
        }
    };

    public boolean isMoveUp() {
        return moveUp;
    }
    public boolean isMoveDown() {
        return moveDown;
    }
    public boolean isMoveLeft() {
        return moveLeft;
    }
    public boolean isMoveRight() {
        return moveRight;
    }

    public boolean isInteracting() {
        boolean result = interactPressed;
        interactPressed = false;
        return result;
    }

    public boolean shouldCloseDialog() {
        boolean result = closeDialog;
        closeDialog = false;
        return result;
    }

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
        actionMap.put("InteractPress", InteractPress);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "InteractPress");

        // Close dialog key binding
        actionMap.put("CloseDialogPress", CloseDialogPress);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "CloseDialogPress");
    }
}
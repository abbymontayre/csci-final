import java.awt.event.*;
import javax.swing.*;

public class KeyHandler {
    private InputMap inputMap;
    private ActionMap actionMap;
    private  boolean moveUp, moveDown, moveLeft, moveRight;

    public KeyHandler(JComponent cp) {
        actionMap = cp.getActionMap();
        inputMap = cp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;
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

        // Press key bindings
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "UpMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "DownMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "LeftMove");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "RightMove");
        
        // Release key bindings
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "UpRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "DownRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "LeftRelease");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "RightRelease");
    }
}
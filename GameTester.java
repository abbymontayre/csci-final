import javax.swing.*;

public class GameTester {
    private JFrame f;
    private GameCanvas gamePanel;

    public GameTester() {
        f = new JFrame("Game Tester");
        gamePanel = new GameCanvas();
    }

    public void setUpGUI() {
        f.setSize(1024, 768);
        f.setTitle("Game Tester");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(gamePanel);
        f.setVisible(true);
        gamePanel.requestFocusInWindow();
    }

}
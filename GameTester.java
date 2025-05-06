import javax.swing.*;

public class GameTester {
    private JFrame f;
    private GamePanel gamePanel;

    public GameTester() {
        f = new JFrame("Game Tester");
        gamePanel = new GamePanel();
    }

    public void setUpGUI() {
        f.setSize(800, 600);
        f.setTitle("Game Tester");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(gamePanel);
        f.setVisible(true);
        gamePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        GameTester tester = new GameTester();
        tester.setUpGUI();
    }
}
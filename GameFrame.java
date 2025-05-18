import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(int playerId) {
        setTitle("LAN Square Game - Player " + playerId);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        GameCanvas gameCanvas = new GameCanvas(playerId);
        add(gameCanvas);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

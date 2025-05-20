import javax.swing.*;


public class GameFrame {
    private GameCanvas gameCanvas;
    private JFrame frame;
    private int playerID;


    public GameFrame() {
        frame = new JFrame();
        gameCanvas = new GameCanvas();
    }
    

    public void setUpGUI() {
        if (gameCanvas == null) {
            System.out.println("Error: GameCanvas not initialized. Connection failed.");
            return;
        }
        frame.setTitle("Who Goes There? - Player");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameCanvas);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }
}
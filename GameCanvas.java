import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameCanvas extends JPanel {

    private Player Player1, Player2;
    private Map map;
    private Portal portal1, portal2;    

    public GameCanvas() {
        setPreferredSize(new Dimension(800, 600));  // Ensure it gets size
        setBackground(Color.BLACK);                 // Set background color
        setFocusable(true);

        map = new Map();

        this.Player1 = new Player(96, 96, 32, 32); 
        this.Player2 = new Player(896, 608, 32, 32);

        this.portal1 = new Portal(350, 172, 32, 32); 
        this.portal2 = new Portal(110, 600, 32, 32);
        
        portal1.addPlayer(Player1);
        portal1.addPlayer(Player2);
        portal1.setDestination(portal2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        map.draw(g);

        g.setColor(Color.GREEN);
        portal1.draw(g);
        portal2.draw(g);

        g.setColor(Color.BLUE);
        Player1.draw(g);
        g.setColor(Color.YELLOW);
        Player2.draw(g);
    }
}

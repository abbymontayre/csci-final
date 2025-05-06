import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    private Player Player1, Player2;
    private GameMap gameMap1, gameMap2;
    private Map map;

    public GamePanel(){
        setFocusable(true);
        
        this.gameMap1 = new GameMap(100, 100, 250, 150); 
        this.gameMap2 = new GameMap(350, 150, 25, 50);

        map = new Map();
        map.addMap(gameMap1);
        map.addMap(gameMap2);

        this.Player1 = new Player(150, 150, 10, 10); 
        this.Player2 = new Player(200, 200, 10, 10);

        addKeyBindings_P1();     
        addKeyBindings_P2();   
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw your game objects here
        
        g.setColor(Color.RED);
        gameMap1.draw(g);
        g.setColor(Color.RED);
        gameMap2.draw(g);

        g.setColor(Color.BLUE);
        Player1.draw(g);

        g.setColor(Color.YELLOW);
        Player2.draw(g);
    }

    private void addKeyBindings_P1(){
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "moveUp1");
        im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown1");
        im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft1");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight1");

        am.put("moveUp1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player1.move(0, -5, map);
                repaint();
            }
        });

        am.put("moveDown1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player1.move(0, 5, map);
                repaint();
            }
        });

        am.put("moveLeft1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player1.move(-5, 0, map);
                repaint();
            }
        });

        am.put("moveRight1", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player1.move(5, 0, map);
                repaint();
            }
        });
    }

    private void addKeyBindings_P2(){
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("W"), "moveUp2");
        im.put(KeyStroke.getKeyStroke("S"), "moveDown2");
        im.put(KeyStroke.getKeyStroke("A"), "moveLeft2");
        im.put(KeyStroke.getKeyStroke("D"), "moveRight2");

        am.put("moveUp2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player2.move(0, -5, map);
                repaint();
            }
        });

        am.put("moveDown2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player2.move(0, 5, map);
                repaint();
            }
        });

        am.put("moveLeft2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player2.move(-5, 0, map);
                repaint();
            }
        });

        am.put("moveRight2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player2.move(5, 0, map);
                repaint();
            }
        });
    }
}
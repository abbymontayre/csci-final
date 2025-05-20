import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class GameCanvas extends JComponent {
    private ArrayList<Entity> entities;
    private KeyHandler keyHandler;
    private int playerID;
    private Player currentPlayer;
    private Player otherPlayer;
    
    // Networking components
    private Socket socket;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    public GameCanvas() {
        this.setPreferredSize(new Dimension(Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        entities = new ArrayList<>();
        setFocusable(true);
        requestFocusInWindow();
        keyHandler = new KeyHandler(this);
        keyHandler.addKeyBinds();
        
        connectToServer(); // Connect to server before setting up entities
        setEntities(entities);

        Timer timer = new Timer(16, e -> {
            update();
            repaint();
        });
        timer.start();
    }
    
    private void connectToServer() {
        try {
            socket = new Socket("localhost", Constants.GAME_SETTINGS.PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("Player #: " + playerID);
            
            if(playerID == 1) {
                System.out.println("Waiting for player 2 to connect...");
            }
            
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();
        } catch (IOException e) {
            System.out.println("IOException: from connectToServer");
        }
    }
    
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
        // Create both players but only control the one matching playerID
        Player player1 = new Player(1, 100, 100, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        Player player2 = new Player(2, 600, 100, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        
        if (playerID == 1) {
            player1.setKeyHandler(keyHandler);
            currentPlayer = player1;
            otherPlayer = player2;
        } else {
            player2.setKeyHandler(keyHandler);
            currentPlayer = player2;
            otherPlayer = player1;
        }
        
        entities.add(player1);
        entities.add(player2);
    }
    
    public double getPlayerX() {
        return currentPlayer.getX();
    }

    public double getPlayerY() {
        return currentPlayer.getY();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOtherPlayer() {
        return otherPlayer;
    }
    
    private void update() {
        if (currentPlayer != null && otherPlayer != null) {
            currentPlayer.update();
            otherPlayer.setX(rfsRunnable.getOtherPlayerX());
            otherPlayer.setY(rfsRunnable.getOtherPlayerY());
        }
    }

    private class ReadFromServer implements Runnable {
        private DataInputStream in;
        private double otherPlayerX, otherPlayerY;

        public ReadFromServer(DataInputStream in) {
            this.in = in;
        }
        
        public void run() {
            try {
                while(true) {
                    otherPlayerX = in.readDouble();
                    otherPlayerY = in.readDouble();
                }
            } catch (Exception e) {
                System.out.println("IOException: from ReadFromServer run()");
            }
        }
        
        public void waitForStartMsg() {
            try {
                String startMsg = in.readUTF();
                System.out.println("Msg from server: " + startMsg);
                new Thread(this).start();
                new Thread(wtsRunnable).start();
            } catch (IOException e) {
                System.out.println("IOException: from waitForStartMsg");
            }
        }
        
        public double getOtherPlayerX() { return otherPlayerX; }
        public double getOtherPlayerY() { return otherPlayerY; }
    }

    private class WriteToServer implements Runnable {
        private DataOutputStream out;

        public WriteToServer(DataOutputStream out) {
            this.out = out;
        }
        
        public void run() {
            try {
                while(true) {
                    if (currentPlayer != null) {
                        out.writeDouble(currentPlayer.getX());
                        out.writeDouble(currentPlayer.getY());
                        out.flush();
                    }
                    Thread.sleep(16);
                }
            } catch (Exception e) {
                System.out.println("IOException: from WriteToServer run()");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(135, 177, 209));
        g2d.fillRect(0, 0, Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT);
        for (Entity entity : entities) {
            entity.draw(g2d);
        }
    }
}

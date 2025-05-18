import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends JComponent implements KeyHandler.MovementSender {
    private List<GuideItem> items;    
    
    // Player entities
    private final Entity player;
    private Entity opponent;
    
    // Networking
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private KeyHandler keyHandler;
    private boolean popupVisible;
    
    public GameCanvas(int playerId) {        
        
        popupVisible = false;
        
        // Initialize player and opponent entities
        items = new ArrayList<>();
        items.add(new GuideItem(1, 500, 400, "Ancient Tome", "This ancient book contains forgotten knowledge...\n\n" +"blasdblsadbasdjhskahdajdahwkajs", Constants.GAME_SETTINGS.PANEL_WIDTH, Constants.GAME_SETTINGS.PANEL_HEIGHT));
        items.add(new GuideItem(2, 300, 400, "Gaggatondra Tome", "okay homo.\n\n" +"blasdblsadbasdjhskahdajdahwkajs", Constants.GAME_SETTINGS.PANEL_WIDTH, Constants.GAME_SETTINGS.PANEL_HEIGHT));
        this.player = new Player(playerId, 100, 100, 50, 50, 5);
        this.opponent = new Player(playerId == 1 ? 2 : 1, 100, 100, 50, 50, 5);

        setPreferredSize(new Dimension(Constants.GAME_SETTINGS.PANEL_WIDTH, Constants.GAME_SETTINGS.PANEL_HEIGHT));
        keyHandler = new KeyHandler(this);
        setFocusable(true);
        requestFocusInWindow();
        addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            // Reset key states when focus is regained
            keyHandler.clearKey(null);
        }
    });
        
        connectToServer();
        startReceiverThread();
    }

    private void connectToServer() {
        try {
            String serverIP = System.getProperty("server.ip", "localhost");
            socket = new Socket(serverIP, Constants.GAME_SETTINGS.PORT);
            socket.setTcpNoDelay(true);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            System.exit(1);
        }
    }

    private void startReceiverThread() {
        new Thread(() -> {
            try {
                while (true) {
                    String gameState = in.readLine();
                    if (gameState != null) {
                        updateGameState(gameState);
                        checkInteractions(); // Add this line
                        repaint();
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection lost: " + e.getMessage());
            }
        }).start();
    }

    private void updateGameState(String gameState) {
        String[] parts = gameState.split("#");
        
        // Update players
        String[] playersData = parts[0].split("\\|");
        for (String playerData : playersData) {
            parts = playerData.split(",");
            if (parts.length >= 3) {
                int id = Integer.parseInt(parts[0]);
                int px = Integer.parseInt(parts[1]);
                int py = Integer.parseInt(parts[2]);
                
                if (id != player.getId()) {
                    opponent.setX(px);
                    opponent.setY(py);
                }
            }
        }
    }

    @Override
    public void sendMovement(int xVel, int yVel) {
        if (!popupVisible) {  // Only allow movement when popup isn't visible
            player.move(xVel, yVel);
            player.checkBounds(Constants.GAME_SETTINGS.PANEL_WIDTH, Constants.GAME_SETTINGS.PANEL_HEIGHT);
            
            if (xVel != 0 || yVel != 0) {
                out.println(player.getX() + "," + player.getY());
            }
        }
        repaint();
    }


    private void checkInteractions() {
        if (popupVisible) {
            if (keyHandler.isKeyPressed("SPACE")) {
                for (GuideItem book : items) {
                    if (book.isDialogVisible()) {
                        book.hideDialog();
                        popupVisible = false;
                        keyHandler.clearKey("SPACE");
                    }
                }
            }
            return;
        }

        if (this.hasFocus()) {
            for (GuideItem book : items) {
                if (book.checkCollision(player) && keyHandler.isKeyPressed("E")) {
                    book.showDialog();
                    popupVisible = true;
                    keyHandler.clearKey("E");
                    break;
                }
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw items
        for (GuideItem item : items) {
            item.render(g);
        }
        
        // Draw game world (background, entities, etc.)
        player.render(g);
        opponent.render(g);
        
        
        // Draw popups LAST (on top of everything)
        for (GuideItem item : items) {
            if (item.isDialogVisible()) {
                item.renderPopup(g);
            }
        }
            
        // Draw debug info
        g.setColor(Color.BLACK);
        g.drawString("Player " + player.getId() + " (" + player.getX() + "," + player.getY() + ")", 10, 20);
        g.drawString("Opponent (" + opponent.getX() + "," + opponent.getY() + ")", 10, 40);
        g.drawString("Game Panel: " + Constants.GAME_SETTINGS.PANEL_WIDTH + "x" + Constants.GAME_SETTINGS.PANEL_HEIGHT, 10, 60);
    }
}
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class GameCanvas extends JComponent {
    private ArrayList<Entity> entities;
    private KeyHandler keyHandler;
    private int playerID;
    private Player currentPlayer;
    private Player otherPlayer;
    private boolean haveWon;
    private WinScreen winScreen = new WinScreen();
    private String filePath;

    
    // Networking components
    private Socket socket;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    private String serverIP;
    private Map map;
    private LevelManager levelManager;
    private Portal portal1, portal2;
    private boolean otherPlayerInPortal = false;
    
    private ArrayList<ArrayList<GuideItem>> guideItemsPerLevel = new ArrayList<>();
    private ArrayList<GuideItem> currentGuideItems = new ArrayList<>();
    
    public GameCanvas(String serverIP) {
        this.serverIP = serverIP;
        filePath = "bgMusic.wav";
        haveWon = false;
        this.setPreferredSize(new Dimension(Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        entities = new ArrayList<>();
        levelManager = new LevelManager();
        setFocusable(true);
        requestFocusInWindow();
        keyHandler = new KeyHandler(this);
        keyHandler.addKeyBinds();
        
        connectToServer();
        LoopMusic(filePath);
        setEntities(entities);

        Timer timer = new Timer(16, e -> {
            update();
            repaint();
        });
        timer.start();

        this.add(winScreen);
    }

    /***
     * The LoopMusic method is responsible for finding, playing, and looping the background music for the animation.
     * Try-Catch element is used here to look out for possible exception errors per loop iteration.
     * @param location file location of the background music
     */
    public static void LoopMusic(String location){
        try {
            File musicPath = new File(location);
            if (musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();

            } else {
                System.out.println("Cant find File");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void connectToServer() {
        try {
            System.out.println("Attempting to connect to server at: " + serverIP);
            socket = new Socket(serverIP, Constants.GAME_SETTINGS.PORT);
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
            System.out.println("Failed to connect to server at " + serverIP + ": " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "Failed to connect to server at " + serverIP + "\nPlease check if the server is running and try again.", 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
        Map map = levelManager.getCurrentMap();
        int[][] playerPositions = {
            {100, 100, 831, 447}, // Level 1
            {891, 63, 63, 453}, // Level 2
            {315, 315, 894, 450}  // Level 3
        };
        int[][] portalPositions = {
            // portal 1, portal 2
            {768, 69, 123, 645}, // Level 1
            {63, 132, 825, 453}, // Level 2
            {894, 321, 63, 453}  // Level 3
        };
        int lvl = levelManager.getCurrentLevel();
        Player player1 = new Player(1, playerPositions[lvl][0], playerPositions[lvl][1], Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
        Player player2 = new Player(2, playerPositions[lvl][2], playerPositions[lvl][3], Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
        if (playerID == 1) {
            player1.setKeyHandler(keyHandler);
            currentPlayer = player1;
            otherPlayer = player2;
        } else {
            player2.setKeyHandler(keyHandler);
            currentPlayer = player2;
            otherPlayer = player1;
        }
        // --- GuideItems per level ---
        if (guideItemsPerLevel.isEmpty()) {
            // Level 1
            ArrayList<GuideItem> g1 = new ArrayList<>();
            g1.add(new GuideItem(258, 69, "Ancient Tome", "Three must rise… in silence they move…", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
            // Level 2
            ArrayList<GuideItem> g2 = new ArrayList<>();
            g2.add(new GuideItem(200, 200, "Old Book", "…one after another, with time between each.", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
            g2.add(new GuideItem(100, 250, "Diary of Hosimachi Suisei", "yagoo…where am i?", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
            // Level 3
            ArrayList<GuideItem> g3 = new ArrayList<>();
            g3.add(new GuideItem(300, 300, "Mysterious Encyclopedia", "Work together to escape.", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
            g3.add(new GuideItem(300, 300, "Confusing Code", "Work together to escape.", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
            g3.add(new GuideItem(300, 300, "Mysterious Encyclopedia", "Work together to escape.", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
            guideItemsPerLevel.add(g1);
            guideItemsPerLevel.add(g2);
            guideItemsPerLevel.add(g3);
        }
        // Remove old GuideItems from entities
        for (GuideItem gi : currentGuideItems) {
            entities.remove(gi);
        }
        // Add new GuideItems for this level
        currentGuideItems = guideItemsPerLevel.get(lvl);
        for (GuideItem gi : currentGuideItems) {
            gi.setPlayerAndHandler(currentPlayer, keyHandler);
            entities.add(gi);
        }
        // --- Portals ---
        if (portal1 == null) {
            portal1 = new Portal(1, portalPositions[lvl][0], portalPositions[lvl][1], Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
            portal2 = new Portal(2, portalPositions[lvl][2], portalPositions[lvl][3], Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
            portal1.setPlayer(currentPlayer);
            portal2.setPlayer(otherPlayer);
            entities.add(portal1);
            entities.add(portal2);
        } else {
            portal1.setX(portalPositions[lvl][0]);
            portal1.setY(portalPositions[lvl][1]);
            portal2.setX(portalPositions[lvl][2]);
            portal2.setY(portalPositions[lvl][3]);
            // Update player references in case they changed
            portal1.setPlayer(currentPlayer);
            portal2.setPlayer(otherPlayer);
        }
        // Add players
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
            
            // Update all entities
            for (Entity entity : entities) {
                entity.update();
            }
            // Check if both players are in their portals
            boolean currentInPortal = portal1.checkCollision(currentPlayer);
            boolean otherInPortal = portal2.checkCollision(otherPlayer);
            if (currentInPortal && otherInPortal) {
                if (levelManager.nextLevel()) {
                    setEntities(new ArrayList<>());
                } else {
                    // Game finished, maybe show a message or reset
                    levelManager.reset();
                    haveWon = true;
                    setEntities(new ArrayList<>());
                }
            }
        }
    }

    private class ReadFromServer implements Runnable {
        private DataInputStream in;
        private double otherPlayerX, otherPlayerY;
        private boolean otherPlayerFacingLeft;

        public ReadFromServer(DataInputStream in) {
            this.in = in;
        }
        
        public void run() {
            try {
                while(true) {
                    otherPlayerX = in.readDouble();
                    otherPlayerY = in.readDouble();
                    otherPlayerFacingLeft = in.readBoolean();
                    otherPlayerInPortal = in.readBoolean();
                    if (otherPlayer != null) {
                        otherPlayer.setFacingLeft(otherPlayerFacingLeft);
                    }
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
                        out.writeBoolean(currentPlayer.isFacingLeft());
                        boolean currentInPortal = portal1.checkCollision(currentPlayer);
                        out.writeBoolean(currentInPortal);
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
        g2d.setColor(new Color(255, 255, 255));
        g2d.fillRect(0, 0, Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT);
        levelManager.getCurrentMap().draw(g2d);
        GuideItem visibleGuideItem = null;
        for (Entity entity : entities) {
            entity.draw(g2d);
            if (entity instanceof GuideItem && ((GuideItem)entity).isDialogVisible()) {
                visibleGuideItem = (GuideItem)entity;
            }
        }
        if (portal1.checkCollision(currentPlayer)) {
            g2d.drawString("Waiting for the other player to enter the portal...", 100, 100);
        }
        if (portal2.checkCollision(otherPlayer)) {
            g2d.drawString("Waiting for you to enter the portal...", 100, 120);
        }
        if (visibleGuideItem != null) {
            visibleGuideItem.drawPopup(g);
        }

        if (haveWon) {
            // When player wins
            winScreen.setVisible(true); // You need to add this setter in WinScreen
            winScreen.render(g2d);
        }
    }
}

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
    private Portal portal1, portal2;
    private boolean otherPlayerInPortal;
    
    private ArrayList<GuideItem> guideItems = new ArrayList<>();
    private ArrayList<Plate> plates = new ArrayList<>();
    
    public GameCanvas(String serverIP) {
        this.serverIP = serverIP;
        this.setPreferredSize(new Dimension(Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        filePath = "bgMusic.wav";
        haveWon = false;
        entities = new ArrayList<>();
        map = new Map();
        keyHandler = new KeyHandler(this);
        keyHandler.addKeyBinds();
        
        connectToServer();
        LoopMusic(filePath);
        setupEntities();

        Timer timer = new Timer(16, e -> {
            update();
            repaint();
        });
        timer.start();

        this.add(winScreen);
    }

    /***
     * The LoopMusic method plays background music continuously for the game.
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
    
    private void setupEntities() {
        if (map.getMapID() == 0) {
            Player player1 = new Player(1, 100, 100, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
            Player player2 = new Player(2, 831, 447, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
            
            if (playerID == 1) {
                currentPlayer = player1;
                otherPlayer = player2;
                player1.setKeyHandler(keyHandler);
            } else {
                currentPlayer = player2;
                otherPlayer = player1;
                player2.setKeyHandler(keyHandler);
            }

            // Set up guide item
            GuideItem guideItem = new GuideItem(258, 469, "A Poet's Keepsake", 
                "Begin at dawn when one stands tall, Skip the noon, let the evening call. Catch the twilight in between, The ticking riddle sits unseen.",       
                Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT);
            guideItem.setPlayerAndHandler(currentPlayer, keyHandler);
            guideItems.add(guideItem);
            entities.add(guideItem);

            // Set up plates
            int[][] platePositions = {{200, 200}, {400, 200}, {600, 200}};
            for (int i = 0; i < 3; i++) {
                Plate plate = new Plate(i + 1, 
                    platePositions[i][0], 
                    platePositions[i][1], 
                    Constants.GAME_SETTINGS.TILE_SIZE, 
                    Constants.GAME_SETTINGS.TILE_SIZE);
                plate.setPlayerAndHandler(currentPlayer, keyHandler);
                plates.add(plate);
                entities.add(plate);
            }
            portal1 = new Portal(1, 768, 69, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
            portal2 = new Portal(2, 123, 630, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
            
            // Set portal-player relationships
            if (playerID == 1) {
                portal1.setPlayer(currentPlayer);
                portal2.setPlayer(otherPlayer);
            } else {
                portal2.setPlayer(currentPlayer);
                portal1.setPlayer(otherPlayer);
            }
    
            entities.add(portal1);
            entities.add(portal2);
            entities.add(player1);
            entities.add(player2);
        } else if (map.getMapID() == 1){
            Player player1 = new Player(1, 961, 65, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
            Player player2 = new Player(2, 193, 7, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
            
            if (playerID == 1) {
                currentPlayer = player1;
                otherPlayer = player2;
                player1.setKeyHandler(keyHandler);
            } else {
                currentPlayer = player2;
                otherPlayer = player1;
                player2.setKeyHandler(keyHandler);
            }

            // // Set up guide item
            // GuideItem guideItem = new GuideItem(258, 469, "A Poet's Keepsake", 
            //     "Begin at dawn when one stands tall, Skip the noon, let the evening call. Catch the twilight in between, The ticking riddle sits unseen.",       
            //     Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT);
            // guideItem.setPlayerAndHandler(currentPlayer, keyHandler);
            // guideItems.add(guideItem);
            // entities.add(guideItem);

            // Set up plates
        //     int[][] platePositions = {{200, 200}, {400, 200}, {600, 200}};
        //     for (int i = 0; i < 3; i++) {
        //         Plate plate = new Plate(i + 1, 
        //             platePositions[i][0], 
        //             platePositions[i][1], 
        //             Constants.GAME_SETTINGS.TILE_SIZE, 
        //             Constants.GAME_SETTINGS.TILE_SIZE);
        //         plate.setPlayerAndHandler(currentPlayer, keyHandler);
        //         plates.add(plate);
        //         entities.add(plate);
        // }
        // Set up portals
        portal1 = new Portal(1, 768, 69, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        portal2 = new Portal(2, 123, 630, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        
        // Set portal-player relationships
        if (playerID == 1) {
            portal1.setPlayer(currentPlayer);
            portal2.setPlayer(otherPlayer);
        } else {
            portal2.setPlayer(currentPlayer);
            portal1.setPlayer(otherPlayer);
        }

        entities.add(portal1);
        entities.add(portal2);
        entities.add(player1);
        entities.add(player2);
        }
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

            // Check if both players are in portals and sequence is complete
            if (Plate.isSequenceComplete()) {
                boolean currentInPortal = (playerID == 1) ? 
                    portal1.checkCollision(currentPlayer) : 
                    portal2.checkCollision(currentPlayer);
                boolean otherInPortal = (playerID == 1) ? 
                    portal2.checkCollision(otherPlayer) : 
                    portal1.checkCollision(otherPlayer);

                if (currentInPortal && otherInPortal) {
                    if(map.getMapID() < 2){
                        map.incrementMapID();
                        entities.clear();
                        setupEntities();
                    } else {
                        haveWon = true;
                        entities.clear();
                    }
                }
            }
        }
    }

    private class ReadFromServer implements Runnable {
        private DataInputStream in;
        private String otherPlayerData;
        private int otherMapID;
        private int sequencePosition;
        private boolean[] plateStates = new boolean[3];

        public ReadFromServer(DataInputStream in) {
            this.in = in;
        }
        
        public void run() {
            try {
                while(true) {
                    otherPlayerData = in.readUTF();
                    // Read plate states from other player
                    sequencePosition = in.readInt();
                    for (int i = 0; i < 3; i++) {
                        plateStates[i] = in.readBoolean();
                    }
                    
                    if (otherPlayer != null) {
                        String[] playerData = otherPlayerData.split(",");
                        otherPlayer.setX(Double.parseDouble(playerData[1]));
                        otherPlayer.setY(Double.parseDouble(playerData[2]));
                        otherPlayer.setFacingLeft(Boolean.parseBoolean(playerData[3]));
                        otherPlayerInPortal = Boolean.parseBoolean(playerData[4]);
                        otherMapID = Integer.parseInt(playerData[5]);
                    }
                    
                    // Update plate states from other player
                    if (!plates.isEmpty()) {
                        for (int i = 0; i < plates.size(); i++) {
                            Plate plate = plates.get(i);
                            plate.setActivated(plateStates[i]);
                        }
                        Plate.setCurrentSequencePosition(sequencePosition);
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
        
        public double getOtherPlayerX() { 
            String[] playerData = otherPlayerData.split(",");
            return Double.parseDouble(playerData[1]); 
        }
        
        public double getOtherPlayerY() { 
            String[] playerData = otherPlayerData.split(",");
            return Double.parseDouble(playerData[2]); 
        }
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
                        // Check portal collision and sequence completion
                        boolean portalCollision = (playerID == 1) ? 
                            portal1.checkCollision(currentPlayer) : 
                            portal2.checkCollision(currentPlayer);
                        boolean inPortal = portalCollision && Plate.isSequenceComplete();
                        
                        // Create player data string              
                        String playerData = String.format("%d,%.2f,%.2f,%b,%b,%d",
                            playerID,
                            currentPlayer.getX(),
                            currentPlayer.getY(),
                            currentPlayer.isFacingLeft(),
                            inPortal,
                            map.getMapID()
                        );
                        out.writeUTF(playerData);
                        
                        // Send plate states
                        out.writeInt(Plate.getCurrentSequencePosition());
                        for (Plate plate : plates) {
                            out.writeBoolean(plate.isActivated());
                        }
                        out.flush();

                        // Sending data
                        System.out.println("Client sent to server: " + playerData);
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
        map.draw(g2d);
        
        GuideItem visibleGuideItem = null;
        for (Entity entity : entities) {
            entity.draw(g2d);
            if (entity instanceof GuideItem && ((GuideItem)entity).isDialogVisible()) {
                visibleGuideItem = (GuideItem)entity;
            }
        }

        // Draw sequence completion status
        if (!Plate.isSequenceComplete()) {
            g2d.setColor(Color.RED);
            g2d.drawString("Activate the plates in the correct sequence!", 100, 50);
        } else {
            g2d.setColor(Color.GREEN);
            g2d.drawString("Sequence complete! Portals are now active!", 100, 50);
        }

        if (portal1.checkCollision(currentPlayer) || portal2.checkCollision(otherPlayer)) {
            if (Plate.isSequenceComplete()) {
                g2d.drawString("Waiting for the other player to enter the portal...", 100, 100);
            } else {
                g2d.drawString("Complete the plate sequence first!", 100, 100);
            }
        }

        if (visibleGuideItem != null) {
            visibleGuideItem.drawPopup(g);
        }

        if (haveWon) {
            winScreen.setVisible(true);
            winScreen.draw(g2d);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;
/**
 * The GameCanvas class is responsible for creating the game canvas for each player.
 * It is also responsible for sending, reading, assigning, and updating the entities and the game loop.
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)   
    @version 22 May 2025

    We have not discussed the Java language code in our program
    with anyone other than our instructor or the teaching assistants
    assigned to this course.
    
    We have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.

    If any Java language code or documentation used in our program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of our program.
 */
public class GameCanvas extends JComponent {
    private ArrayList<Entity> entities;
    private KeyHandler keyHandler;
    private int playerID;
    private Player currentPlayer;
    private Player otherPlayer;
    private boolean haveWon;
    private WinScreen winScreen = new WinScreen();
    private String filePath;
    
    private Socket socket;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    private String serverIP;
    private Map map;
    private Portal portal1, portal2;
    private boolean otherPlayerInPortal;
    
    private ArrayList<GuideItem> guideItems = new ArrayList<>();
    private ArrayList<Plate> plates = new ArrayList<>();
    /**
     * The GameCanvas constructor initializes the game canvas for each player.
     * After receiving the server IP address from GameFrame, the constructor will connect to the server, make the GUI for each player and set up all the needed elements for the game to run.
     * @param serverIP The Server IP Address of the Host requested by GameFrame.
     */
    public GameCanvas(String serverIP) {
        this.serverIP = serverIP;
        filePath = "bgMusic.wav";
        haveWon = false;
        this.setPreferredSize(new Dimension(Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        entities = new ArrayList<>();
        map = new Map();
        setFocusable(true);
        requestFocusInWindow();
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
     * This mutator method is used to play the background music of the game.
     * As with most IO operations, try-catch is used to look out for possible exception errors per loop iteration.
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
    /***
     * This mutator method is used to connect the client to the server.
     * Two threads are created, one for reading from the server and one for writing to the server to ensure constant networking updates.
     */
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
    /***
     * This mutator method is used to set up all the entities for the game,include the players, guide items, plates, and portals.
     * It also sets up needed relationships between other classes such as the keybinds for the players and the guide items, as well as the portal-player relationships.
     */
    private void setupEntities() {

        Player player1 = new Player(1, 896, 192, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
        Player player2 = new Player(2, 128, 448, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE, map);
        
        if (playerID == 1) {
            currentPlayer = player1;
            otherPlayer = player2;
            player1.setKeyHandler(keyHandler);
        } else {
            currentPlayer = player2;
            otherPlayer = player1;
            player2.setKeyHandler(keyHandler);
        }

        guideItems.add(new GuideItem(384, 320, "The King's Prologue","On his first day as King, he looked to the western sky as someone told him lately that everyone deserved the chance to fly.", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        guideItems.add(new GuideItem(896, 448, "The Queen's Demise","Her name was Anne Boleyn, She was the second woman the King ever truly loved. Though, such love was short-lived as he often went eastward, entertaining a new innocent dame. When word broke out through Lady Whistledown's letters, she ran off to the forest and was beheaded.", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        guideItems.add(new GuideItem(192, 128, "Diary of a Rookie Rook","It was my third day on the job. I was only getting the hang of things until..,.you know...this happened..I mean how was I supposed to know that I shouldn't castle the king this early? Whatever..I'm stuck in this corner now, might as well get comfortable up here.", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        guideItems.add(new GuideItem(64, 640, "Forthcoming of the New Bishop","I stand here today in the middle of a worldly crisis. We beg for no wars, and peace for all yet dangers lie creeping. Something tells me we must go beneath the veil of our hopes of change and ask ourselves, 'For Whom do we evoke change?'", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        guideItems.add(new GuideItem(768, 128, "The Knight of my Night","It had been 5 months since I have last seen my husband, and it has been amazing! Ever since I found out from Lady Whistledown that he has had an affair with not one but twelve courtesans?! In a Kingdom, where I am in the center of his drama, I knew I had to run away. That's when that selfless Knight helped staged my beheading. I wished I had gotten his name, perhaps he wouldve made a better husband than that lousy king!", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        guideItems.add(new GuideItem(704, 640, "Suisei's Diary","yagoo....where am i", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));
        guideItems.add(new GuideItem(64, 256, "List of all the Zelda Games","We wont be actually able to fit the list of all the Zelda games, so we made our own. We hope you like it!", Constants.GAME_SETTINGS.SCREEN_WIDTH, Constants.GAME_SETTINGS.SCREEN_HEIGHT));

        for (GuideItem item : guideItems) {
            item.setPlayerAndHandler(currentPlayer, keyHandler);
            entities.add(item);
        }

        int[][] platePositions = {
            {128, 320},
            {896, 320},
            {576, 64},
            {512,640},
            {320,448}
        };
        for (int i = 0; i < 5; i++) {
            Plate plate = new Plate(i + 1, 
                platePositions[i][0], 
                platePositions[i][1], 
                Constants.GAME_SETTINGS.TILE_SIZE, 
                Constants.GAME_SETTINGS.TILE_SIZE);
            plate.setPlayerAndHandler(currentPlayer, keyHandler);
            plates.add(plate);
            entities.add(plate);
        }

        portal1 = new Portal(1, 64, 64, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        portal2 = new Portal(2, 896, 640, Constants.GAME_SETTINGS.TILE_SIZE, Constants.GAME_SETTINGS.TILE_SIZE);
        
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
    /**
     * This getter method is used to get the x-coordinate of the current player.
     * This is mainly used for the networking updates.
     * @return The x-coordinate of the current player.
     */
    public double getPlayerX() {
        return currentPlayer.getX();
    }
    /**
     * This getter method is used to get the y-coordinate of the current player.
     * This is mainly used for the networking updates.
     * @return The y-coordinate of the current player.
     */
    public double getPlayerY() {
        return currentPlayer.getY();
    }
    /**
     * This getter method is used to get the current player.
     * This is mainly used for the networking updates.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * This getter method is used to get the other player.
     * This is mainly used for the networking updates.
     * @return The other player.
     */
    public Player getOtherPlayer() {
        return otherPlayer;
    }
    /***
     * This mutator method ensures that all entities are updated per loop iteration.
     * It also checks for certain flags such as the sequence completion, the portal collision, and the win condition.
     */
    private void update() {
        if (currentPlayer != null && otherPlayer != null) {
            currentPlayer.update();
            otherPlayer.setX(rfsRunnable.getOtherPlayerX());
            otherPlayer.setY(rfsRunnable.getOtherPlayerY());
            
            for (Entity entity : entities) {
                entity.update();
            }

            if (Plate.isSequenceComplete()) {

                boolean currentInPortal = (playerID == 1) ? 
                    portal1.checkCollision(currentPlayer) : 
                    portal2.checkCollision(currentPlayer);
                
                boolean otherInPortal = rfsRunnable.isOtherPlayerInPortal();

                if (currentInPortal && otherInPortal) {
                    haveWon = true;
                    entities.clear();
                }
            }
        }
    }
    /***
     * This inner class focuses on the reading aspect of the networking.
     * It reads the data from the server and updates the other player's data.
     */
    private class ReadFromServer implements Runnable {
        private DataInputStream in;
        private String otherPlayerData;
        private int otherSequencePosition;
        private boolean[] otherPlateStates = new boolean[5];
        /***
         * This constructor initializes the DataInputStream needed for the reading process.
         * It utilizes the local variable in to initialize the DataInputStream.
         * @param in The DataInputStream to be used for reading from the server.
         */
        public ReadFromServer(DataInputStream in) {
            this.in = in;
        }
        /***
         * This mutator method assigned the read data to the corresponding variables of the other player.
         * It also updates the plate states and the sequence position of the other player.
         */
        public void run() {
            try {
                while(true) {
                    otherPlayerData = in.readUTF();
                    
                    otherSequencePosition = in.readInt();
                    for (int i = 0; i < 5; i++) {
                        otherPlateStates[i] = in.readBoolean();
                    }
                    
                    if (otherPlayer != null) {
                        String[] playerData = otherPlayerData.split(",");
                        otherPlayer.setX(Double.parseDouble(playerData[1]));
                        otherPlayer.setY(Double.parseDouble(playerData[2]));
                        otherPlayer.setFacingLeft(Boolean.parseBoolean(playerData[3]));
                        otherPlayerInPortal = Boolean.parseBoolean(playerData[4]);
                        
                        if (playerData.length >= 7) {
                            boolean serverWinState = Boolean.parseBoolean(playerData[6]);
                            if (serverWinState) {
                                haveWon = true;
                                entities.clear();
                            }
                        }
                    }
                    
                    if (!plates.isEmpty()) {
                        for (int i = 0; i < plates.size(); i++) {
                            Plate plate = plates.get(i);
                            plate.setActivated(otherPlateStates[i]);
                        }
                        Plate.setCurrentSequencePosition(otherSequencePosition);
                    }
                }
            } catch (Exception e) {
                System.out.println("IOException: from ReadFromServer run()");
            }
        }
        /***
         * This mutator method waits for the start message from the server to begin the networking process.
         * After the start message is received, the threads for the reading from and writing to the server are started.
         */
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
        /***
         * This getter method is used to get the x-coordinate of the other player.
         * Utilizing the split method, the string is split into an array of strings, and the x-coordinate is parsed as a double.
         * @return The x-coordinate of the other player.
         */
        public double getOtherPlayerX() { 
            String[] playerData = otherPlayerData.split(",");
            return Double.parseDouble(playerData[1]); 
        }
        /***
         * This getter method is used to get the y-coordinate of the other player.
         * Utilizing the split method, the string is split into an array of strings, and the y-coordinate is parsed as a double.
         * @return The y-coordinate of the other player.
         */
        public double getOtherPlayerY() { 
            String[] playerData = otherPlayerData.split(",");
            return Double.parseDouble(playerData[2]); 
        }
        /***
         * This getter method is used to get the boolean value of the other player's portal state.
         * This is mainly to ensure both players are in the same portal state and synched in their game states.
         * @return The boolean value of the other player's portal state.
         */
        public boolean isOtherPlayerInPortal() {
            return otherPlayerInPortal;
        }
    }
    /***
     * This inner class focuses on the writing aspect of the networking.
     * It writes the data to the server and is passed off to the other client as the otherplayer's data.
     */
    private class WriteToServer implements Runnable {
        private DataOutputStream out;
        /***
         * This constructor initializes the DataOutputStream needed for the writing process.
         * It utilizes the local variable out to initialize the DataOutputStream.
         * @param out The DataOutputStream to be used for writing to the server.
         */
        public WriteToServer(DataOutputStream out) {
            this.out = out;
        }
        /***
         * This mutator method is used to write the data to the server.
         * It combines the platerid, x and y coordinates, facing left boolean, portal boolean, and win boolean into a string for the other player.
         * Before it can run, it ensures that the current player, portal1, and portal2 are not null.
         */
        public void run() {
            try {
                while(true) {
                    if (currentPlayer != null && portal1 != null && portal2 != null) {
                        boolean portalCollision = (playerID == 1) ? 
                            portal1.checkCollision(currentPlayer) : 
                            portal2.checkCollision(currentPlayer);
                        boolean inPortal = portalCollision && Plate.isSequenceComplete();
                        
                        String playerData = String.format("%d,%.2f,%.2f,%b,%b,0,%b",
                            playerID,
                            currentPlayer.getX(),
                            currentPlayer.getY(),
                            currentPlayer.isFacingLeft(),
                            inPortal,
                            haveWon
                        );
                        out.writeUTF(playerData);

                        out.writeInt(Plate.getCurrentSequencePosition());
                        for (Plate plate : plates) {
                            out.writeBoolean(plate.isActivated());
                        }
                        
                        out.flush();
                    }
                    Thread.sleep(16);
                }
            } catch (Exception e) {
                System.out.println("IOException: from WriteToServer run()");
            }
        }
    }
    /***
     * This override method is used to draw all the elements of the game screen used by the client.
     * It draws the map, the guide items, the sequence completion status, the portal status, and the win screen.
     * @param g The Graphics object to be used for drawing.
     */
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

        if (!Plate.isSequenceComplete()) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("Escape the pits of the Ghastly Meadows!", 70, 50);
        } else {
            g2d.setColor(Color.GREEN);
            g2d.drawString("Sequence complete! Portals are now active!", 70, 50);
        }

        boolean inPortal = (playerID == 1) ? 
            portal1.checkCollision(currentPlayer) : 
            portal2.checkCollision(currentPlayer);
            
        if (inPortal) {
            if (Plate.isSequenceComplete()) {
                g2d.drawString("Waiting for the other player to enter the portal...", 70, 730);
            } else {
                g2d.drawString("This portal doesn't seem to be working...", 70, 730);
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

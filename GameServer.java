import java.io.*;
import java.net.*;
/**
 * The GameServer class is responsible for creating the server for the game.
 * Through the server, it ensures that the clients' players can connect to each other and play the game smoothly.
 * It is also responsible for sending, reading, assigning, and updating the entities and the game loop.
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)   
    @version 23 May 2025

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
public class GameServer {
    private ServerSocket ss;
    private int numPlayers, maxPlayers;
    private boolean[] playersInPortal = new boolean[2];
    private boolean[] playersCompletedSequence = new boolean[2];
    private boolean gameWon = false;
    
    private Socket P1socket, P2socket;
    private ReadFromClient P1ReadRunnable, P2ReadRunnable;
    private WriteToClient P1WriteRunnable, P2WriteRunnable; 
    private String P1data, P2data;
    private boolean[] P1plateStates = new boolean[5];
    private boolean[] P2plateStates = new boolean[5];
    private int P1currentSequence = 0;
    private int P2currentSequence = 0;
    /***
     * This is the constructor for the GameServer class.
     * It initializes the server socket, the number of players, the maximum number of players, and dummy data for the players to ensure neither player is null when the game freshly starts.
     */
    public GameServer() {
        System.out.println("Game Server");
        numPlayers = 0;
        maxPlayers = 2;
        
        P1data = "1,100,100,false,false,0,false";
        P2data = "2,600,100,false,false,0,false";
        System.out.println("Game Server initialized at port " + Constants.GAME_SETTINGS.PORT);
    }
    /***
     * This is the mutator method for the GameServer class.
     * It updates the player's state based on the player's index, if they are in the portal, and the sequence they are on.
     * It also checks if the game has been won and updates the players' data accordingly.
     * @param playerIndex The index of the player to be updated.
     * @param inPortal The boolean value of whether the player is in the portal.
     * @param sequence The order of the player's correctly interacted plate sequence.
     */
    private synchronized void updatePlayerState(int playerIndex, boolean inPortal, int sequence) {
        playersInPortal[playerIndex - 1] = inPortal;
        playersCompletedSequence[playerIndex - 1] = (sequence == 5);
        
        if (!gameWon && playersInPortal[0] && playersInPortal[1] && 
            playersCompletedSequence[0] && playersCompletedSequence[1]) {
            
            System.out.println("Both players completed sequence and in portal! Game won!");
            gameWon = true;
            
            String[] p1Info = P1data.split(",");
            String[] p2Info = P2data.split(",");
            
            p1Info[6] = "true";
            p2Info[6] = "true";
            
            P1data = String.join(",", p1Info);
            P2data = String.join(",", p2Info);
            
            System.out.println("Updated P1data: " + P1data);
            System.out.println("Updated P2data: " + P2data);
        }
    }
    /***
     * This mutator method is what allows the server to accept connections from the clients.
     * It accepts connections from a maximum of 2 clients and assigns the players to the corresponding sockets and runnables.
     */
    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            ss = new ServerSocket(Constants.GAME_SETTINGS.PORT);

            while (numPlayers < maxPlayers) {
                Socket s = ss.accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player " + numPlayers + " connected");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                if(numPlayers == 1){
                    P1socket = s;
                    P1ReadRunnable = rfc;
                    P1WriteRunnable = wtc;
                } else {
                    P2socket = s;
                    P2ReadRunnable = rfc;
                    P2WriteRunnable = wtc;
                    P1WriteRunnable.sendStartMsg();
                    P2WriteRunnable.sendStartMsg();
                    
                    new Thread(P1ReadRunnable).start();
                    new Thread(P2ReadRunnable).start();
                    new Thread(P1WriteRunnable).start();
                    new Thread(P2WriteRunnable).start();
                }
            }
            System.out.println("No longer accepting connections");
        } catch (IOException e) {
            System.out.println("IOException from acceptConnections: " + e.getMessage());
        }
    }
    /***
     * This inner class reads the data from the clients.
     * It reads the data from the clients and updates the players' data according to their playerID.
     * 
     */
    private class ReadFromClient implements Runnable {
        private int playerID;
        private DataInputStream in;
        /**
         * This is the constructor that creates the ReadFromClient for the corresponding player.
         * It initializes the playerID and the DataInputStream
         * @param playerID The ID of the player.
         * @param in The DataInputStream to be used for reading from the client.
         */
        public ReadFromClient(int playerID, DataInputStream in) {
            this.playerID = playerID;
            this.in = in;
            System.out.println("RFC created for player #: " + playerID);
        }
        /***
         * This run method reads the string data from the client and updates the players' data according to their playerID.
         * It splits the string into an array of strings.
         */
        @Override
        public void run() {
            try {
                while(true) {
                    if(playerID == 1) {
                        P1data = in.readUTF();
                        P1currentSequence = in.readInt();
                        for (int i = 0; i < 5; i++) {
                            P1plateStates[i] = in.readBoolean();
                        }
                        
                        String[] p1Info = P1data.split(",");
                        boolean inPortal = Boolean.parseBoolean(p1Info[4]);
                        updatePlayerState(1, inPortal, P1currentSequence);
                        
                    } else {
                        P2data = in.readUTF();
                        P2currentSequence = in.readInt();
                        for (int i = 0; i < 5; i++) {
                            P2plateStates[i] = in.readBoolean();
                        }
                        
                        String[] p2Info = P2data.split(",");
                        boolean inPortal = Boolean.parseBoolean(p2Info[4]);
                        updatePlayerState(2, inPortal, P2currentSequence);
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException from ReadFromClient run(): " + e.getMessage());
            }
        }
    }
    /***
     * This inner class writes the data to the clients.
     * It assigns the corresponding "otherPlayer" data depending on the playerID.
     */
    private class WriteToClient implements Runnable {
        private int playerID;
        private DataOutputStream out;
        /**
         * This is the constructor that creates the WriteToClient for the corresponding player.
         * It initializes the playerID and the DataOutputStream.
         * @param playerID The ID of the player.
         * @param out The DataOutputStream used for writing to the client.
         */
        public WriteToClient(int playerID, DataOutputStream out) {
            this.playerID = playerID;
            this.out = out;
            System.out.println("WTC created for player #: " + playerID);
        }
        /***
         * This run method writes the data to the client.
         * It writes the otherPlayer data to the client according to their playerID.
         */
        @Override
        public void run() {
            try {
                while(true) {
                    if(playerID == 1) {
                        out.writeUTF(P2data);
                        out.writeInt(P2currentSequence);
                        for (int i = 0; i < 5; i++) {
                            out.writeBoolean(P2plateStates[i]);
                        }
                        out.flush();
                    } else {
                        out.writeUTF(P1data);
                        out.writeInt(P1currentSequence);
                        for (int i = 0; i < 5; i++) {
                            out.writeBoolean(P1plateStates[i]);
                        }
                        out.flush();
                    }
                    Thread.sleep(16);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("Exception from WriteToClient run(): " + e.getMessage());
            }
        }
        /***
         * This method sends the start message to the client.
         * This enables the clients to start their threads and begin the game.
         */
        public void sendStartMsg() {
            try {
                out.writeUTF("We now have 2 players! Start!");
                out.flush();
            } catch (IOException e) {
                System.out.println("IOException from sendStartMsg: " + e.getMessage());
            }
        }
    }
    /***
     * This is the main method that creates the GameServer object and starts the server.
     * It also calls the acceptConnections method to accept connection requests from the clients.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
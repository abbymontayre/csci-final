import java.io.*;
import java.net.*;

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

    public GameServer() {
        System.out.println("Game Server");
        numPlayers = 0;
        maxPlayers = 2;
        try {
            System.out.println("Server started on port: " + Constants.GAME_SETTINGS.PORT);
            ss = new ServerSocket(Constants.GAME_SETTINGS.PORT);
        } catch (Exception e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }

    private synchronized void updatePlayerState(int playerIndex, boolean inPortal, int sequence) {
        playersInPortal[playerIndex - 1] = inPortal;
        playersCompletedSequence[playerIndex - 1] = (sequence == 5);
        
        if (!gameWon && playersInPortal[0] && playersInPortal[1] && 
            playersCompletedSequence[0] && playersCompletedSequence[1]) {
            gameWon = true;
            
            // Update both players' data with win state
            String[] p1Info = P1data.split(",");
            String[] p2Info = P2data.split(",");
            
            // Set win state (adding/updating 7th position in data string)
            p1Info[6] = "true";
            p2Info[6] = "true";
            
            P1data = String.join(",", p1Info);
            P2data = String.join(",", p2Info);
            
            System.out.println("Updated P1data: " + P1data);
            System.out.println("Updated P2data: " + P2data);
        }
    }

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

    private class ReadFromClient implements Runnable {
        private int playerID;
        private DataInputStream in;

        public ReadFromClient(int playerID, DataInputStream in) {
            this.playerID = playerID;
            this.in = in;
            System.out.println("RFC created for player #: " + playerID);
        }

        
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
                        
                        // Update portal and sequence state for player 1
                        String[] p1Info = P1data.split(",");
                        boolean inPortal = Boolean.parseBoolean(p1Info[4]);
                        updatePlayerState(1, inPortal, P1currentSequence);
                        
                    } else {
                        P2data = in.readUTF();
                        P2currentSequence = in.readInt();
                        for (int i = 0; i < 5; i++) {
                            P2plateStates[i] = in.readBoolean();
                        }
                        
                        // Update portal and sequence state for player 2
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

    private class WriteToClient implements Runnable {
        private int playerID;
        private DataOutputStream out;

        public WriteToClient(int playerID, DataOutputStream out) {
            this.playerID = playerID;
            this.out = out;
            System.out.println("WTC created for player #: " + playerID);
        }
        
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

        public void sendStartMsg() {
            try {
                out.writeUTF("We now have 2 players! Start!");
                out.flush();
            } catch (IOException e) {
                System.out.println("IOException from sendStartMsg: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
import java.io.*;
import java.net.*;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers, maxPlayers;
    private int currentLevel = 0;
    private long lastLevelChangeTime = 0;
    private static final long LEVEL_CHANGE_COOLDOWN = 2000; // 2 seconds cooldown
    private boolean[] playersInPortal = new boolean[2];
    private boolean[] playersCompletedSequence = new boolean[2];
    
    private Socket P1socket, P2socket;
    private ReadFromClient P1ReadRunnable, P2ReadRunnable;
    private WriteToClient P1WriteRunnable, P2WriteRunnable; 
    private String P1data, P2data;
    private String P1portalData, P2portalData;
    private boolean[] P1plateStates = new boolean[3];
    private boolean[] P2plateStates = new boolean[3];
    private int P1currentSequence = 0;
    private int P2currentSequence = 0;

    public GameServer() {
        System.out.println("Game Server");
        numPlayers = 0;
        maxPlayers = 2;
        
        P1data = "1,100,100,false,false,0";
        P2data = "2,600,100,false,false,0";
        
        P1portalData = "768,69|123,630";
        P2portalData = "123,630|768,69";
    }

    private synchronized boolean canChangeLevels() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastLevelChangeTime < LEVEL_CHANGE_COOLDOWN) {
            return false;
        }
        
        // Both players must be in a portal AND have completed their sequences
        return playersInPortal[0] && playersInPortal[1] && 
               playersCompletedSequence[0] && playersCompletedSequence[1];
    }

    private synchronized void updatePlayerState(int playerIndex, boolean inPortal, int sequence) {
        playersInPortal[playerIndex - 1] = inPortal;
        playersCompletedSequence[playerIndex - 1] = (sequence == 3); // Assuming 3 is the complete sequence
        
        // Check if we can change levels
        if (canChangeLevels()) {
            currentLevel = (currentLevel == 0) ? 1 : 0;
            lastLevelChangeTime = System.currentTimeMillis();
            System.out.println("Both players completed sequence and in portal - Level changed to: " + currentLevel);
            
            // Update both players' data with new level
            String[] p1Info = P1data.split(",");
            String[] p2Info = P2data.split(",");
            
            p1Info[5] = String.valueOf(currentLevel);
            p2Info[5] = String.valueOf(currentLevel);
            
            P1data = String.join(",", p1Info);
            P2data = String.join(",", p2Info);
            
            // Reset states
            playersInPortal[0] = false;
            playersInPortal[1] = false;
            playersCompletedSequence[0] = false;
            playersCompletedSequence[1] = false;
            P1currentSequence = 0;
            P2currentSequence = 0;
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
        private long lastPrintTime = 0;
        private static final long PRINT_DELAY = 500;

        public ReadFromClient(int playerID, DataInputStream in) {
            this.playerID = playerID;
            this.in = in;
            System.out.println("RFC created for player #: " + playerID);
        }

        private void debugPrint(String... messages) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPrintTime >= PRINT_DELAY) {
                System.out.println("\n--- Player " + playerID + " Update ---");
                for (String message : messages) {
                    System.out.println(message);
                }
                System.out.println("----------------------");
                lastPrintTime = currentTime;
            }
        }
        
        @Override
        public void run() {
            try {
                while(true) {
                    if(playerID == 1) {
                        P1data = in.readUTF();
                        P1portalData = in.readUTF();
                        P1currentSequence = in.readInt();
                        for (int i = 0; i < 3; i++) {
                            P1plateStates[i] = in.readBoolean();
                        }
                        
                        // Update portal and sequence state for player 1
                        String[] p1Info = P1data.split(",");
                        boolean inPortal = Boolean.parseBoolean(p1Info[4]);
                        updatePlayerState(1, inPortal, P1currentSequence);
                        
                        debugPrint(
                            "Player 1 Data: " + P1data,
                            "Player 1 Portal Data: " + P1portalData,
                            "Player 1 Sequence: " + P1currentSequence,
                            "In Portal: " + inPortal,
                            "Sequence Complete: " + playersCompletedSequence[0]
                        );
                    } else {
                        P2data = in.readUTF();
                        P2portalData = in.readUTF();
                        P2currentSequence = in.readInt();
                        for (int i = 0; i < 3; i++) {
                            P2plateStates[i] = in.readBoolean();
                        }
                        
                        // Update portal and sequence state for player 2
                        String[] p2Info = P2data.split(",");
                        boolean inPortal = Boolean.parseBoolean(p2Info[4]);
                        updatePlayerState(2, inPortal, P2currentSequence);
                        
                        debugPrint(
                            "Player 2 Data: " + P2data,
                            "Player 2 Portal Data: " + P2portalData,
                            "Player 2 Sequence: " + P2currentSequence,
                            "In Portal: " + inPortal,
                            "Sequence Complete: " + playersCompletedSequence[1]
                        );
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
                        out.writeUTF(P2portalData);
                        out.writeInt(P2currentSequence);
                        for (int i = 0; i < 3; i++) {
                            out.writeBoolean(P2plateStates[i]);
                        }
                        out.flush();
                    } else {
                        out.writeUTF(P1data);
                        out.writeUTF(P1portalData);
                        out.writeInt(P1currentSequence);
                        for (int i = 0; i < 3; i++) {
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
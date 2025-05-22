import java.io.*;
import java.net.*;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers, maxPlayers;
    private int currentLevel = 0;
    private long lastLevelChangeTime = 0;
    private static final long LEVEL_CHANGE_COOLDOWN = 2000; 
    private boolean[] playersInPortal = new boolean[2];
    
    private Socket P1socket, P2socket;
    
    private ReadFromClient P1ReadRunnable, P2ReadRunnable;
    private WriteToClient P1WriteRunnable, P2WriteRunnable; 
    private boolean[] plateStates = new boolean[3];
    private int currentSequence = 0;
    private int p1MapID, p2MapID;
    private double p1x, p1y, p2x, p2y;
    private boolean p1facingLeft, p2facingLeft, p1isInsidePortal, p2isInsidePortal;

    public GameServer() {
        System.out.println("Game Server");
        numPlayers = 0;
        maxPlayers = 2;

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
                int checkPlayerID = in.readInt();
                if(checkPlayerID == 1) {
                    p1x = in.readDouble();
                    p1y = in.readDouble();
                    p1facingLeft = in.readBoolean();
                    p1isInsidePortal = in.readBoolean();
                    p1MapID = in.readInt(); // read player info sent by client
                    currentSequence = in.readInt(); //reads the state of the sequence of plates
                    for (int i = 0; i < 3; i++) {
                        plateStates[i] = in.readBoolean(); // reads each individual plate state
                    }
                } else {
                    p2x = in.readDouble();
                    p2y = in.readDouble();
                    p2facingLeft = in.readBoolean();
                    p2isInsidePortal = in.readBoolean();
                    p2MapID = in.readInt();
                    currentSequence = in.readInt();
                    for (int i = 0; i < 3; i++) {
                        plateStates[i] = in.readBoolean();
                    }
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
                    // Send Player 2's data to Player 1
                    out.writeDouble(p2x);
                    out.writeDouble(p2y);
                    out.writeBoolean(p2facingLeft);
                    out.writeBoolean(p2isInsidePortal);
                    out.writeInt(p2MapID);
                    out.writeInt(currentSequence);
                    for (int i = 0; i < 3; i++) {
                        out.writeBoolean(plateStates[i]);
                    }
                    out.flush();
                } else {
                    // Send Player 1's data to Player 2
                    out.writeDouble(p1x);
                    out.writeDouble(p1y);
                    out.writeBoolean(p1facingLeft);
                    out.writeBoolean(p1isInsidePortal);
                    out.writeInt(p1MapID);
                    out.writeInt(currentSequence);
                    for (int i = 0; i < 3; i++) {
                        out.writeBoolean(plateStates[i]);
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
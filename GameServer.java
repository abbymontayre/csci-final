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
    private String P1data, P2data;
    private boolean[] plateStates = new boolean[3];
    private int currentSequence = 0;

    public GameServer() {
        System.out.println("Game Server");
        numPlayers = 0;
        maxPlayers = 2;
        
        P1data = "1,100,100,false,false,0";
        P2data = "2,600,100,false,false,0";
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
                        P1data = in.readUTF(); // read player info sent by client
                        currentSequence = in.readInt(); //reads the state of the sequence of plates
                        for (int i = 0; i < 3; i++) {
                            plateStates[i] = in.readBoolean(); // reads each individual plate state
                        }
                    } else {
                        P2data = in.readUTF();
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
                        out.writeUTF(P2data);
                        out.writeInt(currentSequence);
                        for (int i = 0; i < 3; i++) {
                            out.writeBoolean(plateStates[i]);
                        }
                        out.flush();
                        System.out.println("P1data: " + P1data);
                    } else {
                        out.writeUTF(P1data);
                        out.writeInt(currentSequence);
                        for (int i = 0; i < 3; i++) {
                            out.writeBoolean(plateStates[i]);
                        }
                        out.flush();
                        System.out.println("P2data: " + P2data);

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
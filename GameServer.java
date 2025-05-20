import java.io.*;
import java.net.*;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers, maxPlayers;

    private Socket P1socket, P2socket;
    private ReadFromClient P1ReadRunnable, P2ReadRunnable;
    private WriteToClient P1WriteRunnable, P2WriteRunnable; 
    private double P1x, P1y, P2x, P2y;

    public GameServer() {
        System.out.println("Game Server");
        numPlayers = 0;
        maxPlayers = 2;
        P1x = 100;
        P1y = 100;
        P2x = 600;
        P2y = 100;

        try {
            ss = new ServerSocket(8700);
            System.out.println("Server is running on port 8700");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");

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
                } else if(numPlayers == 2){
                    P2socket = s;
                    P2ReadRunnable = rfc;
                    P2WriteRunnable = wtc;
                    P1WriteRunnable.sendStartMsg();
                    P2WriteRunnable.sendStartMsg();
                    Thread readThread = new Thread(P1ReadRunnable);
                    Thread readThread2 = new Thread(P2ReadRunnable);

                    readThread.start();
                    readThread2.start();

                    Thread writeThread = new Thread(P1WriteRunnable);
                    Thread writeThread2 = new Thread(P2WriteRunnable);

                    writeThread.start();
                    writeThread2.start();
                }

            }
            System.out.println("No longer accepting connections");
        } catch (Exception e) {
            System.out.println("IOException: from acceptConnections");
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
                        P1x = in.readDouble();
                        P1y = in.readDouble();
                    } else {
                        P2x = in.readDouble();
                        P2y = in.readDouble();
                    }
                }
            } catch (Exception e) {
                System.out.println("IOException: from ReadFromClient run()");
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
                        out.writeDouble(P2x);
                        out.writeDouble(P2y);
                        out.flush();
                    } else {
                        out.writeDouble(P1x);
                        out.writeDouble(P1y);
                        out.flush();
                    }
                    try {
                        Thread.sleep(16);
                    } catch (Exception e) {
                        System.out.println("Interrupted: from WriteToClient run()");
                    }
                }
            } catch (Exception e) {
                System.out.println("IOException: from WriteToClient run()");
            }
        }
        public void sendStartMsg() {
            try {
                out.writeUTF("We now have 2 players! Start!");
                out.flush();
            } catch (IOException e) {
                System.out.println("IOException: from sendStartMsg");
            }
    }
}
    public static void main(String[] args) {
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
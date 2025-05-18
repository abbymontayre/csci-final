// PlayerNetwork.java
import java.io.*;
import java.net.*;
import java.awt.*;

public class PlayerNetwork extends Entity implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public PlayerNetwork(Socket socket, int playerId) {
        super(playerId, 0, 0, 0, 0, 0); // Server doesn't need visual properties
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] coords = inputLine.split(",");
                if (coords.length == 2) {
                    setX(Integer.parseInt(coords[0]));
                    setY(Integer.parseInt(coords[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameState(String gameState) {
        out.println(gameState);
    }

    @Override
    public void render(Graphics g) {
        // Empty implementation - server doesn't render
    }
}
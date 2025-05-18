import java.io.*;
import java.net.*;
import java.util.*;

public class GameServer {
    private static List<PlayerNetwork> players = new ArrayList<>();
    private static ServerSocket serverSocket;

  public static void main(String[] args) {
    try {
        serverSocket = new ServerSocket(Constants.GAME_SETTINGS.PORT);
        System.out.println("Game Server started on port " + Constants.GAME_SETTINGS.PORT);

        // Accept two player connections
        for (int i = 0; i < 2; i++) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Player " + (i+1) + " connected");
            
            PlayerNetwork player = new PlayerNetwork(clientSocket, i+1);
            players.add(player);
            
            // Send immediate update with all player positions
            String currentState = getGameState();
            player.sendGameState(currentState);
            
            new Thread(player).start();
        }
        
        // Start game loop
        gameLoop();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static void gameLoop() {
    while (true) {
        // Broadcast game state to all players
        String gameState = getGameState();
        for (PlayerNetwork player : players) {
            player.sendGameState(gameState);
        }

        try {
            Thread.sleep(8); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

    private static String getGameState() {
    StringBuilder sb = new StringBuilder();
    
    // Players data
    for (PlayerNetwork player : players) {
        sb.append(player.getId()).append(",")
          .append(player.getX()).append(",")
          .append(player.getY()).append("|");
    }
    
    // Remove the last "|" if it exists
    if (sb.length() > 0) {
        sb.setLength(sb.length() - 1);
    }
    
    return sb.toString();
}
}
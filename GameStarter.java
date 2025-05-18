import java.util.Scanner;

public class GameStarter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("LAN Square Game - Player Setup \n -----------------------------");
        
        // Get server IP
        System.out.print("Enter Server IP [localhost]: ");
        String serverIP = scanner.nextLine().trim();
        if (serverIP.isEmpty()) {
            serverIP = "localhost";
        }
        
        // Get player number
        int playerId;
        do {
            System.out.print("Enter Player Number (1 or 2): ");
            try {
                playerId = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter only 1 or 2");
                playerId = 0;
            }
        } while (playerId != 1 && playerId != 2);
        
        System.setProperty("server.ip", serverIP);
        new GameFrame(playerId);
    }
}
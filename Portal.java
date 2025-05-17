import java.util.*;

public class Portal extends MyObj {
    private List<Player> players = new ArrayList<>();
    private Portal destination;

    public Portal(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void setDestination(Portal destination) {
        this.destination = destination;
    }

    public void checkTeleport(Map map) {
        if (destination == null) return;

        for (Player p : players) {
            if (this.collisionTester(p)) {
                int newX = destination.getX();
                int newY = destination.getY();

                p.setX(newX);
                p.setY(newY);

                if (!map.inTheMap(p)) {
                    System.out.println("Teleport location out of bounds. Reverting.");
                    p.setX(150);
                    p.setY(150);
                } else {
                    System.out.println("Teleported to: " + newX + ", " + newY);
                }
            }
        }
    }
}

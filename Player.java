public class Player extends MyObj {

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean playerCollision(Player other) {
        return collisionTester(other);
    }

    public void move(int dx, int dy, Map map, Player other) {
        int oldX = x;
        int oldY = y;

        x += dx;
        y += dy;

        if (!map.inTheMap(this)) {
            System.out.println("Move blocked by map bounds.");
            x = oldX;
            y = oldY;
        } else if (this.playerCollision(other)) {
            System.out.println("Move blocked due to collision with another player.");
            x = oldX;
            y = oldY;
        } else {
            System.out.println("Moved to: " + x + ", " + y);
        }
    }
}

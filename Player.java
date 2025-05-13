class Player extends MyObj{

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void move(int dx, int dy, Map map) {
        int OldX = x;
        int OldY = y;

        x += dx;
        y += dy;

        if (!map.inTheMap(this)) {
            System.out.println("Move blocked by map bounds.");
            x = OldX;
            y = OldY;
        } else {
            System.out.println("Moved to: " + x + ", " + y);
        }
    }


}
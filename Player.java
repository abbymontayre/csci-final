class Player extends MyObj{

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void move(int dx, int dy, Map map) {
        int OldX = x;
        int OldY = y;

        x += dx;
        y += dy;

        if (!map.inTheMap(this)){
            // If the player is out of bounds, revert to old position
            x = OldX;
            y = OldY;
        }

    }

}
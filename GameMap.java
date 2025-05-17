import java.awt.*;
class GameMap extends MyObj{

    public GameMap(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void draw(Graphics g, Image tileset) {
        int tileSize = 32; // adjust if needed

        for (int i = 0; i < width; i += tileSize) {
            for (int j = 0; j < height; j += tileSize) {
                g.drawImage(tileset,
                    x + i, y + j, x + i + tileSize, y + j + tileSize,  // destination
                    0, 0, tileSize, tileSize,                          // source tile (top-left)
                    null
                );
            }
        }
    }
    
}
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private List<GameMap> maps;
    private Image tileset;

    public Map() {
        maps = new ArrayList<>();
        loadTileset();
        initializeMaps();
    }

    private void loadTileset() {
        try {
            tileset = javax.imageio.ImageIO.read(new java.io.File("assets/tiles.png"));
            System.out.println("Tileset loaded: " + tileset.getWidth(null) + "x" + tileset.getHeight(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initializeMaps() {
        maps.add(new GameMap(64, 64, 256, 288));
        maps.add(new GameMap(384, 64, 256, 288));
        maps.add(new GameMap(704, 64, 256, 288));
        maps.add(new GameMap(64, 384, 256, 288));
        maps.add(new GameMap(384, 384, 256, 288));
        maps.add(new GameMap(704, 384, 256, 288));
        maps.add(new GameMap(320, 160, 64, 96));
        maps.add(new GameMap(640, 160, 64, 96));
        maps.add(new GameMap(320, 480, 64, 96));
        maps.add(new GameMap(640, 480, 64, 96));
    }

    public void draw(Graphics g) {
        for (GameMap map : maps) {
            map.draw(g, tileset);
        }
    }

    public boolean insideCombinedArea(MyObj obj, GameMap map1, GameMap map2) {
        boolean horizontallyAdjacent = map1.getA() == map2.getX() || map2.getA() == map1.getX();
        boolean verticallyAdjacent = map1.getB() == map2.getY() || map2.getB() == map1.getY();

        if (horizontallyAdjacent) {
            int minY = Math.max(map1.getY(), map2.getY());
            int maxY = Math.min(map1.getB(), map2.getB());

            return obj.getB() >= minY && obj.getB() <= maxY &&
                   obj.getY() <= maxY && obj.getY() >= minY &&
                   obj.getA() >= Math.min(map1.getX(), map2.getX()) &&
                   obj.getA() <= Math.max(map1.getA(), map2.getA()) &&
                   obj.getX() <= Math.max(map1.getA(), map2.getA()) &&
                   obj.getX() >= Math.min(map1.getX(), map2.getX());
        }

        if (verticallyAdjacent) {
            int minX = Math.max(map1.getX(), map2.getX());
            int maxX = Math.min(map1.getA(), map2.getA());

            return obj.getA() >= minX && obj.getA() <= maxX &&
                   obj.getX() <= maxX && obj.getX() >= minX &&
                   obj.getB() >= Math.min(map1.getY(), map2.getY()) &&
                   obj.getB() <= Math.max(map1.getB(), map2.getB()) &&
                   obj.getY() <= Math.max(map1.getB(), map2.getB()) &&
                   obj.getY() >= Math.min(map1.getY(), map2.getY());
        }

        return false;
    }

    public boolean insideSingleMap(MyObj obj, GameMap map) {
        return obj.getX() >= map.getX() &&
               obj.getX() + obj.getWidth() <= map.getX() + map.getWidth() &&
               obj.getY() >= map.getY() &&
               obj.getY() + obj.getHeight() <= map.getY() + map.getHeight();
    }

    public boolean inTheMap(MyObj obj) {
        for (GameMap map : maps) {
            if (insideSingleMap(obj, map)) {
                System.out.println("Object is inside a single map!");
                return true;
            }
        }

        for (int i = 0; i < maps.size(); i++) {
            for (int j = i + 1; j < maps.size(); j++) {
                GameMap map1 = maps.get(i);
                GameMap map2 = maps.get(j);

                if (insideCombinedArea(obj, map1, map2)) {
                    System.out.println("Object is inside combined area of two maps!");
                    return true;
                }
            }
        }
        return false;
    }
}
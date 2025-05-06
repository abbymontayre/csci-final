import java.util.*;

public class Map {
    private List<GameMap> maps;

    public Map() {
        maps = new ArrayList<>();
    }

    public void addMap(GameMap map) {
        maps.add(map);
    }

    public boolean insideCombinedArea(MyObj obj, GameMap map1, GameMap map2) {
        // Check if maps are horizontally adjacent or side by side
        boolean horizontallyAdjacent = map1.getA() == map2.getX() ||
                                        map2.getA() == map1.getX();

        // Check if maps are vertically adjacent or top and bottom
        boolean verticallyAdjacent = map1.getB() == map2.getY() ||
                                    map2.getB() == map1.getY();

        if (horizontallyAdjacent) {
            int minY = Math.max(map1.getY(), map2.getY());
            int maxY = Math.min(map1.getB(), map2.getB());

            return obj.getB() >= minY && obj.getB() <= maxY &&
                obj.getY() <= maxY && obj.getY() >= minY &&
                obj.getA() >= Math.min(map1.getX(), map2.getX()) &&
                obj.getA() <= Math.max(map1.getA(), map2.getA()) &&
                obj.getX() <= Math.max(map1.getA(), map2.getA()) &&
                obj.getX() >= Math.min(map1.getX(), map2.getX())
                ;
        }

        if (verticallyAdjacent) {
            int minX = Math.max(map1.getX(), map2.getX());
            int maxX = Math.min(map1.getA(), map2.getA());

            return obj.getA() >= minX && obj.getA() <= maxX &&
                obj.getX() <= maxX && obj.getX() >= minX &&
                obj.getB() >= Math.min(map1.getY(), map2.getY()) &&
                obj.getB() <= Math.max(map1.getB(), map2.getB()) &&
                obj.getY() <= Math.max(map1.getB(), map2.getB()) &&
                obj.getY() >= Math.min(map1.getY(), map2.getY())
                ;
        }

        return false; // Not adjacent
    }

    public boolean insideSingleMap(MyObj obj, GameMap map) {
        return  obj.getX() >= map.getX() &&
                obj.getX() + obj.getWidth() <= map.getX() + map.getWidth() &&
                obj.getY() >= map.getY() &&
                obj.getY() + obj.getHeight() <= map.getY() + map.getHeight();
    }

    public boolean inTheMap(MyObj obj){
        for (GameMap map : maps) {
            if (insideSingleMap(obj, map)){
                System.out.println("Player inside the map!");
                return true;
            }
        }

        for (int i = 0; i < maps.size(); i++){
            for (int j = i+1; j<maps.size(); j++){
                //maps.get() is used to get the map at index i and j
                //and then we check if the object is inside the combined area of the two maps
                GameMap map1 = maps.get(i);
                GameMap map2 = maps.get(j);
                
                if (insideCombinedArea(obj, map1, map2)){
                    System.out.println("Object is inside the combined area of two maps!");
                    return true;
                }
            }
        }
        return false;
    }
}
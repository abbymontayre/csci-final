import java.util.ArrayList;

public class LevelManager {
    private ArrayList<Map> levels;
    private int currentLevel;
    private static final int TOTAL_LEVELS = 3;

    public LevelManager() {
        levels = new ArrayList<>();
        // You can customize each map for each level if needed
        for (int i = 0; i < TOTAL_LEVELS; i++) {
            levels.add(new Map());
        }
        currentLevel = 0;
    }

    public Map getCurrentMap() {
        return levels.get(currentLevel);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean nextLevel() {
        if (currentLevel < TOTAL_LEVELS - 1) {
            currentLevel++;
            return true;
        }
        return false; // No more levels
    }

    public boolean isLastLevel() {
        return currentLevel == TOTAL_LEVELS - 1;
    }

    public void reset() {
        currentLevel = 0;
    }
} 
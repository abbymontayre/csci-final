/**
 * This class contains all the constants used in the game.
 * It is divided into two inner classes namely, GAME_SETTINGS and ENTITY_SETTINGS.
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)   
    @version 22 May 2025

    We have not discussed the Java language code in our program
    with anyone other than our instructor or the teaching assistants
    assigned to this course.
    
    We have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.

    If any Java language code or documentation used in our program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of our program.
 */
public class Constants {

    /**
     * This inner class contains all the constants used in the game. 
     * The constants are for the port and the JFrame settings. 
     */
    public static class GAME_SETTINGS {
        
        public static final int PORT = 8700;
        public static final int SCREEN_WIDTH = 1024;
        public static final int SCREEN_HEIGHT = 768;
        public static final int TILE_SIZE = 64;
        public static final String FONT = "SansSerif";      

    }

    /** 
     * This inner class contains the constants used for the game entities.
     * This is done so that the constants can be easily changed if needed.
     */
    public static class ENTITY_SETTINGS {
        public static final int MOVE_SPEED = 8;
        
    }
}

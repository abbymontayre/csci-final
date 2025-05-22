/**
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)   
    @version 22 May 2025

    I have not discussed the Java language code in my program
    with anyone other than my instructor or the teaching assistants
    assigned to this course.
    
    I have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.

    If any Java language code or documentation used in my program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of my program.
 */

public class Constants {

    /**
     * This class contains all the constants used in the game. 
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
     * This class contains the constants used for the game entities.
     * The move speed is used to determine how fast the entities can move.
     */

    public static class ENTITY_SETTINGS {
        public static final int MOVE_SPEED = 8;
        
    }
}

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
public class GameStarter {

    /**
     * The main method is the entry point of the game.
     * It creates an instance of GameFrame and sets up the GUI.
     */

    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.setUpGUI();
    }
}
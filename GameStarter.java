/**
    * The GameStarter class is what initializes and starts the game for the client.
    * It creates an instance of GameFrame and sets up the GUI.
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
public class GameStarter {

    /**
     * This main method what allows the clients to start the game.
     * It creates an instance of GameFrame and sets up the GUI.
     * If the client is player 1, it will wait for a second client to connect before the GUI launches.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.setUpGUI();
    }
}
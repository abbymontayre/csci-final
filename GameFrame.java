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

import javax.swing.*;
import java.awt.*;

public class GameFrame {
    private GameCanvas gameCanvas;
    private JFrame frame;
    private String serverIP;

    /**
     * Constructor for the GameFrame class.
     * Initializes the JFrame and prompts the user for the server IP address.
     * 
     * @param serverIP The IP address of the server to connect to.
     * @param gameCanvas The GameCanvas object to be added to the JFrame.
     */

    public GameFrame() {
        frame = new JFrame();
        // Show IP input dialog before creating game canvas
        serverIP = askForIPAddress();
        if (serverIP != null) {  // Only create canvas if IP was provided
            gameCanvas = new GameCanvas(serverIP);
        }
    }

    /**
     * The askForIPAddress method prompts the user for the server IP address.
     * It uses a JOptionPane to display a dialog with a text field.
     * If the user enters an empty string, it shows an error message and
     * repeats the prompt.
     * If the user cancels, the program exits.
     */
    
    private String askForIPAddress() {
        JTextField ipField = new JTextField("localhost", 15);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Server IP:"));
        panel.add(ipField);
        
        int result = JOptionPane.showConfirmDialog(null, panel, 
            "Connect to Server", JOptionPane.OK_CANCEL_OPTION);
            
        if (result == JOptionPane.OK_OPTION) {
            String ip = ipField.getText().trim();
            if (ip.isEmpty()) {
                JOptionPane.showMessageDialog(null, "IP address cannot be empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return askForIPAddress();  // Show dialog again
            }
            return ip;
        } else {
            System.exit(0);  // Exit if user cancels
            return null;
        }
    }

    /**
     * The setUpGUI method sets up the GUI for the game.
     * It initializes the JFrame settings, adds the GameCanvas,
     * and makes the frame visible.
     */

    public void setUpGUI() {
        if (gameCanvas == null) {
            System.out.println("Error: GameCanvas not initialized. Connection failed.");
            return;
        }
        frame.setTitle("Who Goes Where?");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameCanvas);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }
}

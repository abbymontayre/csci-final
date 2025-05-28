/**
 * The GameFrame class sets up the GUI for each client.
 * It prompts the user for the server IP address to ensure that the clients can connect to the correct server and port.
    @author Raphaelle Abby U. Montayre (243114) and Angela Kyra U. Salarda (246444)
    @version 23 May 2025

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
import javax.swing.*;

public class GameFrame {
    private GameCanvas gameCanvas;
    private JFrame frame;
    private String serverIP;

    /**
     * This is the constructor for the GameFrame class.
     * It initializes the JFrame and prompts the user for the server IP address to ensure that the clients can connect to the correct server and port.
     * @param serverIP The IP address of the server to connect to.
     * @param gameCanvas The GameCanvas object to be added to the JFrame.
     */
    public GameFrame() {
        frame = new JFrame();
        serverIP = askForIPAddress();
        if (serverIP != null) { 
            gameCanvas = new GameCanvas(serverIP);
        }
    }

    /**
     * The askForIPAddress getter method prompts the user for the server IP address.
     * It uses a JOptionPane to display a dialog with a text field.
     * If the user enters an empty string, it shows an error message and repeats the prompt.
     * If the user cancels, the program exits.
     * @return The server IP address entered by the user.
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
                return askForIPAddress(); 
            }
            return ip;
        } else {
            System.exit(0); 
            return null;
        }
    }

    /**
     * The setUpGUI method sets up the GUI for the game.
     * It initializes the JFrame settings, adds the GameCanvas, and makes the frame visible.
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

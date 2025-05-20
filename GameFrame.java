import javax.swing.*;
<<<<<<< HEAD
import java.awt.*;
=======

>>>>>>> f085cc853d2564ab7a6872b20a2beaf341a61f7a

public class GameFrame {
    private GameCanvas gameCanvas;
    private JFrame frame;
    private String serverIP;

    public GameFrame() {
        frame = new JFrame();
        // Show IP input dialog before creating game canvas
        serverIP = askForIPAddress();
        if (serverIP != null) {  // Only create canvas if IP was provided
            gameCanvas = new GameCanvas(serverIP);
        }
    }
    
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

    public void setUpGUI() {
        if (gameCanvas == null) {
            System.out.println("Error: GameCanvas not initialized. Connection failed.");
            return;
        }
        frame.setTitle("Who Goes There?");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gameCanvas);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }
}

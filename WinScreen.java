import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class WinScreen extends JComponent {
    
    private String title, subtitle, content, endLine;
    private boolean visible = false;
    private Rectangle bounds;

    private Color bgColor = Color.BLACK;
    private Color textColor = Color.WHITE;

    private Font titleFont = new Font("SansSerif", Font.BOLD, 24);
    private Font otherFont = new Font("SansSerif", Font.BOLD, 16);
    private Font contentFont = new Font("SansSerif", Font.PLAIN, 16);


    private int padding = 40; // Reduced for better spacing

    
    private int screenWidth;
    private int screenHeight;
    private int x, y;

    


    public WinScreen() {
        
        this.screenWidth = Constants.GAME_SETTINGS.SCREEN_WIDTH;
        this.screenHeight = Constants.GAME_SETTINGS.SCREEN_HEIGHT;

        this.title = "Victory: The Path Has Cleared";
        this.subtitle = "You have made it out.";
        this.content = "Through cryptic riddles, shifting statues, and whispered clues, you and your partner have escaped the depths of the Ghastly Meadows. Your patience, timing, and teamwork brought light to the silence and meaning to the madness.";
        this.endLine = "But was this the end of the cave... or just the beginning?";
        
        this.bounds = new Rectangle(x, y, screenWidth, screenHeight);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (visible) {
                    // Close the game or perform any other action
                    System.exit(0);
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    public boolean isVisible() {
        return visible;
    }

    
    public void render(Graphics g) {
        if (!visible) return;

        Graphics2D g2d = (Graphics2D) g;

        // Draw background 
        g2d.setColor(bgColor);
        g2d.fillRect(x, y, screenWidth, screenHeight);

        int yPos = y + padding;

        // Draw title
        g2d.setColor(textColor);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = x + (screenWidth - fm.stringWidth(title)) / 2;
        yPos += fm.getAscent();
        g2d.drawString(title, titleX, yPos);

        // Add extra space after the title
        yPos += padding; // Increase this value for more space

        // Draw subtitle
        g2d.setFont(otherFont);
        fm = g2d.getFontMetrics();
        yPos += fm.getHeight() + padding / 2;
        int subtitleX = x + (screenWidth - fm.stringWidth(subtitle)) / 2;
        g2d.drawString(subtitle, subtitleX, yPos);

        // Draw content (word wrap, center-aligned)
        g2d.setFont(contentFont);
        fm = g2d.getFontMetrics();
        yPos += fm.getHeight() + padding / 2;
        int contentWidth = screenWidth - 2 * padding;
        String[] words = content.split(" ");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            String testLine = currentLine + (currentLine.length() > 0 ? " " : "") + word;
            int testWidth = fm.stringWidth(testLine);
            if (testWidth > contentWidth) {
                // Center align the current line
                int lineX = x + (screenWidth - fm.stringWidth(currentLine.toString())) / 2;
                g2d.drawString(currentLine.toString(), lineX, yPos);
                yPos += fm.getHeight();
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            }
        }
        if (currentLine.length() > 0) {
            int lineX = x + (screenWidth - fm.stringWidth(currentLine.toString())) / 2;
            g2d.drawString(currentLine.toString(), lineX, yPos);
            yPos += fm.getHeight();
        }

        // Draw endLine
        g2d.setFont(otherFont);
        fm = g2d.getFontMetrics();
        yPos += padding / 2;
        int endLineX = x + (screenWidth - fm.stringWidth(endLine)) / 2;
        g2d.drawString(endLine, endLineX, yPos);

        // Draw closeHint at the bottom center
        String closeHint = "Press [Esc] to quit the game.";
        g2d.setFont(contentFont.deriveFont(Font.ITALIC));
        fm = g2d.getFontMetrics();
        // Bottom right: x = right edge - text width - padding
        int hintX = x + screenWidth - fm.stringWidth(closeHint) - padding;
        int hintY = y + screenHeight - padding;
        g2d.drawString(closeHint, hintX, hintY);
    }
}
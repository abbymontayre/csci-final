import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class WinScreen extends JComponent {
    
    private String title, subtitle, content, endLine;
    private boolean visible = false;
    private Rectangle bounds;
    private Image endSceneImage;

    private Color bgColor = Color.BLACK;
    private Color textColor = Color.WHITE;

    private Font titleFont = new Font(Constants.GAME_SETTINGS.FONT, Font.BOLD, 24);
    private Font otherFont = new Font(Constants.GAME_SETTINGS.FONT, Font.BOLD, 16);
    private Font contentFont = new Font(Constants.GAME_SETTINGS.FONT, Font.PLAIN, 16);

    private int padding = 40; // Reduced for better spacing
    private int imageHeight = 300; // Height for the end scene image
    
    private int screenWidth;
    private int screenHeight;
    private int x, y;

    public WinScreen() {
        this.screenWidth = Constants.GAME_SETTINGS.SCREEN_WIDTH;
        this.screenHeight = Constants.GAME_SETTINGS.SCREEN_HEIGHT;

        this.title = "Victory: The Path Has Cleared";
        this.subtitle = "You have made it out.";
        this.content = "Through cryptic riddles, mysterious portals, and whispered clues, you and your partner have escaped the depths of the Ghastly Meadows. Your patience, timing, and teamwork brought light to the silence and meaning to the madness.";
        this.endLine = "But was this the end of the cave... or just the beginning?";
        
        this.bounds = new Rectangle(x, y, screenWidth, screenHeight);

        // Load the end scene image
        try {
            endSceneImage = ImageIO.read(new File("end-scene.png"));
        } catch (IOException e) {
            System.out.println("Could not load end-scene.png: " + e.getMessage());
            endSceneImage = null;
        }

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
    
    public void draw(Graphics g) {
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
        yPos += padding;

        // Draw subtitle
        g2d.setFont(otherFont);
        fm = g2d.getFontMetrics();
        yPos += fm.getHeight() + padding / 2;
        int subtitleX = x + (screenWidth - fm.stringWidth(subtitle)) / 2;
        g2d.drawString(subtitle, subtitleX, yPos);

        // Draw the end scene image
        
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
        
        if (endSceneImage != null) {
            yPos += padding;
            // Calculate image width while maintaining aspect ratio
            double aspectRatio = (double) endSceneImage.getWidth(null) / endSceneImage.getHeight(null);
            int imageWidth = (int) (imageHeight * aspectRatio);
            int imageX = x + (screenWidth - imageWidth) / 2;
            g2d.drawImage(endSceneImage, imageX, yPos, imageWidth, imageHeight, null);
            yPos += imageHeight + padding;
        }
        // Draw closeHint at the bottom center
        String closeHint = "Press [Esc] to quit the game.";
        g2d.setFont(contentFont.deriveFont(Font.ITALIC));
        fm = g2d.getFontMetrics();
        int hintX = x + screenWidth - fm.stringWidth(closeHint) - padding;
        int hintY = y + screenHeight - padding;
        g2d.drawString(closeHint, hintX, hintY);
    }
}
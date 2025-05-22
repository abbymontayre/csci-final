import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
/**
    * This class is the WinScreen class displayed at the end of the game.
    * It displays a victory message and an image of the player.
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

    private int padding = 40;
    private int imageHeight = 300; 
    
    private int screenWidth;
    private int screenHeight;
    private int x, y;

    /**
     * This is the constructor for the WinScreen class.
     * It initializes the victory message, subtitle, content, and endLine.
     */
    public WinScreen() {
        this.screenWidth = Constants.GAME_SETTINGS.SCREEN_WIDTH;
        this.screenHeight = Constants.GAME_SETTINGS.SCREEN_HEIGHT;

        this.title = "Victory: The Path Has Cleared";
        this.subtitle = "You have made it out.";
        this.content = "Through cryptic riddles, mysterious portals, and whispered clues, you and your partner have escaped the depths of the Ghastly Meadows. Your patience, timing, and teamwork brought light to the silence and meaning to the madness.";
        this.endLine = "But was this the end of the cave... or just the beginning?";
        
        this.bounds = new Rectangle(x, y, screenWidth, screenHeight);

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
                    System.exit(0);
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * The setVisible mutator method sets the visibility of the WinScreen.
     * This method is called to show or hide the WinScreen.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * The isVisible getter method checks if the WinScreen is currently visible.
     * This method is used to determine if the WinScreen should be drawn on the screen.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * The draw method draws the WinScreen on the given Graphics object.
     * It uses the bounds, background color, text color, and fonts to draw the WinScreen.
     *
     * @param g The Graphics object to draw on.
     */
    public void draw(Graphics g) {
        if (!visible) return;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(bgColor);
        g2d.fillRect(x, y, screenWidth, screenHeight);

        int yPos = y + padding;

        g2d.setColor(textColor);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = x + (screenWidth - fm.stringWidth(title)) / 2;
        yPos += fm.getAscent();
        g2d.drawString(title, titleX, yPos);


        yPos += padding;


        g2d.setFont(otherFont);
        fm = g2d.getFontMetrics();
        yPos += fm.getHeight() + padding / 2;
        int subtitleX = x + (screenWidth - fm.stringWidth(subtitle)) / 2;
        g2d.drawString(subtitle, subtitleX, yPos);

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
        

        g2d.setFont(otherFont);
        fm = g2d.getFontMetrics();
        yPos += padding / 2;
        int endLineX = x + (screenWidth - fm.stringWidth(endLine)) / 2;
        g2d.drawString(endLine, endLineX, yPos);
        
        if (endSceneImage != null) {
            yPos += padding;

            double aspectRatio = (double) endSceneImage.getWidth(null) / endSceneImage.getHeight(null);
            int imageWidth = (int) (imageHeight * aspectRatio);
            int imageX = x + (screenWidth - imageWidth) / 2;
            g2d.drawImage(endSceneImage, imageX, yPos, imageWidth, imageHeight, null);
            yPos += imageHeight + padding;
        }

        String closeHint = "Press [Esc] to quit the game.";
        g2d.setFont(contentFont.deriveFont(Font.ITALIC));
        fm = g2d.getFontMetrics();
        int hintX = x + screenWidth - fm.stringWidth(closeHint) - padding;
        int hintY = y + screenHeight - padding;
        g2d.drawString(closeHint, hintX, hintY);
    }
}
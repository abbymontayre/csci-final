import java.awt.*;
/**
    * This class is used to create a popup window that displays a title and content.
    * This is mainly used to display the guide items to the player.
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

public class Popup {
    private String title, content;
    private boolean visible = false;
    private Rectangle bounds;
    private Color bgColor = new Color(0, 0, 0, 220);
    private Color textColor = Color.WHITE;
    private Font titleFont = new Font("SansSerif", Font.BOLD, 24);
    private Font contentFont = new Font("SansSerif", Font.PLAIN, 16);
    private int padding = 20;

    /**
     * This is the constructor for the Popup class.
     * It initializes the popup with a given title, content, and screen dimensions and ensures that the popup is centered on the screen.
     * @param title       The title of the popup.
     * @param content     The content of the popup.
     * @param screenWidth The width of the screen.
     * @param screenHeight The height of the screen.
     */
    public Popup(String title, String content, int screenWidth, int screenHeight) {
        this.title = title;
        this.content = content;
        
        int width = 400;
        int height = 300;
        int x = (screenWidth - width) / 2;
        int y = (screenHeight - height) / 2;
        this.bounds = new Rectangle(x, y, width, height);
    }

    /**
     * The show() method makes the popup visible.
     * This method is called to display the popup to the user when needed.
     */
    public void show() {
        visible = true;
    }

    /**
     * The hide() method makes the popup invisible.
     * This method is called to close the popup.
     */
    public void hide() {
        visible = false;
    }

    /**
     * The isVisible() method checks if the popup is currently visible.
     * This method is used to determine if the popup should be drawn on the screen.
     * @return true if the popup is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * The draw() method draws the popup on to the screen.
     * It uses the bounds, background color, text color, fonts to render the popup, and makes sure that the content is wrapped properly.
     * 
     * @param g The Graphics object to draw on.
     */
    public void draw(Graphics g) {
        if (!visible) return;

        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setColor(bgColor);
        g2d.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);
        
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);

        g2d.setColor(textColor);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = bounds.x + (bounds.width - fm.stringWidth(title)) / 2;
        int titleY = bounds.y + padding + fm.getAscent();
        g2d.drawString(title, titleX, titleY);

        g2d.setFont(contentFont);
        fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();
        int yPos = titleY + lineHeight + padding;
        
        String[] words = content.split(" ");
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            String testLine = currentLine + (currentLine.length() > 0 ? " " : "") + word;
            int testWidth = fm.stringWidth(testLine);
            
            if (testWidth > bounds.width - 2*padding) {

                g2d.drawString(currentLine.toString(), 
                    bounds.x + padding, 
                    yPos);
                yPos += lineHeight;
                currentLine = new StringBuilder(word);
            } else {
                currentLine.append(currentLine.length() > 0 ? " " : "").append(word);
            }
        }
        
        if (currentLine.length() > 0) {
            g2d.drawString(currentLine.toString(), 
                bounds.x + padding, 
                yPos);
        }

        String closeHint = "Press SPACE to close";
        g2d.setFont(contentFont.deriveFont(Font.ITALIC));
        int hintX = bounds.x + bounds.width - padding - fm.stringWidth(closeHint);
        int hintY = bounds.y + bounds.height - padding;
        g2d.drawString(closeHint, hintX, hintY);
    }
}
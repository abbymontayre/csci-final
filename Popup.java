import java.awt.*;

public class Popup {
    private String title, content;
    private boolean visible = false;
    private Rectangle bounds;
    private Color bgColor = new Color(0, 0, 0, 220); // Semi-transparent black
    private Color textColor = Color.WHITE;
    private Font titleFont = new Font("SansSerif", Font.BOLD, 24);
    private Font contentFont = new Font("SansSerif", Font.PLAIN, 16);
    private int padding = 20;

    public Popup(String title, String content, int screenWidth, int screenHeight) {
        this.title = title;
        this.content = content;
        
        // Calculate popup bounds (centered)
        int width = 400;
        int height = 300;
        int x = (screenWidth - width) / 2;
        int y = (screenHeight - height) / 2;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render(Graphics g) {
        if (!visible) return;

        Graphics2D g2d = (Graphics2D) g;
        
        // Draw background with shadow
        g2d.setColor(bgColor);
        g2d.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);
        
        // Draw border
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 20, 20);

        // Draw title
        g2d.setColor(textColor);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = bounds.x + (bounds.width - fm.stringWidth(title)) / 2;
        int titleY = bounds.y + padding + fm.getAscent();
        g2d.drawString(title, titleX, titleY);

        // Draw content (with word wrap)
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
                // Draw current line
                g2d.drawString(currentLine.toString(), 
                    bounds.x + padding, 
                    yPos);
                yPos += lineHeight;
                currentLine = new StringBuilder(word);
            } else {
                currentLine.append(currentLine.length() > 0 ? " " : "").append(word);
            }
        }
        
        // Draw remaining text
        if (currentLine.length() > 0) {
            g2d.drawString(currentLine.toString(), 
                bounds.x + padding, 
                yPos);
        }

        // Draw close hint
        String closeHint = "Press SPACE to close";
        g2d.setFont(contentFont.deriveFont(Font.ITALIC));
        int hintX = bounds.x + bounds.width - padding - fm.stringWidth(closeHint);
        int hintY = bounds.y + bounds.height - padding;
        g2d.drawString(closeHint, hintX, hintY);
    }
}
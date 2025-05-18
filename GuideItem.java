import java.awt.*;

public class GuideItem extends Entity {
    private Popup popup;

    public GuideItem(int id, int x, int y, String title, String content, int screenWidth, int screenHeight) {
        super(id, x, y, 30, 50, 0);
        this.popup = new Popup(title, content, screenWidth, screenHeight);
    }

    public void showDialog() {
        popup.show();
    }

    public void hideDialog() {
        popup.hide();
    }

    public boolean isDialogVisible() {
        return popup.isVisible();
    }

    public void renderPopup(Graphics g) {
        popup.render(g);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(139, 69, 19)); // Brown book color
        g.fillRect(x, y, width, height);
    }
}
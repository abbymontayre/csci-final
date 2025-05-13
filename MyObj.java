import java.awt.*;

abstract class MyObj {
    protected int x, y, width, height;

    public MyObj(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.fillRect(x, y, width, height);
    } 

    public boolean collisionTester(MyObj other) {
        return (x < other.x + other.width && x + width > other.x && y < other.y + other.height && y + height > other.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getA(){
        return x+width;
    }
    public int getB(){
        return y+height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    /**
     * (x,y) is the top left corner of the object
     * (a,b) is the bottom right corner of the object
     */

}
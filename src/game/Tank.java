package game;

/**
 * @Author Frank
 * @Date 2021/11/20 17:05
 * @Version 1.0
 */
@SuppressWarnings({"all"})
public class Tank {
    private int x;
    private int y;
    private int direct;
    private int speed;
    boolean isLive = true;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void moveup() {
        y -= speed;
    }

    public void movedown() {
        y += speed;
    }

    public void moveleft() {
        x -= speed;
    }

    public void moveright() { x += speed; }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

package game;

/**
 * @Author Frank
 * @Date 2021/11/20 17:05
 * @Version 1.0
 */
import java.util.Vector;

@SuppressWarnings({"all"})
public class MyTank extends Tank {
    Shot shot = null;
    Vector<Shot> shots = new Vector<>();
    //boolean isLive = true;

    public MyTank(int x, int y) {
        super(x, y);
    }

    public void makeShot() {
        switch (getDirect()) {
            case 0:
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            case 1:
                shot = new Shot(getX() + 60, getY() + 20, 1);
                break;
            case 2:
                shot = new Shot(getX(), getY() + 20, 2);
                break;
            case 3:
                shot = new Shot(getX() + 20, getY() + 60, 3);
                break;
        }

        shots.add(shot);

        new Thread(shot).start();
    }
}

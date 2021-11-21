package game;

/**
 * @Author Frank
 * @Date 2021/11/20 17:05
 * @Version 1.0
 */
@SuppressWarnings({"all"})
public class Shot implements Runnable{
    int x;
    int y;
    int direct;
    int speed = 2;
    boolean isLive = true;

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (direct){
                case 0 ://up
                    y -= speed;
                    break;
                case 1 ://right
                    x += speed;
                    break;
                case 2 ://left
                    x -= speed;
                    break;
                case 3 ://down
                    y += speed;
                    break;
            }

            if (x < 0 || x >1000 || y < 0 || y > 750 || !isLive){
                isLive = false;
                break;
            }
        }
    }
}

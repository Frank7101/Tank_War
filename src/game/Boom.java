package game;

/**
 * @Author Frank
 * @Date 2021/11/20 17:05
 * @Version 1.0
 */
@SuppressWarnings({"all"})
public class Boom {
    int x, y;
    int life = 10;//生命周期
    boolean isLive = true;

    public Boom(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeMinus(){
        if (life > 0){
            life--;
        }else{
            isLive = false;
        }
    }
}

package game;

/**
 * @Author Frank
 * @Date 2021/11/20 17:05
 * @Version 1.0
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

@SuppressWarnings({"all"})
public class MyPanel extends JPanel implements KeyListener, Runnable {
    //存放我的坦克
    MyTank Frank = null;
    //存放敌人坦克
    Vector<EnemyTank> enemyTanks = new Vector<>();
    int enemytanksize = 8;

    //存放node,用于恢复EnemyTank坐标
    Vector<Node> nodes = new Vector<>();
    //存放炸弹
    Vector<Boom> Booms = new Vector<>();

    //定义三张图片显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    public MyPanel(String key) {
        //初始化自己坦克
        Frank = new MyTank(500, 500);
        Frank.setSpeed(5);

        //判断recordFile是否存在，不存在开新游戏
        File file = new File(Recorder.getRecordFile());
        if (file.exists()) {
            //数据恢复
            nodes = Recorder.recover();
        } else {
            System.out.println("没有记录，重开！");
            key = "new";
        }

        //将enemyTanks设置给Recorder enemyTanks
        Recorder.setEnemyTanks(enemyTanks);

        switch (key) {
            case "new":
                //初始化敌人坦克
                for (int i = 0; i < enemytanksize; i++) {
                    EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
                    //将enemyTanks设置给setEnemyTanks
                    enemyTank.setEnemyTanks(enemyTanks);

                    enemyTank.setDirect(3);
                    enemyTank.setSpeed(5);
                    new Thread(enemyTank).start();

                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();

                    enemyTanks.add(enemyTank);
                }
                break;
            case "continue":
                //初始化敌人坦克
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);

                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    //将enemyTanks设置给setEnemyTanks
                    enemyTank.setEnemyTanks(enemyTanks);

                    enemyTank.setDirect(node.getDirect());
                    enemyTank.setSpeed(5);
                    new Thread(enemyTank).start();

                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();

                    enemyTanks.add(enemyTank);
                }
                break;
        }


        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/2.jpg"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/3.jpg"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/4.jpg"));
    }

    public void showInfo(Graphics g) {
        //玩家总成绩
        g.setColor(Color.black);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);

        g.drawString("累计击毁敌方坦克", 1020, 30);
        g.drawString(Recorder.getScore() + "", 1080, 100);
        drawtank(1020, 60, g, 0, 0);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);
        showInfo(g);

        //画出自己坦克
        if (Frank != null && Frank.isLive) {
            drawtank(Frank.getX(), Frank.getY(), g, Frank.getDirect(), 0);
        }

        //画子弹!!!先判空！！！
        for (int i = 0; i < Frank.shots.size(); i++) {
            Shot shot = Frank.shots.get(i);
            if (shot != null && shot.isLive) {
                g.draw3DRect(shot.x, shot.y, 1, 1, false);
            } else {
                Frank.shots.remove(shot);
            }
        }

        //根据生命值显示对应图片
        for (int i = 0; i < Booms.size(); i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boom boom = Booms.get(i);

            if (boom.life > 6) {
                g.drawImage(image1, boom.x, boom.y, 60, 60, this);
            } else if (boom.life > 3) {
                g.drawImage(image2, boom.x, boom.y, 60, 60, this);
            } else {
                g.drawImage(image3, boom.x, boom.y, 60, 60, this);
            }

            //减少生命值
            boom.lifeMinus();

            if (boom.life <= 0) {
                Booms.remove(boom);
            }
        }

        //画出敌人坦克
        for (EnemyTank enemyTank : enemyTanks) {
            if (enemyTank.isLive) {//判断敌人坦克是否存活
                drawtank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);

                //画出子弹
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    //取出子弹
                    Shot shot = enemyTank.shots.get(j);
                    //画子弹
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }
    }

    //画坦克（通用）
    public void drawtank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0:
                g.setColor(Color.cyan);
                break;

            case 1:
                g.setColor(Color.yellow);
                break;
        }

        switch (direct) {
            case 0://上
                g.fill3DRect(x, y, 10, 60, false);//左轮
                g.fill3DRect(x + 30, y, 10, 60, false);//右轮
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//底
                g.fillOval(x + 10, y + 20, 20, 20);//顶
                g.drawLine(x + 20, y + 30, x + 20, y);//管
                break;

            case 1://右
                g.fill3DRect(x, y, 60, 10, false);//左轮
                g.fill3DRect(x, y + 30, 60, 10, false);//右轮
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//底
                g.fillOval(x + 20, y + 10, 20, 20);//顶
                g.drawLine(x + 30, y + 20, x + 60, y + 20);//管
                break;

            case 2://左
                g.fill3DRect(x, y, 60, 10, false);//左轮
                g.fill3DRect(x, y + 30, 60, 10, false);//右轮
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//底
                g.fillOval(x + 20, y + 10, 20, 20);//顶
                g.drawLine(x + 30, y + 20, x, y + 20);//管
                break;

            case 3://下
                g.fill3DRect(x, y, 10, 60, false);//左轮
                g.fill3DRect(x + 30, y, 10, 60, false);//右轮
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//底
                g.fillOval(x + 10, y + 20, 20, 20);//顶
                g.drawLine(x + 20, y + 30, x + 20, y + 60);//管
                break;

            default:
                System.out.println("null");
        }
    }

    //判断子弹是否打中敌方坦克
    public void hitEnemy() {
        //for (Shot shot:Frank.shots) {//引起ConcurrentModificationException
        for (int i = 0; i < Frank.shots.size(); i++) {//迭代器不会报异常
            Shot shot = Frank.shots.get(i);
            if (shot != null && shot.isLive) {
                //for (EnemyTank enemyTank : enemyTanks) {
                for (int j = 0; j < enemyTanks.size(); j++) {
                    EnemyTank enemyTank = enemyTanks.get(j);
                    hitTank(shot, enemyTank);
                }
            }
        }
    }

    //判断子弹是否打中我方坦克
    public void hitMy() {
        //for (EnemyTank tank : enemyTanks) {
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            //for (Shot s : tank.shots) {
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot s = enemyTank.shots.get(j);
                if (Frank.isLive && s.isLive) {
                    hitTank(s, Frank);
                }
            }
        }
    }

    //判断敌人坦克是否被击中
    public void hitTank(Shot s, Tank t) {
        switch (t.getDirect()) {
            case 0:
            case 3:
                if (s.x > t.getX() && s.x < t.getX() + 40 && s.y > t.getY() && s.y < t.getY() + 60) {
                    s.isLive = false;
                    t.isLive = false;

                    enemyTanks.remove(t);

                    if (t instanceof EnemyTank) {
                        Recorder.add();
                    }

                    Boom boom = new Boom(t.getX(), t.getY());
                    Booms.add(boom);
                }
                break;
            case 1:
            case 2:
                if (s.x > t.getX() && s.x < t.getX() + 60 && s.y > t.getY() && s.y < t.getY() + 40) {
                    s.isLive = false;
                    t.isLive = false;

                    enemyTanks.remove(t);

                    if (t instanceof EnemyTank) {
                        Recorder.add();
                    }

                    Boom boom = new Boom(t.getX(), t.getY());
                    Booms.add(boom);
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //键盘控制行为
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            Frank.setDirect(0);
            if (Frank.getY() > 0) {
                Frank.moveup();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            Frank.setDirect(3);
            if (Frank.getY() + 60 < 750) {
                Frank.movedown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            Frank.setDirect(2);
            if (Frank.getX() > 0) {
                Frank.moveleft();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            Frank.setDirect(1);
            if (Frank.getX() + 60 < 1000) {
                Frank.moveright();
            }
        }
        //按J发射子弹
        if (e.getKeyCode() == KeyEvent.VK_J) {
            Frank.makeShot();
        }
        //重绘
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hitEnemy();
            hitMy();
            this.repaint();
        }
    }
}

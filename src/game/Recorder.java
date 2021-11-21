package game;

/**
 * @Author Frank
 * @Date 2021/11/20 17:05
 * @Version 1.0
 */
import java.io.*;
import java.util.Vector;

@SuppressWarnings({"all"})
public class Recorder {
    private static int score = 0;
    private static BufferedWriter bw = null;
    private static BufferedReader br = null;
    private static String recordFile = "src\\myRecord.txt";
    //指向MyPanel中的EnemyTanks Vector，用以记录坐标
    private static Vector<EnemyTank> enemyTanks = null;
    //保存EnemyTank信息
    private static Vector<Node> nodes = new Vector<>();

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    //回复EnemyTanks信息
    public static Vector<Node> recover() {
        try {
            br = new BufferedReader(new FileReader(recordFile));
            score = Integer.parseInt(br.readLine());

            //读入record信息，加入nodes
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] xyd = line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]), Integer.parseInt(xyd[1]), Integer.parseInt(xyd[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }

    public static void save() {
        try {
            bw = new BufferedWriter(new FileWriter(recordFile));
            bw.write(score + "\r\n");

            //记录敌人坦克坐标
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                if (enemyTank.isLive) {//保险起见判断存活
                    String record = enemyTank.getX() + " " + enemyTank.getY() + " " + enemyTank.getDirect();
                    bw.write(record + "\r\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getRecordFile() {
        return recordFile;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Recorder.score = score;
    }

    //增加消灭坦克数量
    public static void add() {
        Recorder.score++;
    }
}

package cn.sxt.game;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * @author Michael
 * Plane game main Frame
 * @create 2020-05-26 14:14
 **/
public class MyGameFrame extends JFrame {

    Image planeImg = GameUtil.getImage("images/plane.png");
    Image background = GameUtil.getImage("images/background.jpg");

    Plane plane = new Plane(planeImg,250,250);
    Shell[] shells = new Shell[100];

    Explode bao;
    Date startTime = new Date();
    Date endTime;
    int period;//游戏持续时间

    @Override
    public void paint(Graphics g){  //自动被调用，g相当于一支画笔
        Color c = g.getColor();
        g.drawImage(background, 0, 0,null);
        plane.drawSelf(g); //画飞机

        //画all炮弹
        for(int i=0; i<shells.length;i++){
            shells[i].draw(g);
            
            //飞机和炮弹碰撞的检测
            boolean peng = shells[i].getRect().intersects(plane.getRect());
            if(peng){
                plane.live=false;
                if(bao==null) {
                    bao = new Explode(plane.x, plane.y);
                    endTime = new Date();
                    period = (int)((endTime.getTime()-startTime.getTime())/1000);
                }
                bao.draw(g);
            }
            //计时功能，给出提示
            if(!plane.live){
                g.setColor(Color.YELLOW);
                Font f = new Font("宋体",Font.BOLD,50);
                g.setFont(f);
                g.drawString("时间："+period+"秒",(int)plane.x,(int)plane.y);

            }

        }
        g.setColor(c);

    }

    //帮助我们反复的重画窗口
    class PaintThread extends Thread{

        @Override
        public void run() {
            while (true){
                repaint();   //重画
                System.out.println("窗口画一次");

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //定义键盘监听的内部类
    class KeyMonitor extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            plane.addDirection(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            plane.minusDirection(e);
        }
    }

    /**
     * 初始化窗口
     */
    public void launchFrame(){
        this.setTitle("Michael's work!");
        this.setVisible(true);
        this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
        this.setLocation(300,300);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        new PaintThread().start();  //启动重画窗口的线程
        addKeyListener(new KeyMonitor());//给窗口添加键盘的监听

        //初始化50枚炮弹
        for(int i=0;i<shells.length;i++){
            shells[i] = new Shell();
        }

    }

    public static void main(String[] args) {
        MyGameFrame f = new MyGameFrame();
        f.launchFrame();
    }


    private Image offScreenImage = null;//创建缓冲区
    public void update(Graphics g) {
        if(offScreenImage == null) {
            offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
        }
        Graphics gOff = offScreenImage.getGraphics();//创建离线图片的实例，在图片缓冲区绘图

        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);//将缓冲图片绘制到窗口目标
    }



}

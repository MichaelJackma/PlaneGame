package cn.sxt.game;

import java.awt.*;

/**
 * @author Michael
 * @create 2020-06-11 16:46
 **/
public class Explode {
    double x,y;
    static Image[] imgs = new Image[4];
    static {
        for(int i=0;i<4;i++){
            imgs[i]=GameUtil.getImage("images/explode/e"+(i+1)+".png");
            imgs[i].getWidth(null);
        }
    }

    int count;
    public void draw(Graphics g){
        if(count<=3){
            g.drawImage(imgs[count],(int)x,(int)y,null);
            count++;
        }
    }

    public Explode(double x,double y){
        this.x=x;
        this.y=y;
    }

}

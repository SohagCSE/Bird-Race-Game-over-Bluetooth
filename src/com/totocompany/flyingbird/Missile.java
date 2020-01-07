package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by SOHAG on 1/27/2017.
 */
public class Missile extends GameObject {
    private int score;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public Missile(Bitmap res, int x, int y, int w, int h, int s, int numFrames)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = GamePanel.plspeed/3 + (int)(rand.nextDouble()*score/30);
        
        if(speed<8)speed=8;
        if(speed>=GamePanel.plspeed)speed = GamePanel.plspeed;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i=0;i<image.length;i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet,0,i*height,width,height);
        }

        animation.setFrames(image);
        animation.setDelay(100 - speed);
    }
    public void update()
    {
        x -= speed;
        animation.update();
    }
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch (Exception e){}
    }
}

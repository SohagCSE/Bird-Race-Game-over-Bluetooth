package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by SOHAG on 1/31/2017.
 */
public class Food extends GameObject{
    private int score;
    private int speed;
    private Random rand = new Random();
    private Bitmap image;

    public Food(Bitmap res, int x, int y, int w, int h, int scor)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = scor;
        image = res;

        speed = GamePanel.plspeed/3 + (int)(rand.nextDouble()*score/30);
        
        if(speed<8)speed=8;
        if(speed>=GamePanel.plspeed)speed = GamePanel.plspeed;

    }
    public void update()
    {
        x -= speed;
    }
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(image,x,y,null);
        }catch (Exception e){}
    }
}

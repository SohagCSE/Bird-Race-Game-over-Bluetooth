package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by SOHAG on 1/27/2017.
 */
public class Dragon extends GameObject {
    private int score,row=0;
    private int speed;
    private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public Dragon(Bitmap res, int x, int y, int w, int h, int s, int numFrames)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        score = s;

        speed = 7 + (int)(rand.nextDouble()*score/30);

        if(speed>=15)speed = 15;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;
        
		row=0;
		for(int i=0;i<image.length;i++)
		{
			if(i%3==0 && i>0)
			row++;
			image[i] = Bitmap.createBitmap(spritesheet,(i%3)*width,row*height,width,height);
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

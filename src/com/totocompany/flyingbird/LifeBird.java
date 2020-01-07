package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by sohag on 3/5/2017.
 */
public class LifeBird extends GameObject{
    private Bitmap spritesheet;
    private int row;
    private Animation animation = new Animation();
    private int d,j;
    private  int rty;

    public LifeBird(Bitmap res,int x,int y,int w,int h,int numFrame,int ry)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        rty = ry;

        Bitmap [] image = new Bitmap[numFrame];
        spritesheet = res;

        for(int i = 0; i< image.length; i++)
        {
            if(i%5 == 0 && i>0)row++;
            if(i>=15)
            {
                d = i - 15 + 1;
                j = 15 + 5 - d;
            }
            else if(i>=10)
            {
                d = i - 10 + 1;
                j = 10 + 5 - d;
            }
            else if(i>=5)
            {
                d = i - 5 + 1;
                j = 5 + 5 - d;
            }
            else if(i>=0)
            {
                d = i - 0 + 1;
                j = 0 + 5 - d;
            }

            image[i] = Bitmap.createBitmap(spritesheet , (j-(5*row))*width , row*height , width , height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
    }
    public void update()
    {
        animation.update();
        x += 10;
        y -= rty+1;
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
}
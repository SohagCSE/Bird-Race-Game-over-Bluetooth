package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Created by SOHAG on 1/27/2017.
 */
public class StandAnimate extends GameObject {
    private int speed;
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private int row=0;
		


    public StandAnimate(Bitmap res, int x, int y, int w, int h, int numFrames)
    {
        super.x = x;
        super.y = y;
        width = w;
        height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        //---------for coin-----------
        if(numFrames==10){
	        for(int i=0;i<image.length;i++)
	        {
	            image[i] = Bitmap.createBitmap(spritesheet,i*width,0,width,height);
	        }
        }
        //---------for fire------------
        else if (numFrames==25) {
			row=0;
			for(int i=0;i<image.length;i++)
			{
				if(i%5==0 && i>0)
				row++;
				image[i] = Bitmap.createBitmap(spritesheet,(i%5)*width,row*height,width,height);
			}
		}
        
        animation.setFrames(image);
        animation.setDelay(100 - speed);
    }
    public void update()
    {
        //x -= speed;
        animation.update();
    }
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch (Exception e){}
    }
}
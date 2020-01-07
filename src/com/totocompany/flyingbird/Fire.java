package com.totocompany.flyingbird;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Fire extends GameObject {
   private int speed=3,row=0;
   private Random rand = new Random();
   private Animation animation = new Animation();
   private Bitmap spritesheet;


   public Fire(Bitmap res, int x, int y, int w, int h, int numFrames)
   {
       super.x = x;
       super.y = y;
       width = w;
       height = h;

       Bitmap[] image = new Bitmap[numFrames];

       spritesheet = res;

       row=0;
       for(int i=0;i<image.length;i++)
       {
       	if(i%3==0 && i>0)row++;
       	image[i] = Bitmap.createBitmap(spritesheet,(i%3)*width,row*height,width,height);
       }

       animation.setFrames(image);
       animation.setDelay(100 - speed);
   }
   public void update()
   {
       x += speed;
       animation.update();
   }
   public void draw(Canvas canvas)
   {
       try{
           canvas.drawBitmap(animation.getImage(),x,y,null);
       }catch (Exception e){}
   }
   public Rect getFireRect()
   {
       return new Rect(x,y,x+width-20,y+height);
   }
}

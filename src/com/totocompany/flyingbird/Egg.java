package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Egg extends GameObject
{
    private Bitmap image;
    public Egg(Bitmap res, int x, int y)
    {
        super.x = x;
        super.y = y;
      //  width = w;
      //  height = h;
        image = res;
    }
    public void update()
    {
        x -= 3;
        y += 4;
    }
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(image,x,y,null);
        }catch (Exception e){}
    }
}

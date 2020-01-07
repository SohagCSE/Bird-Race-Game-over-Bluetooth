package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class LifeLove extends GameObject
{
    private Bitmap image;
    public LifeLove(Bitmap res, int x, int y)
    {
        super.x = x;
        super.y = y;
        image = res;
    }
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(image,x,y,null);
        }catch (Exception e){}
    }
}

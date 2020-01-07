package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class LevelTree extends GameObject{
    private Bitmap image;

    public LevelTree(Bitmap res, int x, int y)
    {
        super.x = x;
        super.y = y;
        image = res;
    }
    public void update()
    {
        x += GamePanel.MOVESPEED;
    }
    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(image,x,y,null);
        }catch (Exception e){}
    }
}

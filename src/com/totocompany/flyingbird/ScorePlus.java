package com.totocompany.flyingbird;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by SOHAG on 1/31/2017.
 */
public class ScorePlus extends GameObject{
    private String str;
    Paint paintbrush;

    public ScorePlus(String res, int x, int y)
    {
        this.x = x;
        this.y = y;
        str =res;
    }
    public void update()
    {
        y -= 5;
    }
    public void draw(Canvas canvas)
    {
    	paintbrush =new Paint();
    	paintbrush.setColor(Color.MAGENTA);
    	paintbrush.setTextSize(15);
    	paintbrush.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        try{
            canvas.drawText(str, x, y, paintbrush);
        }catch (Exception e){}
    }
}
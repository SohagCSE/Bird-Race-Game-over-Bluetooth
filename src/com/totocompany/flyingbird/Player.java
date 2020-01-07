package com.totocompany.flyingbird;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Player extends GameObject{
    private Bitmap spritesheet;
    private int score;
    private int row;
    private boolean up;
    private boolean playing,gohome=false;
    private Animation animation = new Animation();
    private long startTime;

    private int d,j;


    public Player(Bitmap res,int w,int h,int numFrame)
    {
        x = 100;//ei 'px' ebong 'py'  helicopter er starting position
        y = GamePanel.HEIGHT/3 ;
        width = w;
        height = h;

        dy = 0;
        score = 0;


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

            //image[] array te spritesheet theke bivinno image soto soto kore kete neya hosse
            image[i] = Bitmap.createBitmap(spritesheet , (j-(5*row))*width , row*height , width , height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
        startTime = System.nanoTime(); //current time ke "nanosecond" a prokash kore

    }
    public void setUp(boolean b)
    {
        up = b;
    }
    public void update()
    {
    	if(gohome)
    	{
    		x+=5;
    		if(y<50)y+=1;
    		else if(y<GamePanel.HEIGHT/3)y-=2;
    		else if(y<GamePanel.HEIGHT/2)y-=3;
    		else y-=4;
    	}
    	
    	
        long elapsed = (System.nanoTime() - startTime)/1000000;
        if(elapsed>100)
        {
            score++;
            startTime = System.nanoTime();
        }
        animation.update();

        
        //-------------if go home then not will up or down by touch-----------
        if(gohome==false)
        {
	        if(up)
	        {
	            dy  -= 1;// "dy = -1" dia dekhte hobe
	        }
	        else
	        {
	            dy += 1;
	        }
	
	        if(dy>10)dy=10;
	        if(dy<-10)dy=-10;
	
	        y += dy*(3+GamePanel.plspeed/8);//dy er sathe joto boro number multiply kora hobe up_down speed toto barbe
	        //nicher 2 ta line amar add kora
	        // if(y<-45)playing = false;//top a lege gele break korbe
	        if(y<-45)y=-45;
	        //if(y>250)playing = false;//down a lege gele break korbe
	        if(y>GamePanel.HEIGHT-180)y=GamePanel.HEIGHT-180;
	        dy = 0;
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore()
    {
        return score;
    }

    public boolean getPlaying()
    {
        return playing;
    }
    public void setPlaying(boolean b)
    {
        playing = b;
    }
    public void setGoHome(boolean b)
    {
    	gohome=b;
    }
    public void resetDY()
    {
        dy = 0;
    }
    public void resetScore()
    {
        score = 0;
    }
    public Rect getPlayerRect()
    {
        //-------eikhane " x+20, y+52 " deyar dara oi instant time a " x,y " er value onujai amra rectangle ta komay nilam------
        //-------jate sudhu pakhir gaye lagle collision detect kore, pakhay lagle jeno detect na kore-------------
        return new Rect(x+20+10,y+52+10,x+width-30-10,y+height-52-15);
        //----------------------------------------------------
    }
}

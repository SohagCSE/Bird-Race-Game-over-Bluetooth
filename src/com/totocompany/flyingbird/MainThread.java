package com.totocompany.flyingbird;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by SOHAG on 1/26/2017.
 */
public class MainThread extends Thread
{
    private int FPS = 35;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder,GamePanel gamePanel)
    {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel; //parameter er gamePanel variable theke Mainthread er gamePanel a assign
    }
    @Override
    public void run()
    {
        long startTime;
        long timeMIllis;
        long waitTime;
        long totalTime = 0;
        int frameCoount = 0;
        long targetTime = 1000/FPS;

        while (running)
        {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
                    //-------------------------------------------------------------------------------------------
                    //somvoboto ei 2 line thread er main part
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                    //nicher line ta ami add korsi
                    //this.gamePanel.drawText(canvas);
                    //--------------------------------------------------------------------------------------------
                }
            }catch (Exception e){}
            finally {
                if(canvas!=null)
                {
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }


            timeMIllis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMIllis;

            try{
                this.sleep(waitTime);
            }catch (Exception e){}

            totalTime += System.nanoTime() - startTime;
            frameCoount++;
            if(frameCoount == FPS)
            {
                averageFPS = 1000/((totalTime/frameCoount)/1000000);
                frameCoount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
    public void setRunning(boolean b)
    {
        running = b;
    }

}

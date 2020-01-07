package com.totocompany.flyingbird;

//player.getplaying()==false hole just update bondho hobe but thread cholbe

import android.R.color;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
//import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.MediaStore.Video;
import android.text.StaticLayout;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.security.PublicKey;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Random;

import javax.security.auth.PrivateCredentialPermission;

/**
 * Created by SOHAG on 1/26/2017.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 500;
    public static  int plspeed=14;
    
    private long coincnt=0,life=3;
    private long foodlpsd=1500,missilelpsd=2000,distance=0,level=1;
    private boolean gameover = false,running=false,helping=false;
    private boolean showinfo=false,showlevel=false,plgohome=false,levelcomplete=false;
    private long missilenum=1;
    
    private int rgbred=100,rgbinc=15;
    
    private float scaleFactorX;
    private float scaleFactorY;
    public static final int MOVESPEED = -5;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Missile> missiles;
    private ArrayList<Dragon> dragons;
    private Random rand = new Random();
    private Random rand2 = new Random();
    
    
    
    private Paint paintbrush_btnbg;
    private Paint paintbrush_playbg;
    private Paint paintbrush_text;
    private Paint paintbrush_stroke;

    //-----------------egg and life---
    private long eggStartTime;
    private ArrayList<Egg> egg;
    private ArrayList<LifeBird> lifebird;
    private ArrayList<LifeLove>lifeLoves;
    
    private ArrayList<LevelTree>levelTrees;
    //----------------------
    
    //--------fire-------
    private ArrayList<Fire> fire;
    private long fireStartTime;
    //-------------------


    //--------food-------------
    private ArrayList<Food> foods;
    private long foodStartTime;
    //--------------------------
    //-----------btn------------
    private ArrayList<StandAnimate> standing;
   

    
    
    private ArrayList<ScorePlus> scrplus;    

    //------sound------------
    private SoundPool sounds;
    //private int deadbird;
    //private int sndbeep;
    //private MediaPlayer dead,beap;
    //----------------------


    public GamePanel(Context context)
    {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);
        setFocusable(true);
        //----------sound----------
        //sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        //deadbird = sounds.load(context,R.raw.scream,1);
        //sndbeep = sounds.load(context,R.raw.beep,1);

       // dead = MediaPlayer.create(context,R.raw.scream);
       // beap = MediaPlayer.create(context,R.raw.beep);
        //-------------------------
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000)//ei "counter<1000" er value komie dekhte hobe er dara ki change hoy
        {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
    	Bitmap bird = BitmapFactory.decodeResource(getResources(),R.drawable.bg6);
        Bitmap resize_bg = Bitmap.createScaledBitmap(bird,WIDTH,HEIGHT,true);
        bg = new Background(resize_bg);
        
        Bitmap img_bird = BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_p);
        img_bird = Bitmap.createScaledBitmap(img_bird,600,628,true);
        player = new Player(img_bird,120,157,20);

        //player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_3),120,157,20);
        leveling();
        
        //--------missile and dragon and fire-----------------
        missiles = new ArrayList<Missile>();
        dragons =new ArrayList<Dragon>();
        fire = new ArrayList<Fire>();        
        missileStartTime = System.nanoTime();
        
        //---------egg and life-------
        egg = new ArrayList<Egg>();
        lifebird = new ArrayList<LifeBird>();
        
        //----------life love---------------
        lifeLoves =new ArrayList<LifeLove>();
        
        /*
        Bitmap img=BitmapFactory.decodeResource(getResources(),R.drawable.love);
    	img=Bitmap.createScaledBitmap(img, 30, 30, true);
    	for(int i=0;i<3;i++)
    	{
    		lifeLoves.add(new LifeLove(img, WIDTH-130+32*i, 12));
    	}
    	*/
        //--------------level tree------------------
    	
    	
    	levelTrees=new ArrayList<LevelTree>();
    	
    	/*
    	Bitmap img=BitmapFactory.decodeResource(getResources(), R.drawable.tree_nest_rv);
    	levelTrees.add(new LevelTree(img, 0, 0));
    	
    	img=BitmapFactory.decodeResource(getResources(),R.drawable.tree_nest);
    	levelTrees.add(new LevelTree(img, (int)(2500+500*level), 0));
    	*/
    	
    	//---------food and score----------------               
        foods = new ArrayList<Food>();
        scrplus=new ArrayList<ScorePlus>();
        
        standing =new ArrayList<StandAnimate>();
        
        Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.coin);
        img = Bitmap.createScaledBitmap(img,300,30,true);
        standing.add(new StandAnimate(img,WIDTH/2,8,30,30,10));
        
        
        
        
        
        thread.setRunning(true);
        thread.start();
    }

    
    
    
    private void leveling()
    {
        	
        //-------------leveling-------------------------------	
    	if(level==1)
    	{
    		plspeed=14;
        	foodlpsd=1400;
        	missilelpsd=1800;
    	}
        if(level==2)
        {
        	plspeed=20;
        	foodlpsd=1400;
        	missilelpsd=1800;
        }
        if(level==3)
        {
        	plspeed=26;
        	foodlpsd=1300;
        	missilelpsd=1600;
        }
        if(level==4)
        {
        	plspeed=30;
        	foodlpsd=1200;
        	missilelpsd=1400;
        	
        }
        if(level>=5)
        {
        	plspeed=34;
        	foodlpsd=1000;
        	missilelpsd=1200;
        }
        /*
        if(level==6)
        {
        	foodlpsd=950;
        	missilelpsd=1100;
        }
        if(level==7)
        {
        	foodlpsd=900;
        	missilelpsd=1000;
        }
        if(level==8)
        {
        	foodlpsd=850;
        	missilelpsd=900;
        }
        if(level==9)
        {
        	foodlpsd=800;
        	missilelpsd=800;
        }
        if(level==10)
        {
        	foodlpsd=750;
        	missilelpsd=700;
        }
        if(level==11)
        {
        	foodlpsd=725;
        	missilelpsd=650;
        }
        if(level==12)
        {
        	foodlpsd=700;
        	missilelpsd=600;
        }
        if(level==13)
        {
        	foodlpsd=675;
        	missilelpsd=550;
        }
        if(level==14)
        {
        	foodlpsd=650;
        	missilelpsd=500;
        }
        if(level==15)
        {
        	foodlpsd=625;
        	missilelpsd=450;
        }
        if(level==16)
        {
        	foodlpsd=600;
        	missilelpsd=400;
        }
        if(level==17)
        {
        	foodlpsd=575;
        	missilelpsd=350;
        }
        if(level==18)
        {
        	foodlpsd=550;
        	missilelpsd=300;
        }
        if(level==19)
        {
        	foodlpsd=500;
        	missilelpsd=250;
        }
        */
        //------------------add missile and dragon------------------------------------

    }








    @Override
    public boolean onTouchEvent(MotionEvent event)
    {    	
    	//-------------------home screen touch---------------------
    	if(running==false)
    	{
    		touch_home(event);
    	}
    	
    	
      	//-------------------playing screen touch-----------------------
    	else
    	{
    		//------------"egg Plus" button touch-------------------------
        	if(event.getX()>(WIDTH-85)*scaleFactorX && event.getX()<(WIDTH-25)*scaleFactorX
        			&& event.getY()>64*scaleFactorY && event.getY()<98*scaleFactorY)
        	{
        		 	Bitmap img_egg = BitmapFactory.decodeResource(getResources(), R.drawable.egg);
	                img_egg = Bitmap.createScaledBitmap(img_egg,35,20,true);
	                egg.add(new Egg(img_egg, player.getX()+5,player.getY()+75));
        	}
        	
    		//---------------right portion screen to bird up-------------
        	else if(plgohome==false && event.getX()>(player.getX()+100)*scaleFactorX)
	        {
	            if (event.getAction() == MotionEvent.ACTION_DOWN)
	            {
	                if (!player.getPlaying()) {
	                    player.setPlaying(true);
	                    if (player.y > -45)
	                        player.setUp(true);
	                } else {
	                    player.setUp(true);
	                }
	                return true;
	            }
	            if (event.getAction() == MotionEvent.ACTION_UP)
	            {
	                player.setUp(false);
	                return true;
	            }
	        }
	        else
	        {
	        	//---------------"egg"  touch-------------------
	        	for(int i=0;i<egg.size();i++)
	        	{
	        		if(event.getX()>(egg.get(i).getX()-10)*scaleFactorX && event.getX()<(egg.get(i).getX()+40)*scaleFactorX &&
		            		    event.getY()>(egg.get(i).getY()-10)*scaleFactorY && event.getY()<(egg.get(i).getY()+25)*scaleFactorY)
		            {
		                Bitmap bird = BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_2);
		                Bitmap resize_bird = Bitmap.createScaledBitmap(bird,250,240,true);
		                int rty =(int) ((event.getY()/2)/(WIDTH - event.getX())*10.f);
		                lifebird.add(new LifeBird(resize_bird,(int)egg.get(i).getX(),(int)egg.get(i).getY()-30,50,60,20,rty));// "event.getY()/2" work properly but why dont know
		                egg.remove(i);
		                life++;
		                if(life>3)life=3;
		            }
	        	}
	        	
	        	//--------------"pause" "play" button touch--------------------
	        	if(event.getX()>5*scaleFactorX && event.getX()<(55+20)*scaleFactorX
	        			&& event.getY()>5*scaleFactorY &&event.getY()<(55+20)*scaleFactorY)
	        	{
	        		if(!player.getPlaying()){
	        			player.setPlaying(true);
	        			if(player.getY()>-45)player.setUp(true);
	        			else player.setUp(false);
	        		}
	        		else{
	        			player.setPlaying(false);
	        		    player.setUp(false);
	        		}        		
	        	}
	        	

	        	
	        	
	        	
	        	/*
          //   }
	      //  else {
	        	//"fire" button touch
				if(event.getX()>10*scaleFactorX && event.getX()<50*scaleFactorX &&
						  event.getY()>275*scaleFactorY && event.getY()<315*scaleFactorY)
				{
					Bitmap bird = BitmapFactory.decodeResource(getResources(),R.drawable.fire6);
	                Bitmap resize_fire = Bitmap.createScaledBitmap(bird,150,200,true);
	                fire.add(new Fire(resize_fire, 140, player.getY()+40, 50, 50, 12));
				}
				*/
			}
    	}
        return super.onTouchEvent(event);
    }
    
    
    
    
    
    
    
    
    
    
    //-----------------home screen touch-----------------------
    private void touch_home(MotionEvent event)
    {
    	//--------------------if no dialogbox is open------------------------
    	if(gameover==false && helping==false && showinfo==false && showlevel==false && levelcomplete==false){
			//starting "play" button touch condition 
    		if(event.getX()>(WIDTH-120)*scaleFactorX && event.getX()<(WIDTH-40)*scaleFactorX
					&& event.getY()>(HEIGHT-120)*scaleFactorY && event.getY()<(HEIGHT-40)*scaleFactorY )
			{
				leveling();
				
				distance=0;
				
				life=3;
				coincnt=0;
				gameover = false;
				running = true;
				
				//-------bird drawing--------
				Bitmap img_bird = BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_p);
		        img_bird = Bitmap.createScaledBitmap(img_bird,600,628,true);
		        player = new Player(img_bird,120,157,20);
				//player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_3),120,157,20);
				
				//----------level tree drawing-------------
				//levelTrees.clear();
				Bitmap img=BitmapFactory.decodeResource(getResources(), R.drawable.tree_nest_rv);
		    	levelTrees.add(new LevelTree(img, 0, 0));
		    	
		    	img=BitmapFactory.decodeResource(getResources(),R.drawable.tree_nest);
		    	levelTrees.add(new LevelTree(img, (int)(2500+500*level), 0));
		    	
		    	//----------adding 3 love--------------------
		        img=BitmapFactory.decodeResource(getResources(),R.drawable.love);
		    	img=Bitmap.createScaledBitmap(img, 30, 30, true);
		    	for(int i=0;i<3;i++)
		    	{
		    		lifeLoves.add(new LifeLove(img, WIDTH-130+32*i, 12));
		    	}
				
		    	//----------reset all---------------
		    	//---"GAME OVER" and "LEVEL COMPLETE" a sobkisu remove kora hoise........
		    	/*
				missiles.clear();
				dragons.clear();
				foods.clear();
				scrplus.clear();
				egg.clear();
				lifebird.clear();
				lifeLoves.clear();
				fire.clear();
				*/
				
				player.resetScore();
				
				player.setPlaying(true);
			}
			//-----------help button---------------
	    	if(event.getX()>30*scaleFactorX && event.getX()<90*scaleFactorX
	    			&& event.getY()>(HEIGHT-110)*scaleFactorY && event.getY()<(HEIGHT-50)*scaleFactorY)
	    	{
	    		helping=true;
	    	}
	    	//-----------info button-------------
	    	if(event.getX()>130*scaleFactorX && event.getX()<190*scaleFactorX
	    			&& event.getY()>(HEIGHT-110)*scaleFactorY && event.getY()<(HEIGHT-50)*scaleFactorY)
	    	{
	    		showinfo=true;
	    	}
	    	//----------------level button----------------------------
	    	if(event.getX()>(WIDTH/2-40)*scaleFactorX && event.getX()<(WIDTH/2+40)*scaleFactorX
	    			&& event.getY()>(HEIGHT-80)*scaleFactorY && event.getY()<(HEIGHT-40)*scaleFactorY)
	    	{
	    		showlevel=true;
	    	}
    	}
    	//---------------level selecting----------------
    	if(showlevel)
    	{
	    	int levelrow=0;
	    	for(int i=0;i<19;i++)
	    	{
	    		if(i%5==0 && i!=0)levelrow++;
	    		if(event.getX()>(260+(i%5)*70-25)*scaleFactorX && event.getX()<(260+(i%5)*70+25)*scaleFactorX
	    				&& event.getY()>(80+levelrow*70-25)*scaleFactorY && event.getY()<(80+levelrow*70+25)*scaleFactorY)
	    		{
	    			leveling();
	    			showlevel=false;
	    			level=i+1;
	    			distance=0;
	    			
	    			
	    			life=3;
	    			coincnt=0;
	    			gameover = false;
	    			running = true;
	    			missilenum=1;
	    			
	    			//-------bird drawing--------
	    			Bitmap img_bird = BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_p);
	    	        img_bird = Bitmap.createScaledBitmap(img_bird,600,628,true);
	    	        player = new Player(img_bird,120,157,20);
	    			//player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_3),120,157,20);
	    			
	    			//----------level tree drawing-------------
	    			//levelTrees.clear();
	    			Bitmap img=BitmapFactory.decodeResource(getResources(), R.drawable.tree_nest_rv);
	    	    	levelTrees.add(new LevelTree(img, 0, 0));
	    	    	
	    	    	img=BitmapFactory.decodeResource(getResources(),R.drawable.tree_nest);
	    	    	levelTrees.add(new LevelTree(img, (int)(2500+500*level), 0));
	    			
	    	    	//----------adding 3 love--------------------
			        img=BitmapFactory.decodeResource(getResources(),R.drawable.love);
			    	img=Bitmap.createScaledBitmap(img, 30, 30, true);
			    	for(int j=0;j<3;j++)
			    	{
			    		lifeLoves.add(new LifeLove(img, WIDTH-130+32*j, 12));
			    	}
	    	    	
	    	    	//----------reset all---------------
	    	    	/*
	    			missiles.clear();
	    			dragons.clear();
	    			foods.clear();
	    			scrplus.clear();
	    			egg.clear();
	    			lifebird.clear();
	    			lifeLoves.clear();
	    			fire.clear();
	    			*/
	    			
	    			player.resetScore();
	    			
	    			player.setPlaying(true);
	    		}
	    			
	    	}
    	}
    	//----------ok button dialogbox---------------
    	if(event.getX()>(WIDTH/2-30)*scaleFactorX && event.getX()<(WIDTH/2+30)*scaleFactorX
    			&& event.getY()>(HEIGHT-140-30)*scaleFactorY && event.getY()<(HEIGHT-140+30)*scaleFactorY)
    	{
    		gameover=false;
    		helping=false;
    		showinfo=false;
    		showlevel=false;
    		levelcomplete=false;
    	}
    	
    	if(levelcomplete || gameover)
    	{
    		//-------------back dialog2 button-------------------------
    		if(event.getX()>(WIDTH/2-100-30)*scaleFactorX && event.getX()<(WIDTH/2-100+30)*scaleFactorX
    				&& event.getY()>(300-30)*scaleFactorY && event.getY()<(300+30)*scaleFactorY)
    		{
    			gameover=false;
        		helping=false;
        		showinfo=false;
        		showlevel=false;
        		levelcomplete=false;
    		}
    		
    		//--------------play dialog2 button----------------------------
    		if(event.getX()>(WIDTH/2+100-30)*scaleFactorX && event.getX()<(WIDTH/2+100+30)*scaleFactorX
    				&& event.getY()>(300-30)*scaleFactorY && event.getY()<(300+30)*scaleFactorY)
    		{
    			leveling();
    			running=true;
    			
    			showlevel=false;
    			distance=0;
    			
    			
    			life=3;
    			coincnt=0;
    			gameover = false;
    			running = true;
    			missilenum=1;
    			
    			//-------bird drawing--------
    			Bitmap img_bird = BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_p);
    	        img_bird = Bitmap.createScaledBitmap(img_bird,600,628,true);
    	        player = new Player(img_bird,120,157,20);
    			//player = new Player(BitmapFactory.decodeResource(getResources(),R.drawable.flying_bird_3),120,157,20);
    			
    			//----------level tree drawing-------------
    			//levelTrees.clear();
    			Bitmap img=BitmapFactory.decodeResource(getResources(), R.drawable.tree_nest_rv);
    	    	levelTrees.add(new LevelTree(img, 0, 0));
    	    	
    	    	img=BitmapFactory.decodeResource(getResources(),R.drawable.tree_nest);
    	    	levelTrees.add(new LevelTree(img, (int)(2500+500*level), 0));
    			
    	    	
    	    	//----------adding 3 love--------------------
		        img=BitmapFactory.decodeResource(getResources(),R.drawable.love);
		    	img=Bitmap.createScaledBitmap(img, 30, 30, true);
		    	for(int i=0;i<3;i++)
		    	{
		    		lifeLoves.add(new LifeLove(img, WIDTH-130+32*i, 12));
		    	}
    	    	
    	    	//----------reset all---------------
    	    	/*
    			missiles.clear();
    			dragons.clear();
    			foods.clear();
    			scrplus.clear();
    			egg.clear();
    			lifebird.clear();
    			lifeLoves.clear();
    			fire.clear();
    			*/
    	    	
    	    	
    			player.resetScore();
    			
    			player.setPlaying(true);
    			
    			
    			gameover=false;
        		helping=false;
        		showinfo=false;
        		showlevel=false;
        		levelcomplete=false;
    		}
    	}
    }








    public void update()  //update method a value gulake update kora hoise
    {
    	//---------level complete------------"GAME OVER" in missile collition-----------
    	if(plgohome)
		{
    		player.update();
    		if(player.getX()>=WIDTH-280)
    		{
    			levelcomplete=true;
    			running=false;
    			plgohome=false;
    			player.setGoHome(false);
    			
    			levelTrees.clear();
    		}
		}
    	
    	//------------when paying -------------------
        if(player.getPlaying())
        {
            bg.update();
            player.update();
            //distance++;
            // coincnt += coininc;
            
            //----------level tree update---------------
            for(int i=0;i<levelTrees.size();i++)levelTrees.get(i).update();
            
            //-----------if 2nd tree come in the screen then level complete---------------
            if(levelTrees.get(1).getX()<=WIDTH-360)
            {
            	player.setPlaying(false);
            	player.setGoHome(true);
            	plgohome=true;
            	
            	

    			//-----all clear-------------
    			missiles.clear();
				dragons.clear();
				foods.clear();
				scrplus.clear();
				egg.clear();
				lifebird.clear();
				lifeLoves.clear();
				fire.clear();
            }
            
            
            
            standing.get(0).update();

                                               
            
            long missileElapsed = (System.nanoTime() - missileStartTime)/1000000;
            //if(missileElapsed>(2000 - player.getScore()/4))//eikhane value ta totha "800" joto komabo, toto besi caman asbe
            if(missileElapsed>missilelpsd)
            {
            	//----------add yello dragon---------------------------
            	/*
            	if(missilenum%7==0)
            	{
            		Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.yello_dragon);
                	//Bitmap resize_missile = Bitmap.createScaledBitmap(img,801,816,true);
                    dragons.add(new Dragon(img,WIDTH+10,(int)(rand.nextDouble()*(HEIGHT-300)),267,136,player.getScore(),18));
            	}
            	*/
            	//----------------add red dragon----------------------
            	if(missilenum%13==0 && level>1){
            		Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.red_dragon);
                	Bitmap resize_img = Bitmap.createScaledBitmap(img,810,540,true);
                    dragons.add(new Dragon(resize_img,WIDTH+10,(int)(rand.nextDouble()*(HEIGHT-300)),270,135,player.getScore(),10));
            	}
            	//---------------upper bullet--------------
            	if(missilenum%4==0)
            	{
            		Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                	Bitmap resize_missile = Bitmap.createScaledBitmap(img,45,144,true);
                    missiles.add(new Missile(resize_missile, WIDTH+10,30,45,12,player.getScore(),12));
            	}
            	//----------------lower bullet--------------
            	else if((missilenum+2)%4==0)
            	{
            		Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                	Bitmap resize_missile = Bitmap.createScaledBitmap(img,45,144,true);
                    missiles.add(new Missile(resize_missile, WIDTH+10,HEIGHT-100,45,12,player.getScore(),12));
            	}
            	missilenum++;
            	//---------------add bullet-------------------------
                if(missiles.size()==0)
                {
                	Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                	Bitmap resize_missile = Bitmap.createScaledBitmap(img,45,144,true);
                    missiles.add(new Missile(resize_missile, WIDTH+10,HEIGHT/3,45,12,player.getScore(),12));
                }
                else
                {
                	Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.missile);
                	Bitmap resize_missile = Bitmap.createScaledBitmap(img,45,144,true);
                    missiles.add(new Missile(resize_missile,WIDTH+10,(int)(rand.nextDouble()*(HEIGHT-100)),45,12,player.getScore(),12));
                }
                missileStartTime = System.nanoTime();
            }

            //--------------missile update and remove-----------------
            for(int i=0;i<missiles.size();i++)
            {
                missiles.get(i).update();
                //if(collision(missiles.get(i),player))
                if(collision(missiles.get(i)))
                {
                	life--;
                	player.setPlaying(false);
                	missiles.remove(i);
                	if(life==0)
                	{
                		gameover=true;//--------GAME OVER----------
                		running = false;
                		
                		
                		//---------all clear---------
                		missiles.clear();
        				dragons.clear();
        				foods.clear();
        				scrplus.clear();
        				egg.clear();
        				lifebird.clear();
        				lifeLoves.clear();
        				fire.clear();
        				
        				levelTrees.clear();
                	}
                    break;
                }
                //remove missile if it is out of screen
                if(missiles.get(i).getX()<-100)
                {
                    missiles.remove(i);
                    break;
                }
            }
            //---------------dragon update and remove-----------------------
            for(int i=0;i<dragons.size();i++)
            {
            	dragons.get(i).update();
            	if(dragons.get(i).getX()<-300)dragons.remove(i);
            }
            //----------------add food---------------------------------------------------------------
            long foodElapsed = (System.nanoTime() - foodStartTime)/1000000;
            if(foodElapsed>foodlpsd)
            {
                int nxtrnd = rand.nextInt();
                if(nxtrnd%4==0)
                {
                	Bitmap apple = BitmapFactory.decodeResource(getResources(), R.drawable.apple4);
                    Bitmap resize = Bitmap.createScaledBitmap(apple,23,23,true);
                    foods.add(new Food(resize,WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT-100)), 28, 31, player.getScore()));
                }
                else if(nxtrnd%4==1)
                {
                	Bitmap apple = BitmapFactory.decodeResource(getResources(), R.drawable.mango);
                    Bitmap resize = Bitmap.createScaledBitmap(apple,25,25,true);
                    foods.add(new Food(resize,WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT-100)), 28, 31, player.getScore()));
                                    }
                else if(nxtrnd%4==2)
                {
                	Bitmap apple = BitmapFactory.decodeResource(getResources(), R.drawable.ananas2);
                    Bitmap resize = Bitmap.createScaledBitmap(apple,23,23,true);
                    foods.add(new Food(resize,WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT-100)), 28, 31, player.getScore()));
                }
                else if(nxtrnd%4==3)
                {
                	Bitmap apple = BitmapFactory.decodeResource(getResources(), R.drawable.banana);
                    Bitmap resize = Bitmap.createScaledBitmap(apple,23,23,true);
                    foods.add(new Food(resize,WIDTH + 10, (int) (rand.nextDouble() * (HEIGHT-100)), 28, 31, player.getScore()));
                }

                foodStartTime = System.nanoTime();
            }

            //-----------food update and remove--------------------------
            for(int i=0;i<foods.size();i++)
            {
                foods.get(i).update();
                if(collisionFood(foods.get(i)))
                {
                    int scr = (int)(rand2.nextDouble()*10);
                    scr = (int) (scr*level*2 + 100);
                    coincnt += scr;
                    scrplus.add(new ScorePlus("+"+scr,foods.get(i).getX(),foods.get(i).getY()));
                    foods.remove(i);
                    break;
                }
                //remove food if it is out of screen
                if(foods.get(i).getX()<-100)
                {
                    foods.remove(i);
                    break;
                }
            }

            //----------------egg------------
            long elapsed = (System.nanoTime() - eggStartTime)/1000000;
            boolean firstegg=false;
            //------comment part below have problem, because each update create eggtime a number, it need condition-------
            /*
            Random randegg=new Random();
            int eggtime= (int) (randegg.nextDouble() * 5000);
            eggtime=eggtime+5000;
            */
            if(elapsed>15000)
            {
            	//if(firstegg)
            	{
            		Bitmap img_egg = BitmapFactory.decodeResource(getResources(), R.drawable.egg);
	                img_egg = Bitmap.createScaledBitmap(img_egg,35,20,true);
	                egg.add(new Egg(img_egg, player.getX()+5,player.getY()+75));
            	}
            	//firstegg=true;
                eggStartTime = System.nanoTime();
            }
            for(int i=0; i<egg.size();i++)
            {
                egg.get(i).update();
                if(egg.get(i).getX()<-100)
                {
                    egg.remove(i);
                }
            }
            
            //----------------life love add and remove------------------
            if(lifeLoves.size()<life){
            	Bitmap img=BitmapFactory.decodeResource(getResources(),R.drawable.love);
            	img=Bitmap.createScaledBitmap(img, 30, 30, true);
            	//lifeLoves.add(new LifeLove(resize_img, WIDTH-150+35*, 55));
            	lifeLoves.add(new LifeLove(img, WIDTH-130+32*(lifeLoves.size()), 12));
            }
            if(lifeLoves.size()>life)
            	lifeLoves.remove(lifeLoves.size()-1);

            
            //----------------throwing fire----------------
            for(int i=0;i<fire.size();i++)
            {
            	fire.get(i).update();
            	for(int j=0;j<missiles.size();j++)
            	{
            		if(collisionFM(fire.get(i),missiles.get(j))==true)
            		{
            			missiles.remove(j);
            		}
            	}
            	if(fire.get(i).getX()>260)fire.remove(i);
            }
            
            //------removing lifebird out of area and update-----------
            for(int i=0;i<lifebird.size();i++)
            {
                lifebird.get(i).update();
                if(lifebird.get(i).getY()<-50)lifebird.remove(i);
            }
            
            
          //------removing scoreplus out of area and update-----------
            for(int i=0;i<scrplus.size();i++)
            {
            	scrplus.get(i).update();
            	if(scrplus.get(i).getY()<-20)scrplus.remove(i);
            }
        }
    }










    @Override
    public void draw(Canvas canvas) //draw function ke mainthread theke call kora hoise, er vitor je ongshe scaling kora hoise se ongsher sob object scalefactor onujai kaj korbe
    {
        scaleFactorX = getWidth()/(WIDTH*1.f);
        scaleFactorY = getHeight()/(HEIGHT*1.f);
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);//alada kono "draw function" korte gele sekhaneo scaling korte hobe
            
            paintbrush_btnbg =new Paint();
            paintbrush_btnbg.setColor(Color.BLACK);
            paintbrush_btnbg.setStyle(Paint.Style.FILL);
            
            paintbrush_stroke=new Paint();
            paintbrush_stroke.setStyle(Paint.Style.STROKE);
            paintbrush_stroke.setStrokeWidth(5);
            paintbrush_stroke.setColor(Color.MAGENTA);
            
            
            paintbrush_text =new Paint();
            paintbrush_text.setColor(Color.BLUE);
            paintbrush_text.setTextSize(20);
            paintbrush_text.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            	            
            paintbrush_playbg =new Paint();
            paintbrush_playbg.setColor(Color.rgb(240, rgbred,20 ));
            paintbrush_playbg.setStyle(Paint.Style.FILL);
            
           
            
            
            
            
            //-------------HOME SCREEN--------------------
            if(running == false)
            {
            	homescreendraw(canvas);
            }
            
            
            //-------------PLAYER SCREEN------------------
            else 
            {
	            bg.draw(canvas);	            	                        	            	                                                 
	            for(LevelTree lt: levelTrees)
	            {
	            	lt.draw(canvas);
	            }	            
	            player.draw(canvas);
	            
	            
	            
	            for (Missile m : missiles)
	            {
	                m.draw(canvas);
	            }
	            for(Dragon d:dragons)
	            {
	            	d.draw(canvas);
	            }
	
	            //-----------egg---
	            //drawing egg
	            for (Egg sp : egg)
	            {
	                sp.draw(canvas);
	            }
	            for (LifeBird lb : lifebird)
	            {
	                lb.draw(canvas);
	            }
	            
	            for(Fire fr:fire)
	            {
	            	fr.draw(canvas);
	            }
	            //------------------
	
	            for(Food f: foods)
	            {
	                f.draw(canvas);
	            }
	            
	            
	            //------------life love-----------------------
	            canvas.drawRect(WIDTH-130,10,WIDTH-30,44, paintbrush_btnbg);
	            canvas.drawCircle(WIDTH-130,10+17,17, paintbrush_btnbg);
	            canvas.drawCircle(WIDTH-30,10+17,17, paintbrush_btnbg);
	            for(LifeLove ll:lifeLoves)
	            {
	            	ll.draw(canvas);
	            }
	            
	            //-----------"egg plus" button---------------------
	            canvas.drawRect(WIDTH-80,44+20,WIDTH-30,44+20+34, paintbrush_btnbg);
	            canvas.drawCircle(WIDTH-80,44+20+17,17, paintbrush_btnbg);
	            canvas.drawCircle(WIDTH-30,44+20+17,17, paintbrush_btnbg);
	            
	            Bitmap img_egg = BitmapFactory.decodeResource(getResources(), R.drawable.egg);
                img_egg = Bitmap.createScaledBitmap(img_egg,35,20,true);
                //egg.add(new Egg(img_egg, player.getX(),player.getY()+65));
	            canvas.drawBitmap(img_egg,WIDTH-90, 44+20+5, null);
	            paintbrush_text.setColor(Color.MAGENTA);
	            paintbrush_text.setTextSize(50);
	            canvas.drawText("+",WIDTH-90+40, 44+20+30, paintbrush_text);
	            
	            	            
	            
	            for(ScorePlus sp: scrplus)
	            {
	            	sp.draw(canvas);
	            }
	            
	            
	            //------"fire" button--------------
	            
	            //canvas.drawCircle(30, 295, 18, paintbrush_btnbg);
	            
	            //--------"pause" button----------------------
	            if(player.getPlaying())
	            {
	            	Bitmap bird = BitmapFactory.decodeResource(getResources(),R.drawable.pause);
	                Bitmap resize_bg = Bitmap.createScaledBitmap(bird,60,60,true);
	            	canvas.drawBitmap(resize_bg, 10, 8,null);
	            }
	            else {
	            	Bitmap bird = BitmapFactory.decodeResource(getResources(),R.drawable.play);
	                Bitmap resize_bg = Bitmap.createScaledBitmap(bird,60,60,true);
	            	canvas.drawBitmap(resize_bg, 10, 8,null);
	            }
	
	            for(StandAnimate cn:standing)
	            {
	            	cn.draw(canvas);
	            }
	            
	            //------------------------distance, coin and life----------------------------------
	            Paint paint = new Paint();
	            paint.setColor(Color.BLACK);
	            paint.setTextSize(25);
	            paint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
	            canvas.drawText(""+distance+" m",140,30,paint);
	            canvas.drawText(""+coincnt,WIDTH/2+40,30,paint);
	            canvas.drawText("Level: "+level, WIDTH-100, 200, paint);
            
            }
                        

          
            canvas.restoreToCount(savedState);
        }
    }
    
    
    
    
    private void homescreendraw(Canvas canvas)
    {
    	//-----------home background----------------
    	Bitmap bg = BitmapFactory.decodeResource(getResources(),R.drawable.bg3);
        Bitmap resize_bg = Bitmap.createScaledBitmap(bg,WIDTH,HEIGHT,true);
    	canvas.drawBitmap(resize_bg,0,0,null);
    	

    	
    	//-----------left tree--------------------
    	bg = BitmapFactory.decodeResource(getResources(),R.drawable.tree_nest_rv);
    	canvas.drawBitmap(bg,0,0,null);
    	
    	//-----------right tree----------------
    	bg = BitmapFactory.decodeResource(getResources(),R.drawable.tree_nest);
        bg = Bitmap.createScaledBitmap(bg,300,HEIGHT,true);
    	canvas.drawBitmap(bg,WIDTH-300,0,null);
    	
    	//----------home bird-------------
    	Bitmap img=  BitmapFactory.decodeResource(getResources(), R.drawable.bird);
    	img = Bitmap.createScaledBitmap(img,150,120,true);
    	canvas.drawBitmap(img, 120,120, null);
    	//----------home dragon-------------
    	img=  BitmapFactory.decodeResource(getResources(), R.drawable.single_dragon);
    	img = Bitmap.createScaledBitmap(img,250,75,true);
    	canvas.drawBitmap(img,400,150, null);    	
    	//----------dragon fire-------------
    	img= BitmapFactory.decodeResource(getResources(), R.drawable.dragonfire);
    	//canvas.drawBitmap(img,350,135,null);
    	
    	//-------------GAME NAME--------------------------------
    	
    	paintbrush_btnbg.setColor(Color.CYAN);
    	canvas.drawRect(250,10,WIDTH-250,80, paintbrush_btnbg);

    	canvas.drawCircle(250, 10, 10, paintbrush_btnbg);
    	canvas.drawCircle(WIDTH-250, 10, 10, paintbrush_btnbg);
    	canvas.drawCircle(250, 80, 10, paintbrush_btnbg);
    	canvas.drawCircle(WIDTH-250, 80, 10, paintbrush_btnbg);
    	paintbrush_btnbg.setColor(Color.BLACK);
    	
    	paintbrush_text.setColor(Color.RED);
    	paintbrush_text.setTextSize(50);
    	canvas.drawText("Flying Bird", 280, 60, paintbrush_text);
    	paintbrush_text.setColor(Color.BLUE);
    	paintbrush_text.setTextSize(20);
    	
    	
    	//-----------home button background--------------
        rgbred += rgbinc;
        if(rgbred>250 || rgbred<80)rgbinc *= -1;
        
        
      //-------------helping text--------------	            
        if(helping){
        	dialogbox1(canvas);
        	
        	
        	paintbrush_text.setColor(Color.WHITE);
        	paintbrush_text.setTextSize(20);
            canvas.drawText("Touch   'Right'   portion   to   control   bird", 205, 100, paintbrush_text);
            canvas.drawText("Touch   'egg'   to   earn   life", 250, 135, paintbrush_text);
            canvas.drawText("Avoid   bullet", 350, 170, paintbrush_text);
            canvas.drawText("Feed   fruit   to   earning   coin", 240, 205, paintbrush_text);
        }
        else if(showinfo){
        	dialogbox1(canvas);
        	
        	paintbrush_text.setColor(Color.WHITE);
        	paintbrush_text.setTextSize(20);
        	canvas.drawText("Designed and Developed by", 260, 100, paintbrush_text);
        	
        	paintbrush_text.setTextSize(25);
            canvas.drawText("Nur Ahmadullah", 240, 135, paintbrush_text);
            paintbrush_text.setColor(Color.MAGENTA);
            canvas.drawText("(SoHaG)", 450, 135, paintbrush_text);
            
            paintbrush_text.setColor(Color.WHITE);
            paintbrush_text.setTextSize(20);
            canvas.drawText("Studying: HSTU, CSE", 260, 170, paintbrush_text);            
        }
        else if(showlevel)
        {
        	dialogbox1(canvas);
        	
        	paintbrush_text.setTextSize(20);
        	paintbrush_text.setColor(Color.BLUE);
            paintbrush_text.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        	
        	paintbrush_btnbg.setColor(Color.WHITE);
        	int levelrow=0;
        	for(int i=0;i<19;i++)
        	{
        		if(i%5==0 && i!=0)levelrow++;
        		canvas.drawCircle(260+(i%5)*70,80+levelrow*70,25,paintbrush_btnbg);
        		canvas.drawText(""+(i+1), 260+(i%5)*70-7, 80+levelrow*70+5, paintbrush_text);        		
        	}
        	paintbrush_btnbg.setColor(Color.BLACK);
        }
        else if(levelcomplete)
        {
        	dialogbox2(canvas);
        	paintbrush_text.setColor(Color.WHITE);
        	paintbrush_text.setTextSize(20);
        	canvas.drawText("Level Completed", 300, HEIGHT/3, paintbrush_text);
        }
        else if(gameover)
        {
        	//--------dialogbox-----------
        	dialogbox2(canvas);
        	
        	//--------game over text----------------
            Paint paint1 = new Paint();
            paint1.setColor(Color.RED);
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
            canvas.drawText("GAME OVER",WIDTH/3,HEIGHT/3,paint1);	                
        }
        else
        {
        	paintbrush_text.setColor(Color.WHITE);
        	paintbrush_text.setTextSize(25);
                
        //--------------drawing help button----------------------
        canvas.drawCircle(60,HEIGHT-80, 30, paintbrush_btnbg);
        canvas.drawCircle(60,HEIGHT-80, 30, paintbrush_stroke);
        canvas.drawText("help",35 , HEIGHT-80+10, paintbrush_text);
        
        //---------drawing info button------------------------
        canvas.drawCircle(160,HEIGHT-80, 30, paintbrush_btnbg);
        canvas.drawCircle(160,HEIGHT-80, 30, paintbrush_stroke);
        canvas.drawText("info",135 , HEIGHT-80+10, paintbrush_text);
        
        //--------level button-------------------------------                
        canvas.drawCircle(WIDTH/2-40, HEIGHT-60, 20, paintbrush_btnbg);
        canvas.drawCircle(WIDTH/2-40, HEIGHT-60, 20, paintbrush_stroke);
        canvas.drawCircle(WIDTH/2+40, HEIGHT-60, 20, paintbrush_btnbg);
        canvas.drawCircle(WIDTH/2+40, HEIGHT-60, 20, paintbrush_stroke);
        
        
        canvas.drawRect(WIDTH/2-35, HEIGHT-80,WIDTH/2+35,HEIGHT-40,paintbrush_stroke);
        canvas.drawRect(WIDTH/2-40, HEIGHT-80,WIDTH/2+40,HEIGHT-40,paintbrush_btnbg);
        canvas.drawText("LEVEL",WIDTH/2-40,HEIGHT-60+10,paintbrush_text);
        
        //--------------drawing play button--------------------
        paintbrush_btnbg.setColor(Color.MAGENTA);
        paintbrush_stroke.setColor(Color.BLACK);
        canvas.drawCircle(WIDTH-80, HEIGHT-80, 40, paintbrush_btnbg);
        canvas.drawCircle(WIDTH-80, HEIGHT-80, 40, paintbrush_stroke);
        paintbrush_btnbg.setColor(Color.BLACK);
        paintbrush_stroke.setColor(Color.MAGENTA);
        
        paintbrush_text.setTextSize(30);
        canvas.drawText("Play", WIDTH-110,HEIGHT-80+10,paintbrush_text);
        
        }
        
        
    }
    
    private void dialogbox1(Canvas canvas)
    {
    	//-----------rectangel----------------
    	paintbrush_btnbg.setColor(Color.DKGRAY);
    	canvas.drawRect(200,50,WIDTH-200,HEIGHT-50, paintbrush_btnbg);
    	//-----------corner circle-----------------
    	canvas.drawCircle(200, 50, 10, paintbrush_btnbg);
    	canvas.drawCircle(WIDTH-200, 50, 10, paintbrush_btnbg);
    	canvas.drawCircle(200, HEIGHT-50, 10, paintbrush_btnbg);
    	canvas.drawCircle(WIDTH-200, HEIGHT-50, 10, paintbrush_btnbg);
    	paintbrush_btnbg.setColor(Color.BLACK);
    	
    	//--------------ok button drawing--------------------
    	canvas.drawCircle(WIDTH/2, HEIGHT-140, 30, paintbrush_playbg);
    	if(showlevel){
    		paintbrush_text.setTextSize(50);
    		canvas.drawText("<-", WIDTH/2-20,HEIGHT-140+15,paintbrush_text);
    		canvas.drawText("-", WIDTH/2-14,HEIGHT-140+15,paintbrush_text);
    		canvas.drawText("-", WIDTH/2-8,HEIGHT-140+15,paintbrush_text);
    	}
    	else
    		canvas.drawText("Ok", WIDTH/2-20,HEIGHT-140+10,paintbrush_text);
    }
    
    private void dialogbox2(Canvas canvas)
    {
    	//-----------rectangel----------------
    	paintbrush_btnbg.setColor(Color.GRAY);
    	canvas.drawRect(200,80,WIDTH-200,350, paintbrush_btnbg);
    	//-----------corner circle-----------------
    	canvas.drawCircle(200, 80, 10, paintbrush_btnbg);
    	canvas.drawCircle(WIDTH-200, 80, 10, paintbrush_btnbg);
    	canvas.drawCircle(200, 350, 10, paintbrush_btnbg);
    	canvas.drawCircle(WIDTH-200, 350, 10, paintbrush_btnbg);
    	paintbrush_btnbg.setColor(Color.BLACK);
    	
    	//--------------back button drawing--------------------
    		canvas.drawCircle(WIDTH/2-100, 300, 30, paintbrush_playbg);
    		paintbrush_text.setTextSize(50);
    		canvas.drawText("<-", WIDTH/2-100-20,300+15,paintbrush_text);
    		canvas.drawText("-", WIDTH/2-100-14,300+15,paintbrush_text);
    		canvas.drawText("-", WIDTH/2-100-8,300+15,paintbrush_text);
    	
    	//--------------play button drawing---------------------------
    		canvas.drawCircle(WIDTH/2+100, 300, 30, paintbrush_playbg);
    		paintbrush_text.setTextSize(20);
    		if(gameover)canvas.drawText("Again", WIDTH/2+100-20,300+10,paintbrush_text);
    		else canvas.drawText("next", WIDTH/2+100-20,300+10,paintbrush_text);
				
    }







    
    
    
    
    
    
    
    
    
    


    //public boolean collision(GameObject a,GameObject b)
    public boolean collision(GameObject a)
    {
        if( Rect.intersects(a.getRectangle(),player.getPlayerRect()) )
        {
            //----------sound--------------
            //sounds.play(deadbird,1,1,0,0,1);
            //dead.start();
            //-----------------------------
            //score /= 3;
            return  true;
        }
        return  false;
    }
    public boolean collisionFood(GameObject a)
    {
        if( Rect.intersects(a.getRectangle(),player.getPlayerRect()) )
        {
            //----------sound--------------
            //sounds.play(sndbeep,1,1,0,0,1);
            //beap.start();
            //-----------------------------
            return  true;
        }
        return  false;
    }
    
    public boolean collisionFM(Fire a,GameObject b)
    {
        if( Rect.intersects(a.getFireRect(),b.getRectangle()))
        {
            return  true;
        }
        return  false;
    }
    
    //---------------------back button handle----------------------
    /*
    public void pause() {
    	thread.setRunning(false);
    	while(true){
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	thread=null;
	}
    
    public void resume(){
    	thread.setRunning(true);
    	getHolder().addCallback(this);
    	thread=new MainThread(getHolder(), GamePanel.this);
    	setFocusable(true);
    	thread.start();
    }
    */
    
}



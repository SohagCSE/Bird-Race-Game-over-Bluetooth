package com.totocompany.flyingbird;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	private GamePanel gamePanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        gamePanel=new GamePanel(this);
        setContentView(gamePanel);
	}
	
	
	//------------------back button handle-------------------
	/*
	@Override
	protected void onPause(){
		super.onPause();
		gamePanel.pause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		gamePanel.resume();
	}
	
	*/
}

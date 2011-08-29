package com.grender.boxyTest;

import java.io.IOException;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.sound.SoundEngine;

import com.grender.boxyTest.utils.SoundMusicEngine;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class main extends Activity implements SensorEventListener {
	
	protected CCGLSurfaceView _glSurfaceView;
	private Bundle saveBundle;
	//private SensorManager sensorManager;
	private MenuScene scene;
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i("Shampoo","onCreate");
	    super.onCreate(savedInstanceState);
	 
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	 
	    Display display = getWindowManager().getDefaultDisplay();
	    SoundMusicEngine.sharedEngine().setContext(this);
	    
	    _glSurfaceView = new CCGLSurfaceView(this); 
	    setContentView(_glSurfaceView);
	    if(savedInstanceState!=null && savedInstanceState.getBoolean("isSavedState",false))
	    {
	    	saveBundle=savedInstanceState;
	    }
	    
	    /*
	    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
				*/
	}
	
	@Override
	public void onStart()
	{
		Log.i("Shampoo","onStart");
		super.onStart();
	    CCDirector.sharedDirector().attachInView(_glSurfaceView);
	    CCDirector.sharedDirector().setDisplayFPS(true); 
	    CCDirector.sharedDirector().setAnimationInterval(1.0f / 30.0f);
	    CCScene t = MenuScene.scene(this);
	    CCDirector.sharedDirector().runWithScene(t);
	    scene=(MenuScene)t.getChildren().get(0);
	    
	}	
	

	@Override
	public void onPause()
	{
		super.onPause();
	    CCDirector.sharedDirector().pause();
	    SoundMusicEngine.sharedEngine().pauseBackgroundMusic();
	    
	}
	 
	@Override
	public void onResume()
	{
		super.onResume();	
	    CCDirector.sharedDirector().resume();
	    SoundMusicEngine.sharedEngine().resumeBackgroundMusic();
	    /*
	    sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		*/
	    
	}
	 
	@Override
	public void onStop()
	{
		super.onStop();
	    CCDirector.sharedDirector().end();
	    SoundMusicEngine.sharedEngine().stopBackgroundMusic();
	    //sensorManager.unregisterListener(this);
	}	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    if(savedInstanceState!=null && savedInstanceState.getBoolean("isSavedState",false))
	    {
	    	saveBundle=savedInstanceState;
	    }
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		
		//scene.updateGravity(event.values[1]);
		   // If the sensor data is unreliable return
	    if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
	        return;

	    
	    float[] inR = new float[16];
	    float[] I = new float[16];
	    float[] orientVals = new float[3];
	    
	    float[] gravity=null;
		float[] geomag={0,0,0};
		// Gets the value of the sensor that has been changed
	    switch (sensorEvent.sensor.getType()) {  
	        case Sensor.TYPE_ACCELEROMETER:
	            gravity = sensorEvent.values.clone();
	            break;
	        case Sensor.TYPE_MAGNETIC_FIELD:
	            geomag = sensorEvent.values.clone();
	            break;
	    }

	    // If gravity and geomag have values then find rotation matrix
	    if (gravity != null && geomag != null) {

	        // checks that the rotation matrix is found
	        boolean success = SensorManager.getRotationMatrix(inR, I,
	                                                          gravity, geomag);
	        if (success) {
	            SensorManager.getOrientation(inR, orientVals);
	            float azimuth = (float) Math.toDegrees(orientVals[0]);
	            float pitch = (float) Math.toDegrees(orientVals[1]);
	            float roll = (float) Math.toDegrees(orientVals[2]);
	            Log.i("Boxy",azimuth+" "+pitch+" "+roll);
	        }
	    }
		
	}
	
}
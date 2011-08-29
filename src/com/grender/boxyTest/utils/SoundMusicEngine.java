package com.grender.boxyTest.utils;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

public class SoundMusicEngine {

    static SoundMusicEngine _sharedEngine = null;
    
    private Context context;
    private HashMap<String, MediaPlayer> effectsHash;
    private MediaPlayer musicPlayer;

    public static SoundMusicEngine sharedEngine() {
        synchronized(SoundMusicEngine.class) {
            if (_sharedEngine == null) {
                _sharedEngine = new SoundMusicEngine();
            }
        }
        return _sharedEngine;
    }

    private SoundMusicEngine()
    {
    	effectsHash=new HashMap<String, MediaPlayer>();
    }

   	public void setContext(Context context)
   	{
   		this.context=context;
   	}
    
	public void playBackgroundMusic(String fileName) {
		try {
	        if(musicPlayer!=null)
	        	musicPlayer.release();
	        musicPlayer=new MediaPlayer();
	        AssetFileDescriptor afd = context.getAssets().openFd(fileName);
	        musicPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
	        musicPlayer.setLooping(true);
	        musicPlayer.prepare();
	        musicPlayer.start();
		}
		catch(Exception ex) {	
		}
	}

	public void stopBackgroundMusic() {
		if(musicPlayer!=null && musicPlayer.isPlaying())
			musicPlayer.stop();
	}

	public void preloadEffect(String fileName) {
		if(effectsHash.containsKey(fileName))
			return;
		try {
	        AssetFileDescriptor afd = context.getAssets().openFd(fileName);
	        MediaPlayer player=new MediaPlayer();
	        player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
	        player.prepare();
	        effectsHash.put(fileName, player);
		}
		catch(Exception ex) {	
		}
	}

	public void playEffect(String fileName) {
		if(!effectsHash.containsKey(fileName))
			return;
		effectsHash.get(fileName).seekTo(0);
		effectsHash.get(fileName).start();
	}
	
	public void setBackgroundMusicVolume(float vol)
	{
		if(musicPlayer!=null)
			musicPlayer.setVolume(vol,vol);
	}

	public void pauseBackgroundMusic() {
		if(musicPlayer!=null)
			musicPlayer.pause();
		
	}

	public void resumeBackgroundMusic() {
		if(musicPlayer!=null)
			musicPlayer.start();
	}
	
}

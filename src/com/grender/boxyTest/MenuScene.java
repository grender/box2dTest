package com.grender.boxyTest;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.cocos2d.actions.UpdateCallback;
import org.cocos2d.actions.ease.CCEaseOut;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCScaleTo;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.events.CCTouchDispatcher;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.transitions.CCFadeTransition;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.jbox2d.collision.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.contacts.ContactPoint;
import org.jbox2d.dynamics.contacts.ContactResult;

import com.grender.boxyTest.utils.SoundMusicEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

public class MenuScene extends CCLayer implements UpdateCallback {

	public static CCScene scene(Activity activity) {
		CCScene scene = CCScene.node();
		CCLayer layer = new MenuScene(activity);
		scene.addChild(layer);
		return scene;
	}

	private PhysicsWorld mWorld;
	private List<CCSprite> sprites;
	private SensorManager mSensorManager;
	private CGPoint startGravity;
	
	
	private MenuScene(Activity activity) {
		setIsTouchEnabled(true);
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		mSensorManager=(SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        mWorld = new PhysicsWorld();  
        mWorld.create(display.getWidth(),display.getHeight());  
  
        sprites=new Vector<CCSprite>();
        // Add 50 Balls  
        for (int i=0; i<20; i++) {
        	
            mWorld.addBox();
            CCSprite sprite=CCSprite.sprite("debug_rect.png");
            sprites.add(sprite);
            addChild(sprite);
            sprite.setScale(mWorld.aspect/5);  
            
            mWorld.addHex();
            sprite=CCSprite.sprite("6edges.png");
            sprites.add(sprite);
            addChild(sprite);
            sprite.setScale(mWorld.aspect/10);            
        }
        SoundMusicEngine.sharedEngine().preloadEffect("click.mp3");
        mWorld.setContactListener(new ContactListener() {
			
			@Override
			public void result(ContactResult point) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void remove(ContactPoint point) {
				// TODO Auto-generated method stub
				
			}
			
			private Object getUserData(Shape shape)
			{
				if(shape==null)
					return null;
				if(shape.m_body==null)
					return null;
				return shape.m_body.m_userData;
			}
			
			@Override
			public void persist(ContactPoint point) {
			}
			
			@Override
			public void add(ContactPoint point) {
				/*
				if(point==null)
					return;
				Object o1 = getUserData(point.shape1);
				Object o2 = getUserData(point.shape1);
				if(o1==null)o1="";
				if(o2==null)o2="";
				if( (!o1.equals("MyBox")) || 
						  (!o2.equals("MyBox")) )
					{
						SoundMusicEngine.sharedEngine().playEffect("click.mp3");
					}
					*/
			}
		});
        
        scheduleUpdate();
	}
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		CGPoint point = CGPoint.ccp(event.getX(), event.getY());
		point = CCDirector.sharedDirector().convertToGL(point);
		startGravity=point;
		return true;
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		return false;
	}

	@Override
	public boolean ccTouchesEnded(MotionEvent event) {
		CGPoint point = CGPoint.ccp(event.getX(), event.getY());
		point = CCDirector.sharedDirector().convertToGL(point);
		Vec2 gravVector = new Vec2(point.x-startGravity.x,point.y-startGravity.y);
		gravVector.normalize();
		gravVector.mulLocal(10);
		mWorld.setGravity(gravVector);
		return true;
	}

	@Override
	public void update(float d) {
		mWorld.update(d);
		for(int i=0;i<mWorld.bodies.size();i++) {
			Body body = mWorld.bodies.get(i);
			draw(body,sprites.get(i));			
		}
	}

	private void draw(Body body,CCSprite sprite) {	
		Vec2 center=body.getPosition();
		sprite.setPosition(center.x*mWorld.aspect,center.y*mWorld.aspect);
		sprite.setRotation((float) (body.getAngle()/Math.PI*180));
	}

	public void updateGravity(float f) {
		// TODO Auto-generated method stub
		mWorld.setGravity(new Vec2( (float)Math.sin(f*Math.PI/180)*10f , (float)Math.cos(f*Math.PI/180)*10f ));
	}
}

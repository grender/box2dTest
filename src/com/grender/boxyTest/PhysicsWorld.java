package com.grender.boxyTest;

import java.util.List;
import java.util.Vector;

import org.jbox2d.collision.AABB;  
import org.jbox2d.collision.CircleDef;  
import org.jbox2d.collision.PolygonDef;  
import org.jbox2d.common.Vec2;  
import org.jbox2d.dynamics.Body;  
import org.jbox2d.dynamics.BodyDef;  
import org.jbox2d.dynamics.ContactListener;
import org.jbox2d.dynamics.World;  
  
import android.util.Log;  
  
public class PhysicsWorld {  
    public int targetFPS = 40;  
    public int timeStep = (1000 / targetFPS);  
    public int iterations = 5;  
  
    public List<Body> bodies;  

  
    private AABB worldAABB;  
    private World world;  
    
	public float aspect=15;  

    public Vec2 toScreen(Vec2 from)
    {
    	return new Vec2(from.x*aspect,from.y*aspect);
    }
    
    public Vec2 fromScreen(Vec2 from)
    {
    	return new Vec2(from.x/aspect,from.y/aspect);
    }
    
    public void create(int screenWidth,int screenHeight) { 
    	bodies=new Vector<Body>();

        // Step 1: Create Physics World Boundaries  
        worldAABB = new AABB();  
        worldAABB.lowerBound.set(fromScreen(new Vec2(-20.0f,-20.0f)));  
        worldAABB.upperBound.set(fromScreen(new Vec2(screenWidth+20f, screenHeight+20f)));  
  
        // Step 2: Create Physics World with Gravity  
        Vec2 gravity = new Vec2((float) 0.0, (float) -10.0);  
        boolean doSleep = false;  
        world = new World(worldAABB, gravity, doSleep);  
  
        // Step 3: Create Ground Box  
        createBorderFromScreen(screenWidth/2f,            -10,screenWidth,20);
        createBorderFromScreen(screenWidth/2f,screenHeight+10,screenWidth,20);
        createBorderFromScreen(           -10,screenHeight/2f,20,screenHeight);
        createBorderFromScreen(screenWidth+10,screenHeight/2f,20,screenHeight);
    }  
  
    private void createBorderFromScreen(float x,float y,float w,float h)
    {
        BodyDef groundBodyDef = new BodyDef();  
        groundBodyDef.position.set(new Vec2(x/aspect,y/aspect));  
        Body groundBody = world.createBody(groundBodyDef);  
        PolygonDef groundShapeDef = new PolygonDef();  
        groundShapeDef.setAsBox(w/2/aspect,h/2/aspect);  
        groundBody.createShape(groundShapeDef);
    }
    
    public void setContactListener(ContactListener listener)
    {
    	world.setContactListener(listener);
    }
    
    public void addBox() {
    	BodyDef bd=new BodyDef();
        PolygonDef def=new PolygonDef();
        def.setAsBox(1.0f,1.0f);
        def.density=1.0f;
        def.friction=0.3f;
        def.restitution=0.0f;
    	bd.position=new Vec2((float) (6.0f+Math.random()), 1.5f+bodies.size()*2.2f);
        Body body = world.createBody(bd);
        body.createShape(def);  
        body.setMassFromShapes();
    	body.m_userData="MyBox";
        bodies.add(body);
    }  

    public void addHex() {
    	BodyDef bd=new BodyDef();
        PolygonDef def=new PolygonDef();
        
        def.clearVertices();
        def.addVertex(new Vec2(-0.8660254037f,0.5f));
        def.addVertex(new Vec2(-0.8660254037f,-0.5f));
        def.addVertex(new Vec2(0,-1));
        def.addVertex(new Vec2(0.8660254037f,-0.5f));
        def.addVertex(new Vec2(0.8660254037f,0.5f));
        def.addVertex(new Vec2(0,1));
        
        def.density=1.0f;
        def.friction=1.0f;
        def.restitution=0.0f;
    	bd.position=new Vec2((float) (6.0f+Math.random()), 1.5f+bodies.size()*2.2f);
        Body body = world.createBody(bd);
        body.createShape(def);  
        body.setMassFromShapes();
    	body.m_userData="MyHex";
        bodies.add(body);
    }     
    
    public void update(float dt) {  
        // Update Physics World  
        //world.step(dt, (int)(dt/5.0f)+1);  
    	//world.step(dt,15);
    	world.step(1f/30f,4);
     }
    
    public void setGravity(Vec2 gravity) { 
    	world.setGravity(gravity);
    }
}  
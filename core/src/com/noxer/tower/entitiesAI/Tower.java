package com.noxer.tower.entitiesAI;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class Tower{
	
	private Body body;
	public float posX, posY, width, height;
	public NinePatchDrawable patchBack, patchHP;
	
	public Tower(NinePatchDrawable patchBack, NinePatchDrawable patchHP, float posX, float posY,
			float width, float height){
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.patchBack = patchBack;
		this.patchHP = patchHP;
	}
	
	
	public void initBody(World world, int playerNum) {
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(posX, posY - height/2);
        body = world.createBody(bodyDef);
        body.setUserData(this);
        CircleShape shape = new CircleShape();
        shape.setRadius(width*1.4f);
        //body.setTransform(body.getPosition(), (float) (Math.PI/4));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.1f;
        fixtureDef.restitution = 0.6f;
        fixtureDef.isSensor = true; // use it on towers not to react but detect collision
        body.createFixture(fixtureDef);
        shape.dispose();
	}

	/*@Override
    public void draw(Batch batch) {
		super.draw(batch);
    }*/
}



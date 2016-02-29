package com.noxer.tower.entitiesAI;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.noxer.tower.entities.Ball;
import com.noxer.tower.entities.BallBasic;
import com.noxer.tower.maths.SteeringUtils;


public class BallBasicAI extends BallBasic implements Steerable<Vector2>{
	private boolean tagged;
	private float boundingRadius, maxLinearAcceleration;
	private float maxAngularSpeed, maxAngularAcceleration;
	private boolean anyAcceleration;
	private Vector2 velocity2;
	
	SteeringBehavior<Vector2> behavior;
	SteeringAcceleration<Vector2> steerOutput;
	
	public BallBasicAI(Sprite sprite, TiledMapTileLayer collisionLayer, World world, NinePatchDrawable patchBack, NinePatchDrawable patchHP,
			TextureRegion[] explosion){
		super(sprite, collisionLayer, world, patchBack, patchHP, explosion);
		this.maxAngularAcceleration = speed/2;
		this.maxAngularSpeed = speed;
		this.maxLinearAcceleration = speed*500;
		this.tagged = false;
		this.steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
	}
	
	public void update(float delta){
		super.update(delta);
		if (behavior != null){
			if (timeFromCollision > threshold && usingAI) {
				behavior.calculateSteering(steerOutput);
				if(behavior instanceof Pursue){
					if( ((Ball) ((Pursue<Vector2>) behavior).getTarget() ).alive )applySteering(delta);
					
				}
				else applySteering(delta);
			}
		}//if (obj instanceof C) {
	}
	
	private void applySteering(float delta){
		anyAcceleration = false;
		if (!steerOutput.linear.isZero()){
			steerOutput.linear.scl(collisionX? 0 : 1, collisionY? 0 : 1);
			body.applyForceToCenter(steerOutput.linear, true); //it already takes care of delta time
			anyAcceleration = true;
		}
		if (steerOutput.angular != 0){ //independent facing
			body.applyTorque(steerOutput.angular, true); // this method internally scales the torque by deltaTime
			anyAcceleration = true;
		}
		else if (!getLinearVelocity().isZero(0.005f)){ //dependent facing
			body.setAngularVelocity((vectorToAngle(getLinearVelocity()) - getAngularVelocity()) * delta);
			body.setTransform(body.getPosition(), vectorToAngle(getLinearVelocity()));
		}
		
		if (anyAcceleration){
			velocity2 = body.getLinearVelocity();
			//forma de calcular que si uno de los ejes pasa de la velocidad se escale 
			//a que ese eje como maximo sea la velocidad max
			if (velocity2.len2() > speed * speed){
				body.setLinearVelocity(velocity2.scl(speed/velocity2.len()));
			}
			if (body.getAngularVelocity() > maxAngularSpeed){
				body.setAngularVelocity(maxAngularSpeed);
			}
		}
	}

	@Override
	public float getMaxLinearSpeed() {
		return speed;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		speed = maxLinearSpeed;
		
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
		
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public float getOrientation() {
		return body.getAngle();
	}

	@Override
	public Vector2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadius;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}

	public Vector2 newVector() {
		return new Vector2();
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return SteeringUtils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return SteeringUtils.angleToVector(outVector, angle);
	}
	
	public void setBehavior(SteeringBehavior<Vector2> behavior){
		this.behavior = behavior;
	}
	
	public SteeringBehavior<Vector2> getBehavior(){
		return behavior;
	}

	@Override
	public void setOrientation(float orientation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Location<Vector2> newLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		// TODO Auto-generated method stub
		
	}
	
}
	

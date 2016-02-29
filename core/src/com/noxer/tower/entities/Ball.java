package com.noxer.tower.entities;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.noxer.tower.maths.CoordConverter;
import com.noxer.tower.screens.Nivel;

public abstract class Ball extends Sprite {
	
	private Vector2 velocity = new Vector2();
	private float spriteW, spriteH, aimX, aimY, xDist, yDist, timeDied;
	protected NinePatchDrawable loadingBarBackground, loadingBar;
	public float speed, life, maxLife, damage, shield, timeFromCollision, progress, inertiaInit, inertiaNow, inertiaMax;
	public boolean alive, justDied, stopMoving, usingAI, movingWithPad, collided;
	protected boolean collisionX, collisionY;
	public Body body;
	protected float threshold;
	private TextureRegion[] explosion;
	public TiledMapTileLayer collisionLayer;
	final short PLAYER1 = 0x1;    // 0001
    final short PLAYER2 = 0x1 << 1; // 0010 or 0x2 in hex
    private Nivel game;
    private boolean checkCollision;
	
	public Ball(Sprite sprite, TiledMapTileLayer collisionLayer, World world, NinePatchDrawable patchBack, NinePatchDrawable patchHP,
			TextureRegion[] explosion){
		super(sprite);
		this.collisionLayer = collisionLayer;
		alive = true;
		stopMoving = true;
		spriteW = getWidth(); 
		spriteH = getHeight(); 
		collided = false;
		timeFromCollision = 0;
		threshold = 0.9f;
		loadingBarBackground = patchBack;
		loadingBar = patchHP;
		timeDied = 0;
		justDied = false;
		this.explosion = explosion;
		inertiaMax = 2.5f;
	}
	
	public abstract void initBody(World world, int playerNum);
	
	public void setAimCoords(float x, float y){
		aimX = x;
		aimY = y;
		stopMoving = false;
		//collided = false;
		movingWithPad = false;
	}
	
	public void setGame(Nivel game){
		this.game = game;
	}

	public void draw(Batch spriteBatch){
		update(Gdx.graphics.getDeltaTime());
		if (alive) {
			super.draw(spriteBatch);
			loadingBarBackground.draw(spriteBatch, getX() - 5, getY() + getHeight() + 5, 35, 12);
	        loadingBar.draw(spriteBatch, getX() - 5, getY() + getHeight() + 5, progress * 35, 12);
		}
		else if (justDied){
			if (timeDied < 0.07f) spriteBatch.draw(explosion[0], getX(), getY());
			else if (timeDied < 0.14) spriteBatch.draw(explosion[1], getX(), getY());
			else if (timeDied < 0.21) spriteBatch.draw(explosion[2], getX(), getY());
			else if (timeDied > 0.29) justDied = false;
			timeDied += Gdx.graphics.getDeltaTime();
		}
	}


	protected void update(float deltaTime) {		
		collisionX = false;
		collisionY = false;
		//CALCULATE INERTIA
		if (body.getLinearVelocity().isZero(speed*speed/5)) inertiaNow = Math.max(inertiaNow - 5/30, inertiaInit);
		else if (timeFromCollision > threshold*1.5f) inertiaNow = Math.min(inertiaMax, inertiaNow + deltaTime*2f);
		
		//CALCULATE VELOCITY AND COLLISIONS FOR AI
		if(usingAI){
			if (body.getLinearVelocity().x < 0) {
				collisionX = collidesLeft(getX() + velocity.x * deltaTime,
						getY());

			} else if (body.getLinearVelocity().x > 0) {
				//Top Right
				collisionX = collidesRight(getX() + velocity.x * deltaTime,
						getY());
			}
			if (collisionX) {
				if (body.getLinearVelocity().x < 0) body.setTransform(body.getPosition().x + 1, body.getPosition().y, body.getAngle()); 
				else body.setTransform(body.getPosition().x - 1, body.getPosition().y, body.getAngle());
				body.setLinearVelocity(0, body.getLinearVelocity().y);
			}
			float valX = (!collisionX) ? getX() + body.getLinearVelocity().x * deltaTime : getX();
			if (body.getLinearVelocity().y < 0) {
				//Bottom left
				collisionY = collidesBottom(valX, getY() + body.getLinearVelocity().y
						* deltaTime);

			} else if (body.getLinearVelocity().y > 0) {
				//Top Left
				collisionY = collidesTop(valX, getY() + body.getLinearVelocity().y
						* deltaTime);
			}
			if (collisionY) {
				if (body.getLinearVelocity().y < 0) body.setTransform(body.getPosition().x, body.getPosition().y + 1, body.getAngle()); 
				else body.setTransform(body.getPosition().x, body.getPosition().y - 1, body.getAngle());
				body.setLinearVelocity(body.getLinearVelocity().x, 0);
			}
			if (body.getLinearVelocity().isZero(0.002f))
				stopMoving = true;
		}
		
		//CALCULATE VELOCITY AND COLLISIONS FOR MANUAL MOVING
		else{
			if (!stopMoving && timeFromCollision > threshold) {
				if (!movingWithPad){
					xDist = aimX - getX();
					yDist = aimY - getY();
					velocity.x = (xDist >= 0) ? speed * Math.min(1f, Math.abs(xDist/yDist)) : -speed * Math.min(1f, Math.abs(xDist/yDist));
					if ((aimX + 3 >= getX()) && (aimX - spriteW <= getX()))
						velocity.x = 0;
					
					velocity.y = (yDist >= 0) ? speed * Math.min(1f, Math.abs(yDist/xDist)) : -speed * Math.min(1f, Math.abs(yDist/xDist));
					if ((aimY + 3 >= getY()) && (aimY - spriteH <= getY()))
						velocity.y = 0;
				}
				else {
					velocity.x = game.controller.getKnobPercentX() * speed;
					velocity.y = game.controller.getKnobPercentY() * speed;
				}
			}
			else if (collided){
				velocity = body.getLinearVelocity();
				if (timeFromCollision > threshold) {
					collided = false;
					body.setAngularVelocity(0);
				}
			}
			else if (!(velocity = body.getLinearVelocity()).isZero()){
				checkCollision = true;
			}
			else checkCollision = false;
			
			if((!stopMoving && timeFromCollision > threshold) || collided || checkCollision){
				if (velocity.x < 0) {
					collisionX = collidesLeft(getX() + velocity.x * deltaTime,
							getY());
	
				} else if (velocity.x > 0) {
					//Top Right
					collisionX = collidesRight(getX() + velocity.x * deltaTime,
							getY());
				}
				if (collisionX) {
					velocity.x = 0;
				}
				float valX = (!collisionX) ? getX() + velocity.x * deltaTime : getX();
				//setY(getY() + velocity.y * deltaTime);
				if (velocity.y < 0) {
					//Bottom left
					collisionY = collidesBottom(valX, getY() + velocity.y
							* deltaTime);
	
				} else if (velocity.y > 0) {
					//Top Left
					collisionY = collidesTop(valX, getY() + velocity.y
							* deltaTime);
				}
				if (collisionY) {
					velocity.y = 0;
				}
				body.setLinearVelocity(velocity);
				if (velocity.isZero())
					if (!movingWithPad) stopMoving = true;
			}
		}
		
		setPosition(body.getPosition().x - spriteW/2, body.getPosition().y - spriteH/2);
		timeFromCollision += deltaTime;		
	}
	
	
	private boolean isCellBlocked(float x, float y){
		Cell cell = CoordConverter.getCell(collisionLayer, x, y);
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}
	
	
	private boolean collidesRight(float x, float y) {
        for(float step = 0; step < spriteH+2; step += spriteH/2)
                if(isCellBlocked(x + spriteW, y + step))
                        return true;
        return false;
	}
	 
	private boolean collidesLeft(float x, float y) {
        for(float step = 0; step < spriteH+2; step += spriteH/2)
                if(isCellBlocked(x, y + step))
                        return true;
        return false;
	}
	 
	private boolean collidesTop(float x, float y) {
        for(float step = 0; step < spriteW+2; step += spriteW/2)
                if(isCellBlocked(x + step, y + spriteH))
                        return true;
        return false;
	}
	 
	private boolean collidesBottom(float x, float y) {
        for(float step = 0; step < spriteW+2; step += spriteW/2)
                if(isCellBlocked(x + step, y))
                        return true;
        return false;
	}
}
/*
 * float speedNow = body.getLinearVelocity().len();
recentSpeed = 0.1 * speedNow + 0.9 * recentSpeed;
if ( recentSpeed < someThreshold )
    ... do something ...
/*
 * float speedNow = body.getLinearVelocity().len();
recentSpeed = 0.1 * speedNow + 0.9 * recentSpeed;
if ( recentSpeed < someThreshold )
    ... do something ...
    ----------------------
 */


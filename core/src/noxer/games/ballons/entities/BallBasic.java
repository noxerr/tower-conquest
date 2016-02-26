package noxer.games.ballons.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class BallBasic extends Ball{
	
	public BallBasic(Sprite sprite, TiledMapTileLayer collisionLayer, World world, NinePatchDrawable patchBack, NinePatchDrawable patchHP,
			TextureRegion[] explosion) {
		super(sprite, collisionLayer, world, patchBack, patchHP, explosion);
		speed = 50;
		life = 100;
		maxLife = 100;
		damage = 25;
		shield = 10;
		threshold = 0.8f;//tiempo para volverse a mover
		progress = 1f;
		inertiaInit = 0.15f;
		inertiaNow = inertiaInit;
	}
	
	
	public void initBody(World world, int playerNum) {
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0.3f;
        bodyDef.position.set(getX() + getWidth()/2, getY() + getHeight()/2);
        body = world.createBody(bodyDef);
        body.setUserData(this);
        //((Sprite)body.getUserData()).setPosition(body.getPosition().x,body.getPosition().y);
        
        //PolygonShape shape = new PolygonShape();
        CircleShape shape = new CircleShape();
        shape.setRadius(getWidth()/2);
        //shape.setAsBox(player.getWidth()/2 / 1, player.getHeight()/2 / 1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.1f;
        fixtureDef.restitution = 0.6f;
        fixtureDef.friction = 0.5f;
        if (playerNum == 0){
        	fixtureDef.filter.categoryBits = PLAYER1;
        	fixtureDef.filter.maskBits = PLAYER2;
        }
        else {
        	fixtureDef.filter.categoryBits = PLAYER2;
        	fixtureDef.filter.maskBits = PLAYER1;
        }
        //fixtureDef.isSensor = true; --> use it on towers not to react but detect collision
        body.createFixture(fixtureDef);
        shape.dispose();
	}

	@Override
    public void draw(Batch batch) {
		super.draw(batch);
    }

}

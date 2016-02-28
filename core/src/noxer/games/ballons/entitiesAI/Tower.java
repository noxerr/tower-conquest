package noxer.games.ballons.entitiesAI;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class Tower{
	
	private Body body;
	private int posX, posY, width, height;
	
	public Tower(NinePatchDrawable patchBack, NinePatchDrawable patchHP, int posX, int posY,
			int width, int height){
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
	}
	
	
	public void initBody(World world, int playerNum) {
		BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(posX + width/2, posY + height/2);
        body = world.createBody(bodyDef);
        body.setUserData(this);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        body.setTransform(body.getPosition(), (float) (Math.PI/2));

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



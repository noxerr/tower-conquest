package noxer.games.ballons.listeners;

import noxer.games.ballons.entities.Ball;
import noxer.games.ballons.entities.BallBasic;
import noxer.games.ballons.screens.testGame;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContListener implements com.badlogic.gdx.physics.box2d.ContactListener{
	private testGame game;
	
	public ContListener(testGame game){
		this.game = game;
	}
	
	@Override
    public void beginContact(Contact contact) {
    	//contact.setRestitution(15);
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    	((Ball)contact.getFixtureA().getBody().getUserData()).stopMoving = true;
    	((Ball)contact.getFixtureB().getBody().getUserData()).stopMoving = true;
    	
    	((BallBasic)contact.getFixtureA().getBody().getUserData()).timeFromCollision = 0;
    	((BallBasic)contact.getFixtureB().getBody().getUserData()).timeFromCollision = 0;
    	//if balls.equalTeam --> contact.setEnabled(false);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    	BallBasic ball1, ball2;
    	ball1 = (BallBasic)contact.getFixtureA().getBody().getUserData();
    	ball2 = (BallBasic)contact.getFixtureB().getBody().getUserData();
    	float damage1, damage2;
    	damage1 = (ball1.damage - ball2.shield) * ball1.inertiaNow;
    	ball1.inertiaNow = ball1.inertiaInit;
    	damage2 = (ball2.damage - ball1.shield) * ball2.inertiaNow;
    	ball2.inertiaNow = ball2.inertiaInit;
    	ball1.life -= damage2;
    	ball2.life -= damage1;
    	if((damage1 = ball1.life / ball1.maxLife) < 0) {
    		game.bodiesToDestroy.add(contact.getFixtureA().getBody());
    		ball1.alive = false;
    		ball1.justDied = true;
    		ball1.progress = 0;
    		game.ply.ballsAlive.removeValue(ball1, true);//TODO REMOVE FROM COMPUTER TOO
    	}
    	else if (damage1 > 1) ball1.progress = 1;
    	else ball1.progress = damage1;

    	if((damage2 = ball2.life / ball2.maxLife) < 0) {
    		game.bodiesToDestroy.add(contact.getFixtureB().getBody());
    		ball2.alive = false;
    		ball2.justDied = true;
    		ball2.progress = 0;
    		game.ply.ballsAlive.removeValue(ball2, true);
    	}
    	else if (damage2 > 1) ball2.progress = 1;
    	else ball2.progress = damage2;
    	
    	ball1.collided = true; ball2.collided = true;
    }
}

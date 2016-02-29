package com.noxer.tower.listeners;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.noxer.tower.screens.testGame;

public class InpListener extends InputAdapter{
	testGame game;
	private float timeElapsed;
	private boolean dragged;
	
	public InpListener(testGame game){
		this.game = game;
		dragged = false;
	}
	
	/*@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Input.Keys.A:
			game.body2.setLinearVelocity(-30f, 15f);
			break;
		case Input.Keys.W:
			game.body2.setLinearVelocity(15f, 30f);
			break;
		case Input.Keys.D:
			game.body2.setLinearVelocity(30f, -15f);
			break;
		case Input.Keys.S:
			game.body2.setLinearVelocity(-15f, -30f);
			break;
		}
		return true;
	}*/


	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Input.Keys.LEFT:
			game.camera.translate(-32,0);
			break;
		case Input.Keys.RIGHT:
			game.camera.translate(32,0);
			break;
		case Input.Keys.UP:
			game.camera.translate(0,32);
			break;
		case Input.Keys.DOWN:
			game.camera.translate(0,-32);
			break;
		case Input.Keys.NUM_1:
			game.map.getLayers().get(0).setVisible(!game.map.getLayers().get(0).isVisible());
			break;
		case Input.Keys.NUM_2:
			game.map.getLayers().get(1).setVisible(!game.map.getLayers().get(1).isVisible());
			break;
		case Input.Keys.NUM_3:
			game.camera.zoom *= 2;
			break;
		case Input.Keys.NUM_4:
			game.camera.zoom /= 2;
			break;
		}
		return true;
	}
	
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (!game.touchingPad){
			timeElapsed = 0;
			if (pointer == 0) game.last_touch_down.set( screenX, screenY, 0);
		}
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!game.touchingPad && !dragged){
			game.touchPos.set(screenX, screenY, 0); //touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			game.camera.unproject(game.touchPos);
			if (game.indexBolaControlada >= 0)
				game.ownBalls[game.indexBolaControlada].setAimCoords(game.touchPos.x, game.touchPos.y);
		}
		/*USE IT TO TARGET ENEMYS: WHEN TOUCHING JOYSTIC AI = FALSE, IF STOP AI = FALSE; IF COLLISION AI = FALSE, IF CLICK OUTSIDE AI FALSE 
		 * final Arrive<Vector2> seek = new Arrive<Vector2>(((BallBasicAI)balls[0].body.getUserData()), 
        		new Location(400,280)).setTimeToTarget(0.001f)
        		.setArrivalTolerance(2f)
        		.setDecelerationRadius(10f);
        ((BallBasicAI)balls[0].body.getUserData()).setBehavior(seek);
        balls[0].usingAI = true;
		 */
		dragged = false;
		return true;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (timeElapsed < 0.15f)timeElapsed += Gdx.graphics.getDeltaTime();
		else if (!game.touchingPad && pointer == 0) {
			moveCamera(screenX, screenY);     
			dragged = true;
		}
        return false;
	}

	private void moveCamera( int touch_x, int touch_y ) {
        Vector3 new_position = getNewCameraPosition( touch_x, touch_y );
        if( !cameraOutOfLimit( new_position ) )
        	game.camera.translate( new_position.sub(game.camera.position ) );
        game.last_touch_down.set( touch_x, touch_y, 0);
    }

    private Vector3 getNewCameraPosition( int x, int y ) {
        Vector3 new_position = game.last_touch_down;
        new_position.sub(x, y, 0);
        new_position.y = -new_position.y;
        new_position.add( game.camera.position );
        return new_position;
    }

    private boolean cameraOutOfLimit( Vector3 position ) {
          return false;
    }
}

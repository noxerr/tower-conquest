package com.noxer.tower.listeners;


import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.noxer.tower.screens.testGame;

public class GestListener extends GestureAdapter{
	testGame game;
	
	public GestListener(testGame game){
		this.game = game;
	}//pan = arrastrar
	
	@Override
	public boolean zoom(float initialDistance, float distance) {
		if (!game.touchingPad) zoomCamera((float) ((initialDistance - distance) * 0.001));
		return true;
	}
	
	public void zoomCamera(float actualDist){
		game.camera.zoom += (actualDist - game.oldDist);
		game.oldDist = actualDist;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		game.oldDist = 0;
		return false;
	}
}

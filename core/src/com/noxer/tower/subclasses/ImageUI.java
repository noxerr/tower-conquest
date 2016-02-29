package com.noxer.tower.subclasses;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.noxer.tower.screens.testGame;

public class ImageUI extends ImageButton{
	private Sprite ball;
	private boolean draw;
	public int position;
	
	public ImageUI(Skin skin, String styleName, TextureRegion ball, boolean draw, final int position, 
			final testGame game){
		super(skin.get(styleName, ImageButtonStyle.class));
		//padRight(20);
		getStyle().imageUp.setMinHeight(60);
		getStyle().imageDown.setMinHeight(60);
		getStyle().imageUp.setMinWidth(60);
		getStyle().imageDown.setMinWidth(60);
		//this.
		addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons){
                //System.out.println("Touched: " + event.getListenerActor().getX());
                //setVisible(false);
            	game.indexBolaControlada = position;
                return true;
            }
        });
		this.draw = draw;
		this.ball = new Sprite(ball);
		this.position = position;
		this.ball.setSize(40, 40);
		this.ball.setAlpha(0.8f);
	}
	
	/*@Override
	public Actor hit(float x, float y, boolean touchable) {
		//System.out.println("hitted");
		return super.hit(x, y, touchable);
	}*/
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if (draw) {
			ball.setPosition(getX() + 60/2 - ball.getWidth()/2, getY() + 60/2 - ball.getHeight()/2);
			ball.draw(batch);
		}
	}
	
}

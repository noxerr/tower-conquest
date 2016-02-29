package com.noxer.tower.subclasses;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

public class Controller extends Touchpad{
	private static Skin touchpadSkin;

	public Controller(float deadzoneRadius, int style, TextureAtlas textureAtlas) {
		super(deadzoneRadius, getStyle(style, textureAtlas));
		//setBounds(30, 45, Gdx.graphics.getWidth()/8, Gdx.graphics.getWidth()/8); //it was 90
		setBounds(30, 35, 90, 90);
	}
	
	public void Dispose(){
		touchpadSkin.dispose();
	}
	

	private static TouchpadStyle getStyle(int style, TextureAtlas textureAtlas) {
		touchpadSkin = new Skin();
		switch(style){
		case 0: 
			touchpadSkin.add("touchBackground", textureAtlas.createSprite("rojoUI")); 
		    touchpadSkin.add("touchKnob", textureAtlas.createSprite("KnobRojo")); 
		    break;
		case 1: 
			touchpadSkin.add("touchBackground", textureAtlas.createSprite("azulUI")); 
		    touchpadSkin.add("touchKnob", textureAtlas.createSprite("KnobAzul")); 
		    break;
		default: break;
		}

	    TouchpadStyle touchpadStyle = new TouchpadStyle(); 
	    touchpadStyle.background = touchpadSkin.getDrawable("touchBackground"); 
	    touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
	    //touchpadStyle.knob.setMinHeight(Gdx.graphics.getWidth()/16); //it was 40
	    //touchpadStyle.knob.setMinWidth(Gdx.graphics.getWidth()/16);
	    touchpadStyle.knob.setMinHeight(45);
	    touchpadStyle.knob.setMinWidth(45);
	    return touchpadStyle; 
	}

}

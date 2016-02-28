package noxer.games.ballons.subclasses;

import noxer.games.ballons.TowerConquest;
import noxer.games.ballons.screens.Nivel;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ImageButtonId extends ImageButton{
	public int id;
	public ImageButtonId (Skin skin, String styleName, final int id, final TowerConquest game) {
		super(skin.get(styleName, ImageButtonStyle.class));
		this.id = id;
		padRight(15);
		addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//System.out.println("Nivel: " + 0);
				game.setScreen(new Nivel(game, id));//TODO GO BACK
			}
		});
	}
}

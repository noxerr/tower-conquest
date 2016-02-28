package noxer.games.ballons.screens;

import noxer.games.ballons.subclasses.ImageButtonId;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class StageSelector implements Screen {
	private MainMenuScreen game;
	private Stage stage;
	private Table table, container;
	private ImageButton backArrow;
	private ImageButton stages[];
	private TextureAtlas atlasStage;
	private Skin skinStage;
	private ScrollPane scrollPane;
	
	public StageSelector(MainMenuScreen game) {
		this.game = game;
	}

	@Override
	public void show() {//addActor lo añade al centro, add lo añade al medio 
		stage = new Stage(new StretchViewport(800, 400));
		Gdx.input.setInputProcessor(stage);
		atlasStage = new TextureAtlas("levelSelector/backgrounds.pack");
		skinStage = new Skin(Gdx.files.internal("levelSelector/backgrounds.json"), atlasStage);
		
		backArrow = new ImageButton(game.skin, "backArrow");
		table = new Table(skinStage);
		table.setBackground(new Image(game.background.getTexture()).getDrawable());
		table.setFillParent(true);
		container = new Table(skinStage);
		
		backArrow.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.game.setScreen(game);//TODO GO BACK
			}
		});
		
		//stage
		stage.addActor(table);
		stage.addActor(backArrow);
		backArrow.setPosition(10, 326);
		
		//scroll pane
		scrollPane = new ScrollPane(container);
		scrollPane.setFlickScroll(true);
		scrollPane.setBounds(100, 0, 600, 400);
		table.addActor(scrollPane);
		
		stages = new ImageButtonId[4];
		stages[0] = new ImageButtonId(skinStage, "hill", 0, game.game);
		stages[1] = new ImageButtonId(skinStage, "iceberg", 1, game.game);
		stages[2] = new ImageButtonId(skinStage, "volcano", 2, game.game);
		stages[3] = new ImageButtonId(skinStage, "field", 3, game.game);
		/*for (int i = 0; i < stages.length; i++){
			stages[i].padRight(15);
			stages[i].addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("Nivel: " + 0);
				game.game.setScreen(new Nivel(game.game, ((ImageButtonId)event.getTarget()).id));//TODO GO BACK
			}
			});
			//stages[i].getColor().mul(1, 1, 1, 0.65f);
		}*/
		container.add(stages);
		
//		table.add(backArrow).align(Align.topLeft).pad(10);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
		table.debugTable();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getCamera().update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}

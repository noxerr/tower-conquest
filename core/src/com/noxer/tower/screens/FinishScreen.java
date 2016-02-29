package com.noxer.tower.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.noxer.tower.TowerConquest;

public class FinishScreen implements Screen {

	final TowerConquest game;
	final FinishScreen mmScreen;
	private TextureAtlas atlas, titulos;
	protected Skin skin, skinTitulos;
	private Stage stage;
	private Table table;
	//private Label heading;
	private Image heading;
	//private TextButton buttonPlay, buttonExit;
	private TextButton play;
	protected Sprite background;
	//private List<String> list;
	//private ScrollPane scrollPane;
	private boolean ganado;
	
    public FinishScreen(final TowerConquest gam, boolean ganado) {
        game = gam;
        mmScreen = this;
        this.ganado = !ganado;
    }
    
	@Override
	public void show() {
		stage = new Stage(new StretchViewport(800, 480)); //CON STRETCH Y BACKGROUND EN LA TABLA SE VE BN
		Gdx.input.setInputProcessor(stage);
		//((OrthographicCamera)stage.getCamera()).setToOrtho(false, 800, 480);
		atlas = new TextureAtlas("skins/userInterface.pack");
		titulos = new TextureAtlas("skins/titulos.pack");
		skin = new Skin(Gdx.files.internal("skins/userInterface.json"), atlas);
		skinTitulos = new Skin(Gdx.files.internal("skins/titulos.json"), titulos);
		table = new Table(skin);
		table.setBounds(0, 0, 800, 480);
		
		//play = new TextButton("PLAY", skinTitulos, "mainMenuBlack");
		
		if (ganado) 
			play = new TextButton("You WON!\nClick to go Back", skinTitulos, "mainMenuBlack");
		
		else 
			play = new TextButton("You lost.\nClick to go Back", skinTitulos, "mainMenuBlack");
		 
		play.getStyle().font.setColor(0.7411f, 0.8f, 0.1137f, 1.0f);//fontColor.set(0.7411f, 0.8f, 0.1137f, 1.0f);//0.7411, "g": 0.8, "b": 0.1137
		
		play.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//game.setScreen(new testGame(game));
	            //dispose();
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		
		//heading = new Label("Tower Conquest", skin, "default");
		//heading.setFontScale(2);
		
		Drawable d = skinTitulos.getDrawable("towerconquest");
		d.setMinWidth(550);
		d.setMinHeight(160);
		heading = new Image(d);
		table.add(heading);
		table.getCell(heading).padBottom(30);
		//table.setBackground(new Image(new Texture("globe.png")).getDrawable());
		table.row();
		table.add(play);
		table.getCell(play).spaceBottom(15);
		//table.debug();
		//table.setOrigin(table.getWidth()/2, table.getHeight()/2);
		table.setFillParent(true);
		stage.addActor(table);
		
		background = new Sprite(new Texture("background.png"));
		background.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());//los get pillan el tamaño original del dispositivo
		
		/*list = new List<String>(skin);
		list.setItems(new String[]{"Uno", "dos", "tres"});
		scrollPane = new ScrollPane(list, skin);
		table.add("Select Level");
		table.add(scrollPane);*/

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
			background.draw(game.batch);
		game.batch.end();
        //table.debugTable();
		stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		//table.invalidateHierarchy();
		stage.getCamera().update();
		//background.setBounds(0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		atlas.dispose();
		skin.dispose();
		stage.dispose();
		background.getTexture().dispose();
		skinTitulos.dispose();
	}

}

/*private ScheduledExecutorService programador;
programador = Executors.newSingleThreadScheduledExecutor();
programador.schedule(accion, timepohastalaprimeraejecucion, TimeUnit.MILLISECONDS);
programador.schedule(accion, 2000, TimeUnit.MILLISECONDS);
 private Runnable accion = new Runnable() {
        @Override
        public void run() {}};
        ---------
        *Timer timer = new Timer();
        *TimerTask tas = new TimerTask(){
	        public void run(){
	        }
	    };
	    
	 	timer.schedule(tas, 2900);        */

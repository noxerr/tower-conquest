package noxer.games.ballons.screens;

import noxer.games.ballons.TowerConquest;
import noxer.games.ballons.data.Player;
import noxer.games.ballons.entities.Ball;
import noxer.games.ballons.entitiesAI.BallBasicAI;
import noxer.games.ballons.listeners.ContListener;
import noxer.games.ballons.listeners.GestListener;
import noxer.games.ballons.listeners.InpListener;
import noxer.games.ballons.maths.CoordConverter;
import noxer.games.ballons.subclasses.Controller;
import noxer.games.ballons.subclasses.ImageUI;
import noxer.games.ballons.subclasses.IsoMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class testGame implements Screen {

	//private boolean firstResize = true;
	public OrthographicCamera camera;
	final TowerConquest game;
	public TiledMap map; 
	IsoMap renderer;
	public Ball[] balls;
	public Vector3 touchPos, last_touch_down = new Vector3();
	public float oldDist = 0;
	TiledMapTileLayer lay1;
	World world;
	Box2DDebugRenderer debugRenderer;
	private NinePatchDrawable loadingBarBackground, loadingBar;
	private int[][] coords;
	public Player ply;
	Stage stage;
	public Controller controller;
	public boolean touchingPad;
	public Array<Body> bodiesToDestroy = new Array<Body>(false, 16);
	private TextureAtlas gameUI, entities;
	private Table container, table;
	private ScrollPane scrollPane;
	private Skin skin;
	
	public testGame(final TowerConquest game) {
		this.game = game;
		float w = Gdx.graphics.getWidth();
		
		//setting up camera and world
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false,w+w,0);
		map = new TmxMapLoader().load("maps/First.tmx");
		renderer = new IsoMap(map);
		touchPos = new Vector3();
		world = new World(new Vector2(0, 0),true);
		world.setContactListener(new ContListener(this));
		gameUI = new TextureAtlas(Gdx.files.internal("skins/gameUI.pack"));
		entities = new TextureAtlas(Gdx.files.internal("skins/entities.pack"));
		
		//setting up Converter Class
		lay1 = (TiledMapTileLayer) map.getLayers().get(1);
		float mapW = lay1.getWidth(), mapH = lay1.getHeight(), 
				tileW = lay1.getTileWidth(), tileH = lay1.getTileHeight();
		CoordConverter.CoordInit(mapW, mapH, tileW, tileH);
		
		setupBalls();
		coords = new int[2][ply.balls.size];
        debugRenderer = new Box2DDebugRenderer();
        
        setupActors();
        
        //AI PART//or evade        
        /*final Flee<Vector2> reachB = new Flee<Vector2>(((BallBasicAI)balls[0].body.getUserData()), 
        		((BallBasicAI)balls[1].body.getUserData()));
        ((BallBasicAI)balls[0].body.getUserData()).setBehavior(reachB);*/
        
        final Pursue<Vector2> seekSB = new Pursue<Vector2>(((BallBasicAI)balls[1].body.getUserData()), 
        		((BallBasicAI)balls[0].body.getUserData()));
        ((BallBasicAI)balls[1].body.getUserData()).setBehavior(seekSB);
        balls[1].usingAI = true;
        
      //setting up processors
  		InputMultiplexer inp = new InputMultiplexer();
  		inp.addProcessor(stage);
  		inp.addProcessor(new InpListener(this));
  		inp.addProcessor(new GestureDetector(new GestListener(this)));
  		Gdx.input.setInputProcessor(inp);
	}


	private void setupBalls() {
		//setting up balls
        NinePatch loadingBarBackgroundPatch = new NinePatch(gameUI.findRegion("lifeBack"), 2, 2, 2, 2);
        NinePatch loadingBarPatch = new NinePatch(gameUI.findRegion("lifeRed"), 2, 2, 2, 2);
        loadingBar = new NinePatchDrawable(loadingBarPatch);
        loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);
        
        TextureRegion[] regionsRed = new TextureRegion[3];
        TextureRegion[] regionsBlue = new TextureRegion[3];
		regionsRed[0] = entities.findRegion("explosionRed1");
		regionsRed[1] = entities.findRegion("explosionRed2");
		regionsRed[2] = entities.findRegion("explosionRed3");
		regionsBlue[0] = entities.findRegion("explosionBlue1");
		regionsBlue[1] = entities.findRegion("explosionBlue2");
		regionsBlue[2] = entities.findRegion("explosionBlue3");
		
		short casX = 12, casY = 8;
        balls = new Ball[3];
		balls[0] = new BallBasicAI(entities.createSprite("ballBasicRed"), lay1, world, loadingBarBackground, loadingBar,
				regionsRed);
		CoordConverter.PlacePlayer(casX, casY, balls[0]);
		casX = 7;
		casY = 17;
		balls[1] = new BallBasicAI(entities.createSprite("ballBasicBlue"), lay1, world, loadingBarBackground, loadingBar,
				regionsBlue);
		CoordConverter.PlacePlayer(casX, casY, balls[1]);
		
		casX = 14;
		casY = 12;
		balls[2] = new BallBasicAI(entities.createSprite("ballBasicRed"), lay1, world, loadingBarBackground, loadingBar,
				regionsRed);
		CoordConverter.PlacePlayer(casX, casY, balls[2]);
		
		//setting up Players
		//Start player1
		Ball[] a = new Ball[2];
		a[0] = balls[0];
		a[1] = balls[2];
		ply = new Player(a);
		initBodys(ply.balls, 5);
	}


	private void setupActors() {
		//stage = new Stage(new FillViewport(800, 480));
		stage = new Stage(new ExtendViewport(800, 480));
		//stage = new Stage();
		
		//setting up actors
		touchingPad = false;
		controller = new Controller(10,0, gameUI);
		controller.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				touchingPad = true;
				((Ball)balls[2].body.getUserData()).movingWithPad = true;
				//TODO WITH THE SELECTED BALL
		        return true;
		    }

		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		    	touchingPad = false;
		    	((Ball)balls[2].body.getUserData()).movingWithPad = false;
		    	((Ball)balls[2].body.getUserData()).stopMoving = true;
		    }
		});
		
		stage.addActor(controller);
		skin = new Skin(gameUI);
		container = new Table(skin);
		table = new Table();
		stage.addActor(container);
		
		scrollPane = new ScrollPane(table);
		scrollPane.setFlickScroll(true);
		ImageUI[] image = new ImageUI[12];
		for (int i = 0; i < 12; i++){
			if (i < ply.balls.size) 
				image[i] = new ImageUI(gameUI.findRegion("dissabledbutton"), entities.findRegion("ballBasicRed"), true, i);
			else image[i] = new ImageUI(gameUI.findRegion("dissabledbutton"), entities.findRegion("ballBasicRed"), false, i);
//			table.add(image[i]).minSize(Gdx.graphics.getWidth()/16, Gdx.graphics.getWidth()/16).spaceRight(20);
			table.add(image[i]).minSize(60, 60).spaceRight(20);
		}	
//		container.setBounds(Gdx.graphics.getWidth()/6.5f, Gdx.graphics.getWidth()/32, Gdx.graphics.getWidth() - 200, Gdx.graphics.getWidth()/16);//was 150, 25, 600, 
		container.setBounds(150, 10, 800 - 200, 64);
		container.bottom();
		container.add(scrollPane).padLeft(10).padRight(10);
		container.getColor().mul(1, 1, 1, 0.65f);
		
	}


	private void initBodys(Array<Ball> player1, int level) {
		for (int i = 0; i < player1.size; i++){
	        player1.get(i).initBody(world, 0);
	        player1.get(i).setGame(this);
		}	
		balls[1].initBody(world, 1);
	}


	@Override
	public void render(float delta) {
		//delta = Math.min(0.06f, delta);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getRenderingCoords();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		world.step(1f/30f, 6, 2);
		for (Body body : bodiesToDestroy){
			//world.destroyBody(body);
			//body.getBody().destroyFixture(body);
			body.setActive(false);
			bodiesToDestroy.removeValue(body, true);
		}
		if (touchingPad) ((Ball)balls[2].body.getUserData()).stopMoving = false; //TODO WITH SELECTED BALL
		//game.batch.enableBlending();
		camera.update();
		renderer.setView(camera);
		//renderer.render();
		camera.rotate(-0.25f, 0, 0, 1);
		renderer.getBatch().begin();
			renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(0));
			renderer.customRender(lay1, coords, ply.balls);
			renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(2));
			balls[1].draw(renderer.getBatch());
			//player.setColor(0, 0, 1, 1);			
			renderer.getBatch().setProjectionMatrix(camera.projection);
			game.font.draw(renderer.getBatch(), "fps: " + Gdx.graphics.getFramesPerSecond(), 280 *camera.zoom, 260*camera.zoom);
		renderer.getBatch().end();
		debugRenderer.render(world, camera.combined);
		container.debugTable();
		stage.getViewport().apply();
		stage.draw();
		stage.act(delta);
	}
	
	private void getRenderingCoords() {
		for (int i = 0; i < ply.balls.size; i++){
			CoordConverter.getCellCoords(ply.balls.get(i).getX()+balls[0].getWidth()/2, ply.balls.get(i).getY(), coords, i);
		}
	}


	public void resize (int width, int height) { 
		//((ScalingViewport)stage.getViewport()).setScaling(Scaling.fit);
		stage.getViewport().update(width, height,true);
		//container.invalidateHierarchy();
		//stage.getCamera().update();
		camera.viewportHeight = height;
		camera.viewportWidth = width;
	}

	public void pause () { 
	}

    public void resume () {
    }

    public void dispose () { 
    	game.batch.dispose();
    	map.dispose();
    	renderer.dispose();
    	balls[0].getTexture().dispose();//dispose all textures
    	controller.Dispose();
    	stage.dispose();
    	world.dispose();
    	gameUI.dispose();
    	entities.dispose();
    	debugRenderer.dispose();
    }

	@Override
	public void show() {
	}


	@Override
	public void hide() {
		dispose();		
	}
	
}



/*
 * 
 * Matrix4 uiMatrix = cam.combined.cpy();
uiMatrix.setToOrtho2D(0, 0, WIDTH, HEIGHT);
batch.setProjectionMatrix(uiMatrix);
batch.begin();
 * 
 * 
//Access the sprite
//((Sprite)body.getUserData()).setPosition(body.getPosition().x,body.getPosition().y);

DistanceJointDef distJoint = new DistanceJointDef();
distJoint.bodyA = body;
distJoint.bodyB = body2;
distJoint.dampingRatio = 0.5f;
distJoint.frequencyHz = 4;
distJoint.length = 230;
//world.createJoint(distJoint);

RopeJointDef ropeJoint = new RopeJointDef();
ropeJoint.bodyA = body;
ropeJoint.bodyB = body2;
ropeJoint.maxLength = balls[1].getWidth()*2;
ropeJoint.localAnchorA.set(0, 0);
ropeJoint.localAnchorB.set(0, 0);
//world.createJoint(ropeJoint);
 * 
 * Pixmap pixmap = new Pixmap( 64, 64, Format.RGBA8888 );
pixmap.setColor( 0, 1, 0, 0.75f );
pixmap.fillCircle( 32, 32, 32 );
Texture pixmaptex = new Texture( pixmap );
pixmap.dispose();
 * 
 * */



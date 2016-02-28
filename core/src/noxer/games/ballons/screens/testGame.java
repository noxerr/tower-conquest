package noxer.games.ballons.screens;

import noxer.games.ballons.TowerConquest;
import noxer.games.ballons.data.Player;
import noxer.games.ballons.entities.Ball;
import noxer.games.ballons.listeners.ContListener;
import noxer.games.ballons.maths.CoordConverter;
import noxer.games.ballons.subclasses.Controller;
import noxer.games.ballons.subclasses.ImageUI;
import noxer.games.ballons.subclasses.IsoMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class testGame implements Screen {

	//private boolean firstResize = true;
	public OrthographicCamera camera;
	final TowerConquest game;
	public TiledMap map; 
	protected IsoMap renderer;
	
	//public Ball[] balls;
	public Ball[] ownBalls, enemyBalls;
	
	public Vector3 touchPos, last_touch_down = new Vector3();
	public float oldDist = 0;
	TiledMapTileLayer lay1;
	protected World world;
	
	Box2DDebugRenderer debugRenderer;
	private float elapsedSinceAnimation = 0;
	
	protected int[][] coords, coordsCPU;
	public Player ply, cpu;
	
	Stage stage;
	public Controller controller;
	public boolean touchingPad;
	public Array<Body> bodiesToDestroy = new Array<Body>(false, 16);
	protected TextureAtlas gameUI, entities;
	private Table container, table;
	private ScrollPane scrollPane;
	private Skin skin;
	
	public testGame(final TowerConquest game) {
		this.game = game;
		float w = Gdx.graphics.getWidth();
		
		touchPos = new Vector3();
		world = new World(new Vector2(0, 0),true);
		world.setContactListener(new ContListener(this));
		//setting up camera and world
	    camera = new OrthographicCamera();
	    camera.setToOrtho(false,w+w,0);
        debugRenderer = new Box2DDebugRenderer();

        //AI PART//or evade        
        /*final Flee<Vector2> reachB = new Flee<Vector2>(((BallBasicAI)balls[0].body.getUserData()), 
        		((BallBasicAI)balls[1].body.getUserData()));
        ((BallBasicAI)balls[0].body.getUserData()).setBehavior(reachB);*/
        
	}


	protected void setupActors() {
		stage = new Stage(new StretchViewport(800, 480));
		
		//setting up actors
		touchingPad = false;
		controller = new Controller(10,0, gameUI);
		controller.addListener(new InputListener(){
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				touchingPad = true;
				((Ball)ownBalls[2].body.getUserData()).movingWithPad = true;
				//TODO WITH THE SELECTED BALL
		        return true;
		    }

		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
		    	touchingPad = false;
		    	((Ball)ownBalls[2].body.getUserData()).movingWithPad = false;
		    	((Ball)ownBalls[2].body.getUserData()).stopMoving = true;
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


	protected void initBodys(Array<Ball> player1, int level) {
		/*for (int i = 0; i < player1.size; i++){
	        player1.get(i).initBody(world, 0);
	        player1.get(i).setGame(this);
		}	
		balls[1].initBody(world, 1);*/
	}


	@Override
	public void render(float delta) {
		delta = Math.min(0.06f, delta);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		getRenderingCoords();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		world.step(1f/30f, 6, 2);
		for (Body body : bodiesToDestroy){
			//body.getBody().destroyFixture(body);
			body.setActive(false);
			world.destroyBody(body);
			bodiesToDestroy.removeValue(body, true);
		}
		
		//ANIMACIONES
		elapsedSinceAnimation += Gdx.graphics.getDeltaTime();
        if(elapsedSinceAnimation > 0.8f){
            updateWaterAnimations();
            elapsedSinceAnimation = 0.0f;
        }
		
		
		if (touchingPad) ((Ball)ownBalls[2].body.getUserData()).stopMoving = false; //TODO WITH SELECTED BALL
		//game.batch.enableBlending();
		camera.update();
		renderer.setView(camera);
		//renderer.render();
		//camera.rotate(-0.25f, 0, 0, 1);
		renderer.getBatch().begin();
			renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(0));
			renderer.customRender(lay1, coords, ply.balls, coordsCPU, cpu.balls);
			renderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(2));
			ply.tower.patchHP.draw(renderer.getBatch(), ply.tower.posX - ply.tower.width/2, 
					ply.tower.posY + ply.tower.height*4/5, ply.tower.width, 16);
			cpu.tower.patchHP.draw(renderer.getBatch(), cpu.tower.posX - cpu.tower.width/2, 
					cpu.tower.posY + cpu.tower.height*4/5, cpu.tower.width, 16);
			//balls[1].draw(renderer.getBatch());
			//player.setColor(0, 0, 1, 1);			
			renderer.getBatch().setProjectionMatrix(camera.projection);
			game.font.draw(renderer.getBatch(), "fps: " + Gdx.graphics.getFramesPerSecond(), 280 *camera.zoom, 260*camera.zoom);
		renderer.getBatch().end();
		debugRenderer.render(world, camera.combined);
		//container.debugTable();
		stage.getViewport().apply();
		stage.draw();
		stage.act(delta);
	}
	
	//ACTUALIZA LAS COORDENADAS PARA SABER DONDE PINTAR
	private void getRenderingCoords() {
		for (int i = 0; i < ply.balls.size; i++){
			CoordConverter.getCellCoords(ply.balls.get(i).getX()+ply.balls.get(i).getWidth()/2, ply.balls.get(i).getY(), coords, i);
		}
		for (int i = 0; i < cpu.balls.size; i++){
			CoordConverter.getCellCoords(cpu.balls.get(i).getX()+cpu.balls.get(i).getWidth()/2, cpu.balls.get(i).getY(), coordsCPU, i);
		}
	}
	
	abstract void updateWaterAnimations();


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
    	
    	//balls[1].getTexture().dispose();//dispose all textures
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



package noxer.games.ballons.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import noxer.games.ballons.TowerConquest;
import noxer.games.ballons.data.OpcionesPartida;
import noxer.games.ballons.data.Player;
import noxer.games.ballons.entities.Ball;
import noxer.games.ballons.entitiesAI.BallBasicAI;
import noxer.games.ballons.listeners.GestListener;
import noxer.games.ballons.listeners.InpListener;
import noxer.games.ballons.maths.CoordConverter;
import noxer.games.ballons.subclasses.IsoMap;

public class Nivel extends testGame{

	private NinePatchDrawable loadingBarBackground, loadingBar;
	ArrayList<TiledMapTileLayer.Cell> towerCellsInScene;
    Map<String,TiledMapTile> towerTiles;
    private OpcionesPartida ops;
    
	public Nivel(TowerConquest game, int nivel) {
		super(game);
		ops = new OpcionesPartida(nivel);
		map = ops.map;
		renderer = new IsoMap(map);
		
		gameUI = new TextureAtlas(Gdx.files.internal("skins/gameUI.pack"));
		entities = new TextureAtlas(Gdx.files.internal("skins/entities.pack"));
		
		//setting up Converter Class
		lay1 = (TiledMapTileLayer) map.getLayers().get(1);
		float mapW = lay1.getWidth(), mapH = lay1.getHeight(), 
				tileW = lay1.getTileWidth(), tileH = lay1.getTileHeight();
		CoordConverter.CoordInit(mapW, mapH, tileW, tileH); //iniciamos con las coordenadas del mapa
		
		//INIT BALLS
		setupBalls();
		coords = new int[2][ply.balls.size];
		
		super.setupActors();
		
		final Pursue<Vector2> seekSB = new Pursue<Vector2>(((BallBasicAI)balls[1]), 
        		((BallBasicAI)balls[0]));
        ((BallBasicAI)balls[1].body.getUserData()).setBehavior(seekSB);
        balls[1].usingAI = true;
        
        
      //setting up processors
  		InputMultiplexer inp = new InputMultiplexer();
  		inp.addProcessor(stage);
  		inp.addProcessor(new InpListener(this));
  		inp.addProcessor(new GestureDetector(new GestListener(this)));
  		Gdx.input.setInputProcessor(inp);
  		
  		animateTowers();
	}
	
	
	private void animateTowers() {
		//ANIMATED TOWERS
        // Get a reference to the tileset named "Sec"
        TiledMapTileSet tileset =  map.getTileSets().getTileSet("Sec");
 
 
        // Now we are going to loop through all of the tiles in the Water tileset
        // and get any TiledMapTile with the property "WaterFrame" set
        // We then store it in a map with the frame as the key and the Tile as the value
        towerTiles = new HashMap<String,TiledMapTile>();
        for(TiledMapTile tile:tileset){
             Object property = tile.getProperties().get("Tower1");
            if(property != null)
            	towerTiles.put((String)property,tile);
        }
 
        // Now we want to get a reference to every single cell ( Tile instance ) in the map
        towerCellsInScene = new ArrayList<TiledMapTileLayer.Cell>();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2);
        for(int x = 0; x < layer.getWidth();x++){
            for(int y = 0; y < layer.getHeight();y++){
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                if (cell != null) {
                	Object property = cell.getTile().getProperties().get("Tower1");
                	if(property != null) towerCellsInScene.add(cell);
                }
            }
        }		
	}
	
	
	@Override
	void updateWaterAnimations() {
		for(TiledMapTileLayer.Cell cell : towerCellsInScene){
            String property = (String) cell.getTile().getProperties().get("Tower1");
            Integer currentAnimationFrame = Integer.parseInt(property);
 
            currentAnimationFrame++;
            if(currentAnimationFrame > towerTiles.size())
                currentAnimationFrame = 1;
 
            cell.setTile(towerTiles.get(currentAnimationFrame.toString()));
        }
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
		
		ownBalls = new Ball[ops.numBolasMias];
		enemyBalls = new Ball[ops.numBolasEnemigo];
		
		for (int i = 0; i < ownBalls.length; i++){
			//ownBalls[i] = 
		}
		
		
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


	

}

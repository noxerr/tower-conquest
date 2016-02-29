package com.noxer.tower.screens;

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
import com.noxer.tower.TowerConquest;
import com.noxer.tower.data.OpcionesPartida;
import com.noxer.tower.data.Player;
import com.noxer.tower.entities.Ball;
import com.noxer.tower.entitiesAI.BallBasicAI;
import com.noxer.tower.entitiesAI.Tower;
import com.noxer.tower.listeners.GestListener;
import com.noxer.tower.listeners.InpListener;
import com.noxer.tower.maths.CoordConverter;
import com.noxer.tower.subclasses.IsoMap;


public class Nivel extends testGame{

	private NinePatchDrawable loadingBarBackground, loadingBar, ldBackBlue, ldBarBlue;
	ArrayList<TiledMapTileLayer.Cell> towerCellsInScene;
    Map<String,TiledMapTile> towerTiles;
    
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
		coordsCPU = new int[2][cpu.balls.size];
		
		
		final Pursue<Vector2> seekSB = new Pursue<Vector2>(((BallBasicAI)enemyBalls[0]), 
        		((BallBasicAI)ownBalls[0]));
        ((BallBasicAI)enemyBalls[0].body.getUserData()).setBehavior(seekSB);
        enemyBalls[0].usingAI = true;
        
        final Pursue<Vector2> seekSB2 = new Pursue<Vector2>(((BallBasicAI)enemyBalls[1]), 
        		((BallBasicAI)ownBalls[1]));
        ((BallBasicAI)enemyBalls[1].body.getUserData()).setBehavior(seekSB2);
        enemyBalls[1].usingAI = true;
        enemyBalls[1].maxLife = 70;
        enemyBalls[1].life = 70;
        
        final Pursue<Vector2> seekSB3 = new Pursue<Vector2>(((BallBasicAI)enemyBalls[2]), 
        		((BallBasicAI)ownBalls[2]));
        ((BallBasicAI)enemyBalls[2].body.getUserData()).setBehavior(seekSB3);
        enemyBalls[2].usingAI = true;
        enemyBalls[2].maxLife = 70;
        enemyBalls[2].life = 70;
        
        super.setupActors();
        
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
        NinePatch ldBack = new NinePatch(gameUI.findRegion("lifeBlue"), 2, 2, 2, 2);
        NinePatch ldBar = new NinePatch(gameUI.findRegion("lifeBlue"), 2, 2, 2, 2);
        loadingBar = new NinePatchDrawable(loadingBarPatch);
        loadingBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);
        ldBarBlue = new NinePatchDrawable(ldBar);
        ldBackBlue = new NinePatchDrawable(ldBack);
        
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
			switch (ops.unidadesMias[i]) { //segun el tipo de unidad k sea la 0, creamos 1 tipo o otro, y asi
			case 0:
				ownBalls[i] = new BallBasicAI(entities.createSprite("ballBasicRed"), 
						lay1, world, loadingBarBackground, loadingBar, regionsRed);
				break;
				//PONER OTROS CASOS
			default:
				ownBalls[i] = new BallBasicAI(entities.createSprite("ballBasicRed"), 
						lay1, world, loadingBarBackground, loadingBar, regionsRed);
				break;
			}
			CoordConverter.PlacePlayer(ops.posicionesMias[0][i], ops.posicionesMias[1][i], ownBalls[i]);
			ownBalls[i].initBody(world, 0);
			ownBalls[i].setGame(this);
		}
		
		
		for (int i = 0; i < enemyBalls.length; i++){
			switch (ops.unidadesEnemigo[i]) { //segun el tipo de unidad k sea la 0, creamos 1 tipo o otro, y asi
			case 0:
				enemyBalls[i] = new BallBasicAI(entities.createSprite("ballBasicBlue"), 
						lay1, world, loadingBarBackground, loadingBar, regionsBlue);
				break;
				//PONER OTROS CASOS
			default:
				enemyBalls[i] = new BallBasicAI(entities.createSprite("ballBasicBlue"), 
						lay1, world, loadingBarBackground, loadingBar, regionsBlue);
				break;
			}
			CoordConverter.PlacePlayer(ops.posicionesEnemigas[0][i], ops.posicionesEnemigas[1][i], enemyBalls[i]);
			enemyBalls[i].initBody(world, 1);
			enemyBalls[i].setGame(this);
		}
		
		ply = new Player(ownBalls);
		cpu = new Player(enemyBalls);
		
		updateOps();
		
		Tower mia = new Tower(loadingBarBackground, loadingBar, ops.xTorreMia, ops.yTorreMia, 64, ops.hTorreMia);
		Tower enemigo = new Tower(ldBackBlue, ldBarBlue, ops.xTorreEn, ops.yTorreEn, 64, ops.hTorreEn);
		ply.addTower(mia);
		cpu.addTower(enemigo);
		mia.initBody(world, 0);
		enemigo.initBody(world, 1);
	}


	private void updateOps() {
		float xAux, yAux;
		xAux = CoordConverter.getCellX(ops.xTorreMia, ops.yTorreMia);
		yAux = CoordConverter.getCellY(ops.xTorreMia, ops.yTorreMia);
		ops.xTorreMia = xAux;
		ops.yTorreMia = yAux;
		
		xAux = CoordConverter.getCellX(ops.xTorreEn, ops.yTorreEn);
		yAux = CoordConverter.getCellY(ops.xTorreEn, ops.yTorreEn);
		ops.xTorreEn = xAux;
		ops.yTorreEn = yAux;
	}


	

}

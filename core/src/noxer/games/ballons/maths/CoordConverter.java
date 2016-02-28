package noxer.games.ballons.maths;


import noxer.games.ballons.entities.Ball;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;


public class CoordConverter {
	private static float mapH, tileW, tileH, fixedX, fixedY;
	
	public static void CoordInit(float mapW, float mapH, float tileW, float tileH){
		CoordConverter.tileW = tileW;
		CoordConverter.tileH = tileH;
		fixedX = mapW * tileW / 2;
		fixedY = mapH * tileH / 2;
		CoordConverter.mapH = mapH-1;
	}

	public static void PlacePlayer(int tileX, int tileY, Ball player){
		float x = fixedX + ((tileX - tileY) * tileW / 2), y = fixedY - ((tileX + tileY) * tileH / 2);
		player.setPosition(x, y);
	}

	//USED BY BALL TO CHECK IF CELL IS BLOCKED
	public static Cell getCell(TiledMapTileLayer collisionLayer, float x, float y){
		Cell cell = collisionLayer.getCell(
				(int) ((tileW * fixedY - tileW * y - fixedX * tileH + tileH * x) / (tileW * tileH)+ 0.5), 
				(int) (mapH - ((tileW * fixedY - tileW * y + fixedX * tileH - tileH * x) / (tileW * tileH)-0.5)));
		return cell;
	}
	
	//en realidad guarda la celda y le pasas tu las coordenadas
	public static void getCellCoords(float x, float y, int[][] coords, int i){
		coords[0][i] = (int) ((tileW * fixedY - tileW * y - fixedX * tileH + tileH * x) / (tileW * tileH)+ 0.5);
		coords[1][i] = (int) (mapH - ((tileW * fixedY - tileW * y + fixedX * tileH - tileH * x) / (tileW * tileH)-0.5));
	}
	
	public static float getCellX(float x, float y){
		return fixedX + ((x - y) * tileW / 2);
	}
	
	public static float getCellY(float x, float y){
		return fixedY - ((x + y) * tileH / 2);
	}
}







package noxer.games.ballons.data;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class OpcionesPartida {

	public int numBolasMias, numBolasEnemigo, numEnemigos;
	public TiledMap map;
	public OpcionesPartida(int nivel) {
		switch (nivel) {
		case 0:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			break;
		case 1:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			break;
		default:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			break;
		}
	}
	
}

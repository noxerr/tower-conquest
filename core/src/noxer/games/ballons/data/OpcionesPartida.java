package noxer.games.ballons.data;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class OpcionesPartida {

	public int numBolasMias, numBolasEnemigo, numEnemigos, xTorreMia, yTorreMia, xTorreEn, yTorreEn;
	public TiledMap map;
	public int posicionesMias [][], posicionesEnemigas[][];
	public OpcionesPartida(int nivel) {
		switch (nivel) {
		case 0:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			posicionesMias = new int[2][3];
			posicionesEnemigas = new int[2][3];
			posicionesMias[0][0] = 14;
			posicionesMias[0][1] = 15;
			posicionesMias[0][2] = 16;
			
			posicionesMias[1][0] = 26;
			posicionesMias[1][1] = 26;
			posicionesMias[1][2] = 26;
			
			posicionesEnemigas[0][0] = 26;
			posicionesEnemigas[0][1] = 26;
			posicionesEnemigas[0][2] = 26;
			
			posicionesEnemigas[1][0] = 15;
			posicionesEnemigas[1][1] = 16;
			posicionesEnemigas[1][2] = 17;
			
			break;
		case 1:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			posicionesMias = new int[2][3];
			posicionesEnemigas = new int[2][3];
			posicionesMias[0][0] = 14;
			posicionesMias[0][1] = 15;
			posicionesMias[0][2] = 16;
			
			posicionesMias[1][0] = 26;
			posicionesMias[1][1] = 26;
			posicionesMias[1][2] = 26;
			
			posicionesEnemigas[0][0] = 26;
			posicionesEnemigas[0][1] = 26;
			posicionesEnemigas[0][2] = 26;
			
			posicionesEnemigas[1][0] = 15;
			posicionesEnemigas[1][1] = 16;
			posicionesEnemigas[1][2] = 17;
			break;
		default:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			posicionesMias = new int[2][3];
			posicionesEnemigas = new int[2][3];
			posicionesMias[0][0] = 14;
			posicionesMias[0][1] = 15;
			posicionesMias[0][2] = 16;
			
			posicionesMias[1][0] = 26;
			posicionesMias[1][1] = 26;
			posicionesMias[1][2] = 26;
			
			posicionesEnemigas[0][0] = 26;
			posicionesEnemigas[0][1] = 26;
			posicionesEnemigas[0][2] = 26;
			
			posicionesEnemigas[1][0] = 15;
			posicionesEnemigas[1][1] = 16;
			posicionesEnemigas[1][2] = 17;
			break;
		}
	}
	
}

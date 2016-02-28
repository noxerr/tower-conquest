package noxer.games.ballons.data;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class OpcionesPartida {

	public int numBolasMias, numBolasEnemigo, numEnemigos, xTorreMia, yTorreMia, xTorreEn, yTorreEn;
	public TiledMap map;
	public int posicionesMias [][], posicionesEnemigas[][];
	public int unidadesMias[]; //0 ballbasic, 1 ballDefender, 2 ballStrong
	public int unidadesEnemigo[]; //0 ballbasic, 1 ballDefender, 2 ballStrong
	public OpcionesPartida(int nivel) {
		switch (nivel) {
		case 0:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			posicionesMias = new int[2][3];
			posicionesEnemigas = new int[2][3];
			posicionesMias[0][0] = 29;
			posicionesMias[0][1] = 30;
			posicionesMias[0][2] = 31;
			
			posicionesMias[1][0] = 41;
			posicionesMias[1][1] = 41;
			posicionesMias[1][2] = 41;
			
			posicionesEnemigas[0][0] = 41;
			posicionesEnemigas[0][1] = 41;
			posicionesEnemigas[0][2] = 41;
			
			posicionesEnemigas[1][0] = 33;
			posicionesEnemigas[1][1] = 31;
			posicionesEnemigas[1][2] = 32;
			unidadesMias = new int [] {0,1,2};
			unidadesEnemigo = new int [] {0,1,0};
			break;
		case 1:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			posicionesMias = new int[2][3];
			posicionesEnemigas = new int[2][3];
			posicionesMias[0][0] = 29;
			posicionesMias[0][1] = 30;
			posicionesMias[0][2] = 31;
			
			posicionesMias[1][0] = 41;
			posicionesMias[1][1] = 41;
			posicionesMias[1][2] = 41;
			
			posicionesEnemigas[0][0] = 41;
			posicionesEnemigas[0][1] = 41;
			posicionesEnemigas[0][2] = 41;
			
			posicionesEnemigas[1][0] = 33;
			posicionesEnemigas[1][1] = 31;
			posicionesEnemigas[1][2] = 32;
			unidadesMias = new int [] {0,1,2};
			unidadesEnemigo = new int [] {0,1,2};
			break;
		default:
			map = new TmxMapLoader().load("maps/First.tmx");
			numBolasMias = 3;
			numBolasEnemigo = 3;
			posicionesMias = new int[2][3];
			posicionesEnemigas = new int[2][3];
			posicionesMias[0][0] = 29;
			posicionesMias[0][1] = 30;
			posicionesMias[0][2] = 31;
			
			posicionesMias[1][0] = 41;
			posicionesMias[1][1] = 41;
			posicionesMias[1][2] = 41;
			
			posicionesEnemigas[0][0] = 41;
			posicionesEnemigas[0][1] = 41;
			posicionesEnemigas[0][2] = 41;
			
			posicionesEnemigas[1][0] = 33;
			posicionesEnemigas[1][1] = 31;
			posicionesEnemigas[1][2] = 32;
			unidadesMias = new int [] {0,1,2};
			unidadesEnemigo = new int [] {0,1,2};
			break;
		}
	}
	
}

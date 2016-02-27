package noxer.games.ballons.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import noxer.games.ballons.TowerConquest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Tower Conquest";
	    config.width = 800;
	    config.height = 480;
		new LwjglApplication(new TowerConquest(), config);
	}
}

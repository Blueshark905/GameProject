package com.parkhon.dabyss.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.parkhon.dabyss.DabyssGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		//Technical Configuration
		//config.useGL30 = true;
		config.forceExit = false;

		//Basic Configuration
		config.title = DabyssGame.getTITLE();
		config.width = DabyssGame.getWIDTH();
		config.height = DabyssGame.getHEIGHT();
		new LwjglApplication(new DabyssGame(), config);
	}
}

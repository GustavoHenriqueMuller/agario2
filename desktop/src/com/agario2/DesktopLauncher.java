package com.agario2;

import com.agario2.util.Constants;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setForegroundFPS(60);
		config.setTitle(Constants.WINDOW_TITLE + " v" + Constants.VERSION);
		config.setResizable(false);

		new Lwjgl3Application(new Game(), config);
	}
}

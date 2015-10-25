package masterofgalaxy.desktop;

import masterofgalaxy.MogGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class DesktopLauncher {
	public static void main(String[] arg) {
		TexturePacker.processIfModified("ui/skin", "ui/", "uiskin");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MogGame(), config);
	}
}

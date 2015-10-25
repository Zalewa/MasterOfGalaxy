package masterofgalaxy.desktop;

import masterofgalaxy.MogGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] args) {
		if (ResourceBuilder.isResourceBuildEnabled(args)) {
			new ResourceBuilder().build();
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new MogGame(), config);
	}
}

package masterofgalaxy.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.collision.Ray;

import masterofgalaxy.ecs.systems.MoveToTargetSystem;
import masterofgalaxy.ecs.systems.RenderingSystem;
import masterofgalaxy.world.worldbuild.GameStartSetup;

public class WorldInputProcessor extends InputAdapter {
    private WorldScreen screen;
    private boolean debug = false;

    public WorldInputProcessor(WorldScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.B) {
            RenderingSystem render = screen.getEntityEngine().getSystem(RenderingSystem.class);
            render.setDrawBounds(!render.isDrawBounds());
        } else if (keycode == Input.Keys.R) {
            screen.startNewGame(GameStartSetup.randomize(screen.getGame()));
        } else if (keycode == Input.Keys.D) {
            debug = !debug;
            screen.setDebug(debug);
        } else if (keycode == Input.Keys.M) {
            MoveToTargetSystem system = screen.getEntityEngine().getSystem(MoveToTargetSystem.class);
            system.setProcessing(!system.checkProcessing());
        } else if (keycode == Input.Keys.O) {
            screen.getDebugMode().setControlOtherPlayers(!screen.getDebugMode().isControlOtherPlayers());
        } else if (keycode == Input.Keys.ESCAPE) {
            screen.escape();
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Ray pickRay = screen.getViewport().getPickRay(screenX, screenY);
        screen.pickEntity(pickRay.origin.x, pickRay.origin.y);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        final float speed = 5.0f * screen.getCamera().getZoom();
        float moveX = Gdx.input.getDeltaX() * speed;
        float moveY = Gdx.input.getDeltaY() * speed;
        screen.getCamera().translate(-moveX, moveY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        final float speed = 0.1f;
        screen.getCamera().zoomBy(amount * speed);
        return false;
    }
}

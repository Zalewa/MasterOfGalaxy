package masterofgalaxy.world.turns;

import com.badlogic.ashley.signals.Signal;
import com.badlogic.gdx.utils.Disposable;
import masterofgalaxy.world.WorldScreen;

public class TurnProcessor implements Disposable {
    private static final float positionEpsilon = 0.1f;

    private WorldScreen screen;
    private Slider slider;
    private ColonyProgressor colonyProgressor;
    private FleetSameDockMerger fleetSameDockMerger;
    private int turn;
    private boolean isProcessing = false;
    private boolean firstProcess = false;

    public Signal<Object> turnChanged = new Signal<Object>();
    public Signal<Object> turnProcessingFinished = new Signal<Object>();

    public TurnProcessor(WorldScreen screen) {
        this.screen = screen;
        slider = new Slider(screen);
        colonyProgressor = new ColonyProgressor(screen);
        fleetSameDockMerger = new FleetSameDockMerger(screen);
    }

    public void startProcessing() {
        isProcessing = true;
        firstProcess = true;
        slider.start();
    }

    public void update(float delta) {
        if (!isProcessing) {
            return;
        }
        if (firstProcess) {
            firstProcess = false;
            return;
        }
        if (isProcessingComplete()) {
            endProcessing();
        }
    }

    private boolean isProcessingComplete() {
        return slider.isProcessingComplete();
    }

    private void endProcessing() {
        colonyProgressor.progress();
        isProcessing = false;
        slider.end();
        fleetSameDockMerger.execute();

        ++turn;
        turnProcessingFinished.dispatch(null);
    }

    private float getFleetSlideTime() {
        return 0.2f;
    }

    @Override
    public void dispose() {
        turnProcessingFinished.removeAllListeners();
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
        turnChanged.dispatch(null);
    }
}

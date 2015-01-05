package masterofgalaxy.world.turns;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import masterofgalaxy.ecs.components.*;
import masterofgalaxy.ecs.systems.MoveToTargetSystem;
import masterofgalaxy.world.WorldScreen;

class Slider {
    private static final float positionEpsilon = 0.1f;

    private WorldScreen screen;
    private Vector2 distanceDiff = new Vector2();
    private Vector2 moveSlice = new Vector2();
    private MoveToTargetSystem moveToTargetSystem;

    public Slider(WorldScreen screen) {
        this.screen = screen;
    }

    public void start() {
        moveToTargetSystem = screen.getEntityEngine().getSystem(MoveToTargetSystem.class);
        moveToTargetSystem.setProcessing(true);
        ImmutableArray<Entity> fleets = screen.getEntityEngine().getEntitiesFor(Family.getFor(FleetComponent.class));
        for (int i = 0; i < fleets.size(); ++i) {
            startFleet(fleets.get(i));
        }
    }

    private void startFleet(Entity fleet) {
        EntityTargetComponent entityTarget = Mappers.entityTarget.get(fleet);
        if (entityTarget.target == null) {
            return;
        }
        BodyComponent targetBody = Mappers.body.get(entityTarget.target);
        FleetComponent fleetComponent = Mappers.fleet.get(fleet);
        BodyComponent body = Mappers.body.get(fleet);
        DockComponent dock = Mappers.dock.get(fleet);
        if (dock != null) {
            Vector2 startingPos = new Vector2(Mappers.body.get(dock.dockedAt).getPosition());
            fleet.remove(DockComponent.class);
            body.setPosition(startingPos);
        }

        MoveTargetComponent moveTarget = Mappers.moveTarget.get(fleet);
        if (moveTarget == null) {
            moveTarget = screen.getEntityEngine().createComponent(MoveTargetComponent.class);
            fleet.add(moveTarget);
        }
        float fleetSpeed = fleetComponent.getSpeedInParsecsPerTurn() * screen.getWorld().getUnitsPerParsec();
        moveSlice = moveTarget.getTarget();
        moveSlice.set(Mappers.body.get(entityTarget.target).getPosition())
                .sub(body.getPosition())
                .nor()
                .scl(fleetSpeed);
        distanceDiff.set(targetBody.getPosition()).sub(body.getPosition());
        if (moveSlice.len2() < distanceDiff.len2()) {
            moveTarget.setTarget(moveSlice.add(body.getPosition()));
        } else {
            moveTarget.setTarget(targetBody.getPosition());
        }
        moveTarget.speed = fleetSpeed / getFleetSlideTime();
    }

    public void end() {
        moveToTargetSystem.setProcessing(false);
        ImmutableArray<Entity> fleets = screen.getEntityEngine().getEntitiesFor(Family.getFor(FleetComponent.class, MoveTargetComponent.class));
        for (int i = fleets.size() - 1; i >= 0; --i) {
            endMovingFleet(fleets.get(i));
        }
    }

    private void endMovingFleet(Entity fleet) {
        BodyComponent fleetBody = Mappers.body.get(fleet);
        EntityTargetComponent entityTarget = Mappers.entityTarget.get(fleet);
        BodyComponent targetBody = Mappers.body.get(entityTarget.target);

        if (targetBody.getPosition().epsilonEquals(fleetBody.getPosition(), positionEpsilon)) {
            DockComponent dock = screen.getEntityEngine().createComponent(DockComponent.class);
            dock.dockedAt = entityTarget.target;
            fleet.add(dock);
            fleet.remove(MoveTargetComponent.class);
            entityTarget.target = null;
        }
    }

    private float getFleetSlideTime() {
        return 0.2f;
    }

    public boolean isProcessingComplete() {
        return moveToTargetSystem.getNumProcessed() == 0;
    }
}

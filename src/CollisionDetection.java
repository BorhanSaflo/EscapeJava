import java.util.Iterator;

import org.jogamp.java3d.*;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
class CollisionDetection extends Behavior {
    private boolean inCollision;
    private Shape3D shape;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public CollisionDetection(Shape3D s) {
        shape = s; // save the original color of 'shape"
        inCollision = false; // initialize the collision state
    }

    public void initialize() { // USE_GEOMETRY USE_BOUNDS
        wEnter = new WakeupOnCollisionEntry(shape, WakeupOnCollisionEntry.USE_GEOMETRY);
        wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_GEOMETRY);
        wakeupOn(wEnter); // initialize the behavior
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes();
        transparencyAttributes.setTransparencyMode(TransparencyAttributes.FASTEST);
        inCollision = !inCollision; // collision has taken place

        if (inCollision) {
            System.out.print("In Collision");
            wakeupOn(wExit); // wait for the next collision
        } else {
            System.out.print("Not in Collision");
            wakeupOn(wEnter); // wait for the next collision
        }
    }
}

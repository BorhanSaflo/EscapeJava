import org.jogamp.java3d.*;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Vector3d;

public class LockPuzzle extends Puzzle {
    private int numSections = 10, lastSection = 0, n = 0, phase = 0, combination[] = { 5, 3, 7 };
    private double adjustmentAngle = 104 * (Math.PI / 180);
    private boolean cw = true;
    private Vector3d dialTranslation = new Vector3d(-0.048, 0.3, 1.06);
    private double dialScale = 0.245;
    private TransformGroup lockTG, dialTG, safeDoorTG;
    private double dialAng = 0;

    public void createPuzzle() {
        TransformGroup safeTG = getCreateObjects().createLooseObject("*safe", new AxisAngle4d(), new Vector3d(0, 0, 0),
                1);
        safeDoorTG = getCreateObjects().createLooseObject("*safe-door", new AxisAngle4d(), new Vector3d(-0.03, 0, 0.9),
                0.83);
        dialTG = getCreateObjects().createLooseObject("*safe-dial", new AxisAngle4d(0, 0, 1, adjustmentAngle),
                dialTranslation, dialScale);

        safeDoorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        safeDoorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        Transform3D lockT3D = new Transform3D();
        lockT3D.rotY(-Math.PI / 4);
        lockT3D.setScale(0.02);
        lockT3D.setTranslation(new Vector3d(0.06, -0.038, 0.45));

        lockTG = new TransformGroup(lockT3D);

        safeTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        safeTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        safeTG.setCapability(TransformGroup.ALLOW_CHILDREN_READ);

        lockTG.addChild(safeTG);
        lockTG.addChild(safeDoorTG);
        lockTG.addChild(dialTG);
    }

    public TransformGroup positionObj() {
        return lockTG;
    }

    public void rotateDial(double angle) {
        if (isUnlocked())
            return;

        dialAng += angle;

        if (dialAng > 2 * Math.PI)
            dialAng -= 2 * Math.PI;
        else if (dialAng < 0)
            dialAng += 2 * Math.PI;

        Transform3D t3d = new Transform3D();
        t3d.set(new AxisAngle4d(0, 0, 1, dialAng + adjustmentAngle));
        t3d.setScale(dialScale);
        t3d.setTranslation(dialTranslation);

        dialTG.setTransform(t3d);

        updateLogic();
    }

    private void updateLogic() {
        int section = (int) (dialAng / (2 * Math.PI) * numSections);

        if (section == lastSection)
            return;

        if (section == (lastSection + 1) % numSections) { // if turned clockwise

            if (cw) { // if continuing clockwise
                n++;

                if (n >= numSections) {
                    phase = 1;
                    n = 1;
                }

                if (phase == 2) {
                    phase = 0;
                    n = 0;
                    return;
                }
            }

            else { // if switching from counter-clockwise
                if (phase == 2 && lastSection == combination[1] && n >= numSections && n < numSections * 2) {
                    phase = 3;
                }

                cw = true;
                n = 1;
            }
        } else if (section == (lastSection - 1 + numSections) % numSections) { // if turned counter-clockwise
            if (phase == 0) {
                n = 0;
                return;
            }

            if (!cw) { // if continuing counter-clockwise
                n++;
                if (phase == 1 || phase == 3) {
                    phase = 0;
                    n = 0;
                    return;
                }
            }

            else { // if switching from clockwise
                if (phase == 1 && lastSection == combination[0]) {
                    phase = 2;
                }

                cw = false;
                n = 1;
            }
        }

        lastSection = section;
    }

    public boolean tryUnlock() {
        if (isUnlocked())
            return false;

        if (phase == 3 && lastSection == combination[2] && n < numSections) {
            unlock();
        }

        if (isUnlocked()) {
            Sounds.playSound(Sounds.successSound);

            Transform3D doort3d = new Transform3D();
            doort3d.rotY(Math.PI / 2);
            doort3d.setScale(0.83);
            doort3d.setTranslation(new Vector3d(0.7, -0.038, 1.7));

            safeDoorTG.setTransform(doort3d);

            Transform3D dialt3d = new Transform3D();
            dialt3d.setScale(0.001);
            dialt3d.rotX(Math.PI / 2);
            dialt3d.setTranslation(new Vector3d(0, -10, 0));

            dialTG.setTransform(dialt3d);
        } else
            Sounds.playSound(Sounds.wrongSound);

        return isUnlocked();
    }
}

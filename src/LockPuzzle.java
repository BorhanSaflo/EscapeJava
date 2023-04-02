import org.jogamp.java3d.*;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Vector3d;

public class LockPuzzle {
    private int numSections = 12, lastSection=0, n=0, phase = 0, combination[] = {5, 3, 7};
    private boolean cw = true, unlocked = false;
    private AxisAngle4d axis = new AxisAngle4d(1, 0, 0, Math.PI/2);
    private Vector3d translation = new Vector3d(0, 0, 0);
    private double scale = 0.05;
    private TransformGroup lockTG, dialTG;
    private double dialAng = 0;

    public LockPuzzle(){
        /*
        bodyTG = CreateObjects.createObject("*lock-body", new AxisAngle4d(), new Vector3d(x, y, z), 0.2);
        dialTG = CreateObjects.createObject("*lock-dial", new AxisAngle4d(), new Vector3d(x, y, z), 0.2);
        shackleTG = CreateObjects.createObject("*lock-shackle", new AxisAngle4d(), new Vector3d(x, y, z), 0.2);

        lockTG = new TransformGroup();
        lockTG.addChild(bodyTG);
        lockTG.addChild(dialTG);
        lockTG.addChild(shackleTG);

         */


        BranchGroup objBG = (BranchGroup) CreateObjects.createObject("*lock", new AxisAngle4d(), new Vector3d(), 0.2).getChild(0);
        Node bodyShape = objBG.getChild(0);
        Node dialShape = objBG.getChild(1);
        Node shackleShape = objBG.getChild(2);

        for(int i=0; i<3; i++)
            objBG.removeChild(0);

        BranchGroup bodyBG = new BranchGroup();
        BranchGroup dialBG = new BranchGroup();
        BranchGroup shackleBG = new BranchGroup();

        bodyBG.addChild(bodyShape);
        dialBG.addChild(dialShape);
        shackleBG.addChild(shackleShape);

        dialTG = new TransformGroup();
        dialTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        dialTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        dialTG.addChild(dialBG);

        Transform3D lockT3D = new Transform3D();
        lockT3D.set(axis);
        lockT3D.setTranslation(translation);
        lockT3D.setScale(scale);

        lockTG = new TransformGroup(lockT3D);
        lockTG.addChild(bodyBG);
        lockTG.addChild(dialTG);
        lockTG.addChild(shackleBG);

    }

    public TransformGroup positionObj(){
        return lockTG;
    }

    public void rotateDial(double angle){
        dialAng += angle;

        if(dialAng>2*Math.PI)
            dialAng -= 2*Math.PI;
        else if(dialAng<0)
            dialAng += 2*Math.PI;

        Transform3D t1 = new Transform3D(), t2 = new Transform3D(), t3 = new Transform3D();
        t1.setTranslation(new Vector3d(0, 0, -0.393));
        t2.rotY(dialAng);
        t3.setTranslation(new Vector3d(0, 0, 0.393));

        Transform3D all = new Transform3D();
        all.mul(t1, all);
        all.mul(t2, all);
        all.mul(t3, all);

        dialTG.setTransform(all);

        updateLogic();
    }

    private void updateLogic(){
        int section = (int) (dialAng/(2*Math.PI)*numSections);

        if(section==lastSection)
            return;

        System.out.println("Phase " + phase + " Section: " + section);

        if(section==(lastSection+1)%numSections) {          // if turned clockwise

            if(cw) {                                        // if continuing clockwise
                n++;

                if(n>=numSections*3){
                    phase = 1;
                    n = 1;
                }

                if(phase == 2){
                    phase = 0;
                    n = 0;
                    return;
                }
            }

            else{                                           // if switching from counter-clockwise
                if(phase==2 && lastSection==combination[1] && n>=numSections && n<numSections*2){
                    phase = 3;
                }

                cw = true;
                n = 1;
            }
        }
        else if(section==(lastSection-1+numSections)%numSections) {     // if turned counter-clockwise
            if(phase == 0){
                n = 0;
                return;
            }

            if(!cw) {                                                     // if continuing counter-clockwise
                n++;
                if(phase == 1 || phase == 3){
                    phase = 0;
                    n = 0;
                    return;
                }
            }

            else{                                                       // if switching from clockwise
                if(phase==1 && lastSection==combination[0]){
                    phase = 2;
                }

                cw = false;
                n = 1;
            }
        }

        lastSection = section;
    }

    public boolean tryUnlock(){
        if(phase==3 && lastSection==combination[2] && n<numSections){
            unlocked = true;
        }

        return unlocked;
    }
}

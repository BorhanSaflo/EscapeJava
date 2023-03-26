import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Vector3d;

public class createObjects {
    public static BranchGroup room() {
        BranchGroup roomBG = new BranchGroup();
        
        /* Prefixes:
         * + Focusable (eg. clues, puzzles)
         * - Focused (shouldn't be used here)
         * ! Immovable (eg. room, windows)
         * @ Interactable (eg. door, trash bin)
         * # Equipable (eg. key, tool)
         * 
         * Note: The given names are set to the object's TG, not Shape3D.
         */
        
        roomBG.addChild(createObject("!emptyroom", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0, 0, 0), 1));
        
        roomBG.addChild(createObject("!windows", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.028, 0.04, 0.118), 0.82));
        roomBG.addChild(createObject("@door1", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.208, -0.02, 0.937), 0.22));
        roomBG.addChild(createObject("@door2", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.21, -0.022, -0.353), 0.36));

        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.05, -0.103, -0.648), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.15, -0.103, -0.648), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.25, -0.103, -0.648), 0.05));

        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI),    new Vector3d(0.382, -0.103, -0.62), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI),    new Vector3d(0.382, -0.103, -0.52), 0.05));

        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI),    new Vector3d(-0.01, -0.103, 0.844), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI/2),  new Vector3d(-0.13, -0.103, 0.884), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI/2),  new Vector3d(-0.23, -0.103, 0.884), 0.05));

        roomBG.addChild(createObject("@blueBin",  new AxisAngle4d(0, 0, 0, Math.PI/2),  new Vector3d(-0.27, -0.103, 0.70), 0.15));
        roomBG.addChild(createObject("@redBin",  new AxisAngle4d(0, 0, 0, Math.PI/2),  new Vector3d(-0.27, -0.103, 0.745), 0.15));
        roomBG.addChild(createObject("@blackBin",  new AxisAngle4d(0, 0, 0, Math.PI/2),  new Vector3d(-0.27, -0.103, 0.79), 0.15));

        roomBG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(-0.27, -0.103, 0.4), 0.15));
        roomBG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(-0.27, -0.103, 0.1), 0.15));
        roomBG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(-0.27, -0.103, -0.2), 0.15));
        
        // roomBG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(-0.27, -0.103, 0.4), 0.15));

        roomBG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4, 0.08, 0.4), 0.05));
        roomBG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4, 0.08, 0.1), 0.05));
        roomBG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4, 0.08, -0.2), 0.05));



        return roomBG;
    }

    public static TransformGroup createObject(String name, AxisAngle4d rotation, Vector3d translation, double scale) {
        Transform3D transform = new Transform3D();
        transform.set(rotation);
        transform.setScale(scale);
        transform.setTranslation(translation);
        
        TransformGroup objTG = new TransformGroup(transform);
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTG.setName(name);
        objTG.setUserData(transform);
        objTG.addChild(LoadObject.loadObject("objects/"+name.substring(1)+".obj"));
        
        return objTG;
    }
}

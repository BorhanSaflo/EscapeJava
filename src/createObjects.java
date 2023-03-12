import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Vector3d;

public class createObjects {
    public static BranchGroup room() {
        BranchGroup roomBG = LoadObject.loadObject("objects/emptyroom.obj");

        roomBG.addChild(createCouch(new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.05, -0.103, -0.648)));
        roomBG.addChild(createCouch(new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.15, -0.103, -0.648)));
        roomBG.addChild(createCouch(new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.25, -0.103, -0.648)));

        roomBG.addChild(createCouch(new AxisAngle4d(0, 1, 0, Math.PI),    new Vector3d(0.382, -0.103, -0.62)));
        roomBG.addChild(createCouch(new AxisAngle4d(0, 1, 0, Math.PI),    new Vector3d(0.382, -0.103, -0.52)));

        roomBG.addChild(createCouch(new AxisAngle4d(0, 1, 0, Math.PI),    new Vector3d(-0.01,  -0.103, 0.844)));
        roomBG.addChild(createCouch(new AxisAngle4d(0, 1, 0, Math.PI/2),  new Vector3d(-0.13, -0.103, 0.884)));
        roomBG.addChild(createCouch(new AxisAngle4d(0, 1, 0, Math.PI/2),  new Vector3d(-0.23, -0.103, 0.884)));

        return roomBG;
    }

    public static TransformGroup createCouch(AxisAngle4d rotation, Vector3d translation) {
        double scale = 0.05;

        Transform3D couchTransform = new Transform3D();
        couchTransform.set(rotation);
        couchTransform.setScale(scale);
        couchTransform.setTranslation(translation);
        TransformGroup couchTG = new TransformGroup(couchTransform);
        couchTG.addChild(LoadObject.loadObject("objects/couch.obj"));
        return couchTG;
    }
}

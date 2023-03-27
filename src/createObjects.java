import org.jogamp.java3d.utils.geometry.Box;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

public class createObjects {

    public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
	public final static Color3f Grey = new Color3f(0.35f, 0.35f, 0.35f);
	public final static Color3f Black = new Color3f(0.0f, 0.0f, 0.0f);
	// public final static Color3f[] clr_list = {Blue, Green, Red, Yellow, Cyan, Orange, Magenta, Grey};
	public final static int clr_num = 8;
	private static Color3f[] mtl_clrs = {White, Grey, Black};


    public static BranchGroup room() {
        BranchGroup roomBG = new BranchGroup();

        /*
         * Prefixes:
         * + Focusable (eg. clues, puzzles)
         * - Focused (shouldn't be used here)
         * ! Immovable (eg. room, windows)
         * 
         * @ Interactable (eg. door, trash bin)
         * # Equipable (eg. key, tool)
         * 
         * Note: The given names are set to the object's TG, not Shape3D.
         */
        
        roomBG.addChild(createObject("!emptyroom", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.007, 0, 0.122), 1.122));
        
        roomBG.addChild(createObject("!windows", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.028, 0.04, 0.118), 0.82));
        roomBG.addChild(createObject("@door1", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.208, -0.02, 0.937), 0.22));
        roomBG.addChild(createObject("@door2", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.21, -0.022, -0.353), 0.36));

        roomBG.addChild(createObject("!whiteboard", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.1, 0.04, -0.35), 0.35));

        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI / 2), new Vector3d(0.05, -0.103, -0.648), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI / 2), new Vector3d(0.15, -0.103, -0.648), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI / 2), new Vector3d(0.25, -0.103, -0.648), 0.05));

        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(0.382, -0.103, -0.62), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(0.382, -0.103, -0.52), 0.05));

        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.01, -0.103, 0.844), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI / 2), new Vector3d(-0.13, -0.103, 0.884), 0.05));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI / 2), new Vector3d(-0.23, -0.103, 0.884), 0.05));

        roomBG.addChild(bins(0, 0, 0));
        roomBG.addChild(tvs(0, 0, 0));
        roomBG.addChild(computers(0, 0, 0));
        roomBG.addChild(highStuff(0, 0, 0));
        roomBG.addChild(middleStuff(-0.1, 0, 0));
        roomBG.addChild(lowStuff(0, 0, 0));

        //puzzles
        roomBG.addChild(new computerPuzzle().positionTextObj());

        return roomBG;
    }

    public static BranchGroup tvs(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, 0.4 + z), 0.05));
        BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, 0.1 + z), 0.05));
        BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, -0.2 + z), 0.05));

        return BG;
    }

    public static BranchGroup computers(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("!computer", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(0.155 + x, -0.03 + y, 0.875 + z), 0.05));
        BG.addChild(createObject("!computer", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(0.275 + x, -0.03 + y, 0.875 + z), 0.05));

        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, 0.7375 + z), 0.05));
        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, 0.635 + z), 0.05));
        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, 0.525 + z), 0.05));

        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, 0.25 + z), 0.05));
        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, 0.125 + z), 0.05));
        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, 0.0 + z), 0.05));

        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, -0.25 + z), 0.05));
        BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI/2), new Vector3d(0.415 + x, -0.03 + y, -0.375 + z), 0.05));

        return BG;
    }

    public static BranchGroup lowStuff(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createBox("desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.2, -0.065, 0.875), 0.3f, 0.005f, 0.1f, 0.5, obj_Appearance(Grey)));
        BG.addChild(createBox("desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.415, -0.065, 0.615), 0.1f, 0.005f, 0.35f, 0.5, obj_Appearance(Grey)));
        BG.addChild(createBox("desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.415, -0.065, 0.125), 0.1f, 0.005f, 0.45f, 0.5, obj_Appearance(Grey)));
        BG.addChild(createBox("desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.415, -0.065, -0.32), 0.1f, 0.005f, 0.265f, 0.5, obj_Appearance(Grey)));

        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.275 + x, -0.086 + y, 0.85 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.15 + x, -0.086 + y, 0.85 + z), 0.0575));

        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, 0.5 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, 0.625 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, 0.75 + z), 0.0575));

        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, 0.3 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, 0.185 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, 0.07 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, -0.045 + z), 0.0575));

        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, -0.25 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.385 + x, -0.086 + y, -0.375 + z), 0.0575));

        return BG;        
    }

    public static BranchGroup bins(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("@blueBin",  new AxisAngle4d(0, 0, 0, Math.PI/2),  new Vector3d(-0.27 + x, -0.103 + y, 0.70 + z), 0.15));
        BG.addChild(createObject("@redBin",  new AxisAngle4d(0, 0, 0, Math.PI/2),  new Vector3d(-0.27 + x, -0.103 + y, 0.745 + z), 0.15));
        BG.addChild(createObject("@blackBin",  new AxisAngle4d(0, 0, 0, Math.PI/2),  new Vector3d(-0.27 + x, -0.103 + y, 0.79 + z), 0.15));

        return BG;
    }

    public static BranchGroup highStuff(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(-0.27 + x, -0.085 + y, 0.4 + z), 0.15));
        BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(-0.27 + x, -0.085 + y, 0.1 + z), 0.15));
        BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(-0.27 + x, -0.085 + y, -0.2 + z), 0.15));
        
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.35 + x, -0.063 + y, 0.5 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.2 + x, -0.063 + y, 0.5 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.35 + x, -0.063 + y, 0.32 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.2 + x, -0.063 + y, 0.32 + z), 0.08));

        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.35 + x, -0.063 + y, 0.18 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.2 + x, -0.063 + y, 0.18 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.35 + x, -0.063 + y, 0.01 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.2 + x, -0.063 + y, 0.01 + z), 0.08));

        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.35 + x, -0.063 + y, -0.12 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.2 + x, -0.063 + y, -0.12 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.35 + x, -0.063 + y, -0.3 + z), 0.08));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.2 + x, -0.063 + y, -0.3 + z), 0.08));

        return BG;
    }

    public static BranchGroup middleStuff(double x, double y, double z){
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("+middletable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.16 + x, -0.10 + y, -0.2 + z), 0.06));
        BG.addChild(createObject("+middletable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.16 + x, -0.10 + y, 0.125 + z), 0.06));
        BG.addChild(createObject("+middletable", new AxisAngle4d(0, 1, 0, Math.PI/2), new Vector3d(0.16 + x, -0.10 + y, 0.45 + z), 0.06));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90), new Vector3d(0.17 + x, -0.09 + y, -0.16 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90), new Vector3d(0.17 + x, -0.09 + y, 0.165 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90), new Vector3d(0.17 + x, -0.09 + y, 0.49 + z), 0.07));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI/2 * .80), new Vector3d(0.13 + x, -0.09 + y, -0.19 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI/2 * .80), new Vector3d(0.13 + x, -0.09 + y, 0.135 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI/2 * .80), new Vector3d(0.13 + x, -0.09 + y, 0.46 + z), 0.07));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90), new Vector3d(0.15 + x, -0.09 + y, -0.23 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90), new Vector3d(0.15 + x, -0.09 + y, 0.095 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90), new Vector3d(0.15 + x, -0.09 + y, 0.42 + z), 0.07));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40), new Vector3d(0.20 + x, -0.09 + y, -0.21 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40), new Vector3d(0.20 + x, -0.09 + y, 0.115 + z), 0.07));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40), new Vector3d(0.20 + x, -0.09 + y, 0.44 + z), 0.07));

        return BG;
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
        objTG.addChild(LoadObject.loadObject("objects/" + name.substring(1) + ".obj"));

        return objTG;
    }

    public static TransformGroup createBox(String name, AxisAngle4d rotation, Vector3d translation, float x, float y, float z, double scale, Appearance appearance) {
        Transform3D transform = new Transform3D();
        transform.set(rotation);
        transform.setScale(scale);
        transform.setTranslation(translation);

        TransformGroup objTG = new TransformGroup(transform);
        objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTG.setName(name);
        objTG.setUserData(transform);
        objTG.addChild(new Box(x, y, z, Primitive.GENERATE_NORMALS, appearance));
        return objTG;
    }

    public static Appearance obj_Appearance(Color3f m_clr) {		
		Material mtl = new Material();                     
		mtl.setShininess(32);
		mtl.setAmbientColor(mtl_clrs[0]);                   
		mtl.setDiffuseColor(m_clr);
		mtl.setSpecularColor(mtl_clrs[1]);
		mtl.setEmissiveColor(mtl_clrs[2]);                  
		mtl.setLightingEnable(true);

		Appearance app = new Appearance();
		app.setMaterial(mtl);                              
		return app;
	}
}
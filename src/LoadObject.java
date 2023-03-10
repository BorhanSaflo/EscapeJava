import java.io.FileNotFoundException;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Color3f;

public class LoadObject {
    public final static Color3f Yellow = new Color3f(1.0f, 1.0f, 0.0f);
    public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
    public final static Color3f Grey = new Color3f(0.35f, 0.35f, 0.35f);
    public final static Color3f Black = new Color3f(0.0f, 0.0f, 0.0f);
    public final static int clr_num = 8;
    private static Color3f[] mtl_clrs = { White, Grey, Black };

    public static Appearance obj_Appearance(Color3f m_clr) {
        Material mtl = new Material(); // define material's attributes
        mtl.setShininess(32);
        mtl.setAmbientColor(mtl_clrs[0]); // use them to define different materials
        mtl.setDiffuseColor(m_clr);
        mtl.setSpecularColor(mtl_clrs[1]);
        mtl.setEmissiveColor(mtl_clrs[2]); // use it to switch button on/off
        mtl.setLightingEnable(true);

        Appearance app = new Appearance();
        app.setMaterial(mtl); // set appearance's material
        return app;
    }

    public static BranchGroup loadObject(String objName) {

        ObjectFile loader = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI /
                180.0));

        BranchGroup objGroup = null;
        try {
            objGroup = loader.load(objName).getSceneGroup();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParsingErrorException e) {
            e.printStackTrace();
        } catch (IncorrectFormatException e) {
            e.printStackTrace();
        }

        // String mtlFilename = "objects/untitled.mtl";
        // try {
        // ArrayList<Appearance> appearances = MTLParser.parseMTL(mtlFilename);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        for (int i = 0; i < objGroup.numChildren(); i++) {
            Shape3D shape = (Shape3D) objGroup.getChild(i);
            shape.setAppearance(obj_Appearance(Yellow));
        }
        return objGroup;
    }
}

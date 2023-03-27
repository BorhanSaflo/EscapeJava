import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.ImageComponent2D;
import org.jogamp.java3d.Link;
import org.jogamp.java3d.SharedGroup;
import org.jogamp.java3d.Texture;
import org.jogamp.java3d.Texture2D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.image.TextureLoader;

public class createObjects {
    public static BranchGroup room() {
        BranchGroup roomBG = new BranchGroup();
        SharedGroup[] roomSG = createSG();

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

        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                new Vector3d(0.05, -0.103, -0.648), 0.05, roomSG));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                new Vector3d(0.15, -0.103, -0.648), 0.05, roomSG));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                new Vector3d(0.25, -0.103, -0.648), 0.05, roomSG));

        roomBG.addChild(
                createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(0.382, -0.103, -0.62), 0.05, roomSG));
        roomBG.addChild(
                createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(0.382, -0.103, -0.52), 0.05, roomSG));

        roomBG.addChild(
                createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI), new Vector3d(-0.01, -0.103, 0.844), 0.05, roomSG));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(-0.13, -0.103, 0.884), 0.05, roomSG));
        roomBG.addChild(createObject("+couch", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(-0.23, -0.103, 0.884), 0.05, roomSG));

        roomBG.addChild(
                createObject("@computer", new AxisAngle4d(0, -1, 0, Math.PI / 2), new Vector3d(0.4, 0.0, -0.16), 0.05));

        roomBG.addChild(
                createObject("!whiteboard", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.1, 0.04, -0.35), 0.35));

        roomBG.addChild(bins(0, 0, 0));
        roomBG.addChild(tvs(0, 0, 0));
        roomBG.addChild(highStuff(0, 0, 0, roomSG));
        roomBG.addChild(middleStuff(-0.1, 0, 0, roomSG));
        roomBG.addChild(lowStuff(0, 0, 0));

        // puzzles
        roomBG.addChild(new computerPuzzle().positionTextObj());

        // Window backgrounds
        roomBG.addChild(windowBackground("IMG_6706", 1.2f, 0.35f, 0.01f, 0f, 0f, -1f));
        roomBG.addChild(windowBackground("WindowBackground2", 0.01f, 1.0f, 3f, 0.8f, 0.2f, 0.2f));
        roomBG.addChild(windowBackground("WindowBackground", 2f, 1.0f, 0.01f, 0f, 0.2f, 1.3f));
        
        // sound Effects
        roomBG.addChild(Sounds.keyboardSound);
        roomBG.addChild(Sounds.wrongSound);
        roomBG.addChild(Sounds.successSound);

        return roomBG;
    }

    public static TransformGroup windowBackground(String fileName, float width, float height, float depth, float x,
            float y, float z) {
        TextureLoader loader = new TextureLoader("objects/images/" + fileName + ".jpg", null);
        ImageComponent2D image = loader.getImage();

        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        texture.setEnable(true); 

        TransparencyAttributes transparencyAttr = new TransparencyAttributes();
        transparencyAttr.setTransparencyMode(TransparencyAttributes.BLEND_ONE_MINUS_SRC_ALPHA);
        transparencyAttr.setTransparency(0.65f); 

        Appearance app = new Appearance();
        app.setTexture(texture);
        app.setTransparencyAttributes(transparencyAttr);

        Box background = new Box(width, height, depth, Primitive.GENERATE_TEXTURE_COORDS, app);

        Transform3D transform = new Transform3D();
        transform.setTranslation(new Vector3d(x, y, z));

        Transform3D scaler = new Transform3D();
        scaler.setScale(0.4);
        transform.mul(scaler);

        TransformGroup backgroundTG = new TransformGroup(transform);
        backgroundTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        backgroundTG.addChild(background);

        return backgroundTG;
    }

    public static BranchGroup tvs(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, 0.4 + z), 0.05));
        BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, 0.1 + z), 0.05));
        BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, -0.2 + z), 0.05));

        return BG;
    }

    public static BranchGroup lowStuff(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("!longdesk-half", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(0.2 + x, -0.065 + y, 0.895 + z), 0.15));
        BG.addChild(createObject("!longdesk", new AxisAngle4d(0, 0, 0, Math.PI / 2),
                new Vector3d(0.43 + x, -0.065 + y, 0.7 + z), 0.3));

        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(0.275 + x, -0.086 + y, 0.85 + z), 0.0575));
        BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(0.15 + x, -0.086 + y, 0.85 + z), 0.0575));

        return BG;
    }

    public static BranchGroup bins(double x, double y, double z) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("@blueBin", new AxisAngle4d(0, 0, 0, Math.PI / 2),
                new Vector3d(-0.27 + x, -0.103 + y, 0.70 + z), 0.15));
        BG.addChild(createObject("@redBin", new AxisAngle4d(0, 0, 0, Math.PI / 2),
                new Vector3d(-0.27 + x, -0.103 + y, 0.745 + z), 0.15));
        BG.addChild(createObject("@blackBin", new AxisAngle4d(0, 0, 0, Math.PI / 2),
                new Vector3d(-0.27 + x, -0.103 + y, 0.79 + z), 0.15));

        return BG;
    }

    public static BranchGroup highStuff(double x, double y, double z, SharedGroup[] SG) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(-0.27 + x, -0.085 + y, 0.4 + z), 0.15, SG));
        BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(-0.27 + x, -0.085 + y, 0.1 + z), 0.15, SG));
        BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(-0.27 + x, -0.085 + y, -0.2 + z), 0.15, SG));

        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                new Vector3d(-0.35 + x, -0.063 + y, 0.5 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                new Vector3d(-0.2 + x, -0.063 + y, 0.5 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(-0.35 + x, -0.063 + y, 0.32 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(-0.2 + x, -0.063 + y, 0.32 + z), 0.08, SG));

        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                new Vector3d(-0.35 + x, -0.063 + y, 0.18 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                new Vector3d(-0.2 + x, -0.063 + y, 0.18 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(-0.35 + x, -0.063 + y, 0.01 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(-0.2 + x, -0.063 + y, 0.01 + z), 0.08, SG));

        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                new Vector3d(-0.35 + x, -0.063 + y, -0.12 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                new Vector3d(-0.2 + x, -0.063 + y, -0.12 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(-0.35 + x, -0.063 + y, -0.3 + z), 0.08, SG));
        BG.addChild(createObject("!chair-high", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(-0.2 + x, -0.063 + y, -0.3 + z), 0.08, SG));

        return BG;
    }

    public static BranchGroup middleStuff(double x, double y, double z, SharedGroup[] SG) {
        BranchGroup BG = new BranchGroup();

        BG.addChild(createObject("+middletable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(0.16 + x, -0.10 + y, -0.2 + z), 0.06, SG));
        BG.addChild(createObject("+middletable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(0.16 + x, -0.10 + y, 0.125 + z), 0.06, SG));
        BG.addChild(createObject("+middletable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                new Vector3d(0.16 + x, -0.10 + y, 0.45 + z), 0.06, SG));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90),
                new Vector3d(0.17 + x, -0.09 + y, -0.16 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90),
                new Vector3d(0.17 + x, -0.09 + y, 0.165 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90),
                new Vector3d(0.17 + x, -0.09 + y, 0.49 + z), 0.07, SG));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI / 2 * .80),
                new Vector3d(0.13 + x, -0.09 + y, -0.19 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI / 2 * .80),
                new Vector3d(0.13 + x, -0.09 + y, 0.135 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI / 2 * .80),
                new Vector3d(0.13 + x, -0.09 + y, 0.46 + z), 0.07, SG));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90),
                new Vector3d(0.15 + x, -0.09 + y, -0.23 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90),
                new Vector3d(0.15 + x, -0.09 + y, 0.095 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90),
                new Vector3d(0.15 + x, -0.09 + y, 0.42 + z), 0.07, SG));

        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40),
                new Vector3d(0.20 + x, -0.09 + y, -0.21 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40),
                new Vector3d(0.20 + x, -0.09 + y, 0.115 + z), 0.07, SG));
        BG.addChild(createObject("+middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40),
                new Vector3d(0.20 + x, -0.09 + y, 0.44 + z), 0.07, SG));

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

    public static TransformGroup createObject(String name, AxisAngle4d rotation, Vector3d translation, double scale, SharedGroup[] tempSG) {
        if(name == "+middlechair") {
            SharedGroup SG = tempSG[0];
            Link link = new Link(SG);

            Transform3D transform = new Transform3D();
            transform.set(rotation);
            transform.setScale(scale);
            transform.setTranslation(translation);

            TransformGroup objTG = new TransformGroup(transform);
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTG.setName(name);
            objTG.setUserData(transform);
            objTG.addChild(link);

            return objTG;
        }
        else if (name == "+middletable") {
            SharedGroup SG = tempSG[1];
            Link link = new Link(SG);

            Transform3D transform = new Transform3D();
            transform.set(rotation);
            transform.setScale(scale);
            transform.setTranslation(translation);

            TransformGroup objTG = new TransformGroup(transform);
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTG.setName(name);
            objTG.setUserData(transform);
            objTG.addChild(link);

            return objTG;
        }
        else if (name == "+couch") {
            SharedGroup SG = tempSG[2];
            Link link = new Link(SG);    

            Transform3D transform = new Transform3D();
            transform.set(rotation);
            transform.setScale(scale);
            transform.setTranslation(translation);

            TransformGroup objTG = new TransformGroup(transform);
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTG.setName(name);
            objTG.setUserData(transform);
            objTG.addChild(link);

            return objTG;
        }
        else if (name == "!highTable") {
            SharedGroup SG = tempSG[3];
            Link link = new Link(SG);
                
            Transform3D transform = new Transform3D();
            transform.set(rotation);
            transform.setScale(scale);
            transform.setTranslation(translation);

            TransformGroup objTG = new TransformGroup(transform);
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTG.setName(name);
            objTG.setUserData(transform);
            objTG.addChild(link);
    
            return objTG;
        }
        else if (name == "!chair-high") {
            SharedGroup SG = tempSG[4];
            Link link = new Link(SG);

            Transform3D transform = new Transform3D();
            transform.set(rotation);
            transform.setScale(scale);
            transform.setTranslation(translation);

            TransformGroup objTG = new TransformGroup(transform);
            objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTG.setName(name);
            objTG.setUserData(transform);
            objTG.addChild(link);

            return objTG;
        }
        else {
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
    }
    
    public static SharedGroup[] createSG() {
        SharedGroup[] SGs = new SharedGroup[5];

        BranchGroup middleChairBG = LoadObject.loadObject("objects/middlechair.obj");
        SharedGroup middleChairSG = new SharedGroup();
        middleChairSG.addChild(middleChairBG);
        middleChairSG.compile();
        SGs[0] = middleChairSG;

        BranchGroup middleTableBG = LoadObject.loadObject("objects/middletable.obj");
        SharedGroup middleTableSG = new SharedGroup();
        middleTableSG.addChild(middleTableBG);
        middleTableSG.compile();
        SGs[1] = middleTableSG;

        BranchGroup couchBG = LoadObject.loadObject("objects/couch.obj");
        SharedGroup couchSG = new SharedGroup();
        couchSG.addChild(couchBG);
        couchSG.compile();
        SGs[2] = couchSG;

        BranchGroup highTableBG = LoadObject.loadObject("objects/highTable.obj");
        SharedGroup highTableSG = new SharedGroup();
        highTableSG.addChild(highTableBG);
        highTableSG.compile();
        SGs[3] = highTableSG;
        
        BranchGroup highChairsBG = LoadObject.loadObject("objects/chair-high.obj");
        SharedGroup highChairsSG = new SharedGroup();
        highChairsSG.addChild(highChairsBG);
        highChairsSG.compile();
        SGs[4] = highChairsSG;

        //TODO: tvs SharedGroup
        //TODO: windows SharedGroup
        //TODO: computers SharedGroup

        return SGs;
    }
}
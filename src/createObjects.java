import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.image.TextureLoader;

public class createObjects {
        private static SharedGroup[] roomSG = new SharedGroup[7];
        public static String[] SGObjects = { "middlechair", "middletable", "couch", "highTable", "chair-high", "chair-low",
                        "computer" };
        public static RotationInterpolator door1Rot, door2Rot;
        private static BranchGroup roomBG = new BranchGroup();

        public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
        public final static Color3f Grey = new Color3f(0.35f, 0.35f, 0.35f);
        public final static Color3f Black = new Color3f(0.0f, 0.0f, 0.0f);
        public final static Color3f Red = new Color3f(0.8f, 0.0f, 0.0f);
        public final static Color3f Green = new Color3f(0.0f, 0.8f, 0.0f);
        public final static Color3f Blue = new Color3f(0.0f, 0.0f, 0.8f);
        public final static Color3f Yellow = new Color3f(0.8f, 0.8f, 0.0f);

        public static BranchGroup room() {
                createSG();
                                

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

                roomBG.addChild(createObject("!emptyroom", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.007, 0, 0.122),
                                1.122));

                roomBG.addChild(createObject("!ceiling", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0, 0.13, 0), 1.122));
                roomBG.addChild(createObject("!floor", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0, -0.0535, 0),
                                1.122));
                roomBG.addChild(createObject("!windows", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.028, 0.04, 0.118),
                                0.82));

                roomBG.addChild(createObject("!door1", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.208, -0.02, 0.937),
                                0.22));
                roomBG.addChild(createObject("@doorKnob1", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.153, -0.045, 0.937), 0.18));
                roomBG.addChild(createObject("!door2", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.21, -0.022, -0.347),
                                0.36));
                roomBG.addChild(createObject("@doorKnob2", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.153, -0.045, -0.352), 0.36));

                roomBG.addChild(createObject("!whiteboard", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.1, 0.04, -0.36), 0.344));

                
                double z = -0.45;
                for (int i = 0; i < 3; i++) {

                        TransformGroup objRG = new TransformGroup();
                        objRG.addChild(createObject("!ceilingfan", new AxisAngle4d(0, 0, 0, 0),
                                        new Vector3d(0.225, 0.135, z), 0.1));
                        Transform3D t3d = new Transform3D();
                        t3d.rotX(Math.PI);
                        t3d.setTranslation(new Vector3d(0.225 - 0.0055, 0.135, z - 0.0195));
                        roomBG.addChild(rotate_Behavior(1000, objRG, t3d));
                        roomBG.addChild(objRG);

                        z += 0.45;
                }

                roomBG.addChild(couches(0, 0, 0));
                roomBG.addChild(bins(0, 0, 0));
                roomBG.addChild(tvs(0, 0, 0));
                roomBG.addChild(computers(0, 0, 0));
                roomBG.addChild(highStuff(0, 0, 0));
                roomBG.addChild(middleStuff(-0.1, 0, 0));
                roomBG.addChild(lowStuff(0, 0, 0));

                // puzzles
                roomBG.addChild(new computerPuzzle().positionTextObj());
                roomBG.addChild(computerPuzzleClues());

                // Window backgrounds
                roomBG.addChild(windowBackground("!WindowBackground", 0.01f, 0.85f, 6.5f, 0.8f, -0.025f, 0.2f));
                roomBG.addChild(windowBackground("!WindowBackground2", 3.8f, 0.85f, 0.01f, 0f, -0.025f, -1f));
                roomBG.addChild(windowBackground("!WindowBackground3", 3.8f, 0.85f, 0.01f, 0f, -0.025f, 1.3f));

                // sound Effects
                roomBG.addChild(Sounds.keyboardSound);
                roomBG.addChild(Sounds.wrongSound);
                roomBG.addChild(Sounds.successSound);

                return roomBG;
        }

        public static TransformGroup windowBackground(String fileName, float width, float height, float depth, float x,
                        float y, float z) {
                TextureLoader loader = new TextureLoader("objects/images/" + fileName.substring(1) + ".jpg", null);
                ImageComponent2D image = loader.getImage();

                Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGB, image.getWidth(), image.getHeight());
                texture.setImage(0, image);
                texture.setEnable(true);

                Appearance app = new Appearance();
                app.setTexture(texture);

                Box background = new Box(width, height, depth, Primitive.GENERATE_TEXTURE_COORDS, app);

                Transform3D transform = new Transform3D();
                transform.setTranslation(new Vector3d(x, y, z));

                Transform3D scaler = new Transform3D();
                scaler.setScale(0.2);
                transform.mul(scaler);

                TransformGroup backgroundTG = new TransformGroup(transform);
                backgroundTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

                backgroundTG.addChild(background);

                return backgroundTG;
        }

        public static BranchGroup couches(double x, double y, double z) {
                BranchGroup BG = new BranchGroup();
                

                BG.addChild(createObject("!couch", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.05, -0.103, -0.648), 0.05));
                BG.addChild(createObject("!couch", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.15, -0.103, -0.648), 0.05));
                BG.addChild(createObject("!couch", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.25, -0.103, -0.648), 0.05));

                BG.addChild(createObject("!couch", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(0.382, -0.103, -0.62), 0.05));
                BG.addChild(createObject("!couch", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(0.382, -0.103, -0.52), 0.05));

                BG.addChild(createObject("!couch", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(-0.01, -0.103, 0.844), 0.05));
                BG.addChild(createObject("!couch", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(-0.13, -0.103, 0.884), 0.05));
                BG.addChild(createObject("!couch", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(-0.23, -0.103, 0.884), 0.05));

                
                BG.addChild(createObject("!ottoman", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.16, -0.12, -0.515), 0.07));

                return BG;
        }

        public static BranchGroup tvs(double x, double y, double z) {
                BranchGroup BG = new BranchGroup();
                

                BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, 0.4 + z),
                                0.05));
                BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, 0.1 + z),
                                0.05));
                BG.addChild(createObject("!tv", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.4 + x, 0.08 + y, -0.2 + z),
                                0.05));

                return BG;
        }

        public static BranchGroup computers(double x, double y, double z) {
                BranchGroup BG = new BranchGroup();
                

                BG.addChild(createObject("!computer", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(0.155 + x, -0.03 + y, 0.875 + z), 0.05));
                BG.addChild(createObject("!computer", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(0.275 + x, -0.03 + y, 0.875 + z), 0.05));

                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, 0.7375 + z), 0.05));
                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, 0.635 + z), 0.05));
                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, 0.525 + z), 0.05));

                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, 0.25 + z), 0.05));
                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, 0.125 + z), 0.05));
                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, 0.0 + z), 0.05));

                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, -0.25 + z), 0.05));
                BG.addChild(createObject("!computer", new AxisAngle4d(0, -1, 0, Math.PI / 2),
                                new Vector3d(0.415 + x, -0.03 + y, -0.375 + z), 0.05));

                return BG;
        }

        public static BranchGroup lowStuff(double x, double y, double z) {
                BranchGroup BG = new BranchGroup();
                

                BG.addChild(createBox("!desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.2, -0.065, 0.875), 0.3f,
                                0.005f, 0.1f, 0.5, LoadObject.obj_Appearance(Grey)));
                BG.addChild(createBox("!desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.415, -0.065, 0.615), 0.1f,
                                0.005f, 0.35f, 0.5, LoadObject.obj_Appearance(Grey)));
                BG.addChild(createBox("!desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.415, -0.065, 0.125), 0.1f,
                                0.005f, 0.45f, 0.5, LoadObject.obj_Appearance(Grey)));
                BG.addChild(createBox("!desk", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.415, -0.065, -0.32), 0.1f,
                                0.005f, 0.265f, 0.5, LoadObject.obj_Appearance(Grey)));

                
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(0.275 + x, -0.086 + y, 0.85 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(0.15 + x, -0.086 + y, 0.85 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(0.275 + x, -0.086 + y, 0.85 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(0.15 + x, -0.086 + y, 0.85 + z), 0.0575));

                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, 0.5 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, 0.625 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, 0.75 + z), 0.0575));

                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, 0.3 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, 0.185 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, 0.07 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, -0.045 + z), 0.0575));

                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, -0.25 + z), 0.0575));
                BG.addChild(createObject("!chair-low", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.385 + x, -0.086 + y, -0.375 + z), 0.0575));

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

        public static BranchGroup highStuff(double x, double y, double z) {
                BranchGroup BG = new BranchGroup();
                

                BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(-0.27 + x, -0.085 + y, 0.4 + z), 0.15));
                BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(-0.27 + x, -0.085 + y, 0.1 + z), 0.15));
                BG.addChild(createObject("!highTable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(-0.27 + x, -0.085 + y, -0.2 + z), 0.15));

                
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(-0.35 + x, -0.063 + y, 0.5 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(-0.2 + x, -0.063 + y, 0.5 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.35 + x, -0.063 + y, 0.32 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.2 + x, -0.063 + y, 0.32 + z), 0.08));

                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(-0.35 + x, -0.063 + y, 0.18 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(-0.2 + x, -0.063 + y, 0.18 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.35 + x, -0.063 + y, 0.01 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.2 + x, -0.063 + y, 0.01 + z), 0.08));

                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(-0.35 + x, -0.063 + y, -0.12 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 1, 0, Math.PI),
                                new Vector3d(-0.2 + x, -0.063 + y, -0.12 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.35 + x, -0.063 + y, -0.3 + z), 0.08));
                BG.addChild(createObject("@chair-high", new AxisAngle4d(0, 0, 0, 0),
                new Vector3d(-0.2 + x, -0.063 + y, -0.3 + z), 0.08));

                return BG;
        }

        public static void rotateChair(double angle, TransformGroup chairTG) {
                Transform3D chairTransform = new Transform3D();
                chairTG.getTransform(chairTransform);
                chairTransform.setRotation(new AxisAngle4d(0, 1, 0, angle));
                chairTG.setTransform(chairTransform);
        }

        public static BranchGroup middleStuff(double x, double y, double z) {
                BranchGroup BG = new BranchGroup();
                

                BG.addChild(createObject("!middletable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.16 + x, -0.10 + y, -0.2 + z), 0.06));
                BG.addChild(createObject("!middletable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.16 + x, -0.10 + y, 0.125 + z), 0.06));
                BG.addChild(createObject("!middletable", new AxisAngle4d(0, 1, 0, Math.PI / 2),
                                new Vector3d(0.16 + x, -0.10 + y, 0.45 + z), 0.06));

                
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90),
                                new Vector3d(0.17 + x, -0.09 + y, -0.16 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90),
                                new Vector3d(0.17 + x, -0.09 + y, 0.165 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * .90),
                                new Vector3d(0.17 + x, -0.09 + y, 0.49 + z), 0.07));

                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI / 2 * .80),
                                new Vector3d(0.13 + x, -0.09 + y, -0.19 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI / 2 * .80),
                                new Vector3d(0.13 + x, -0.09 + y, 0.135 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI / 2 * .80),
                                new Vector3d(0.13 + x, -0.09 + y, 0.46 + z), 0.07));

                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90),
                                new Vector3d(0.15 + x, -0.09 + y, -0.23 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90),
                                new Vector3d(0.15 + x, -0.09 + y, 0.095 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.90),
                                new Vector3d(0.15 + x, -0.09 + y, 0.42 + z), 0.07));

                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40),
                                new Vector3d(0.20 + x, -0.09 + y, -0.21 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40),
                                new Vector3d(0.20 + x, -0.09 + y, 0.115 + z), 0.07));
                BG.addChild(createObject("!middlechair", new AxisAngle4d(0, 1, 0, Math.PI * 1.40),
                                new Vector3d(0.20 + x, -0.09 + y, 0.44 + z), 0.07));

                return BG;
        }

        public static TransformGroup createLooseObject(String name, AxisAngle4d rotation, Vector3d translation,
                        double scale) {
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

        public static TransformGroup createObject(String name, AxisAngle4d rotation, Vector3d translation,
                        double scale) {
                if (name.substring(1, name.length() - 1).equals("doorKnob")) {
                        TransformGroup RG = new TransformGroup();
                        RG.addChild(createLooseObject(name, rotation, translation, scale));

                        RotationInterpolator rot = rotate_Behavior(0, RG, new Transform3D());
                        roomBG.addChild(rot);

                        if (name.substring(9).charAt(0) - '0' == 1)
                                door1Rot = rot;
                        else
                                door2Rot = rot;

                        return RG;
                }

                for (int i = 0; i < SGObjects.length; i++) {
                        if (name.substring(1).equals(SGObjects[i])) {
                                SharedGroup SG = roomSG[i];
                                Link link = new Link(SG);
                                link.setName(SGObjects[i]);

                                Transform3D transform = new Transform3D();
                                transform.set(rotation);
                                transform.setScale(scale);
                                transform.setTranslation(translation);
                                
                                TransformGroup objTG = new TransformGroup(transform);
                                objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                                objTG.addChild(link);
                                objTG.setName(name);
                                objTG.setUserData(transform);

                                return objTG;
                        }
                }

                return createLooseObject(name, rotation, translation, scale);
        }

        public static void createSG() {
                for (int i = 0; i < 7; i++) {
                        SharedGroup objSG = new SharedGroup();
                        objSG.addChild(LoadObject.loadObject("objects/" + SGObjects[i] + ".obj"));
                        objSG.getChild(0).setName(SGObjects[i]);
                        objSG.setName(SGObjects[i]);
                        objSG.compile();
                        roomSG[i] = objSG;
                }
        }

        public static TransformGroup createBox(String name, AxisAngle4d rotation, Vector3d translation, float x,
                        float y, float z, double scale, Appearance appearance) {
                Transform3D transform = new Transform3D();
                transform.set(rotation);
                transform.setScale(scale);
                transform.setTranslation(translation);

                TransformGroup objTG = new TransformGroup(transform);
                objTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                objTG.setName(name);
                objTG.setUserData(transform);
                objTG.addChild(new Box(x, y, z, Primitive.GENERATE_NORMALS, appearance));

                if (name.equals("redClue")) {
                        Transform3D textTransform = new Transform3D();
                        textTransform.setScale(0.05);
                        textTransform.set(new AxisAngle4d(0, -1, 0, Math.PI / 2));

                        TransformGroup textTG = new TransformGroup(textTransform);
                        textTG.addChild(computerPuzzle.createTextObj("4", White));

                        objTG.addChild(textTG);
                }

                return objTG;
        }

        public static BranchGroup computerPuzzleClues() {
                BranchGroup BG = new BranchGroup();
                

                BG.addChild(createBox("+redBox1", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.4, -0.0575, -0.065), 0.1f,
                                0.1f, 0.1f, 0.05f, LoadObject.obj_Appearance(Red)));
                BG.addChild(createBox("+redBox2", new AxisAngle4d(0, -1, 0, Math.PI / 4),
                                new Vector3d(0.425, -0.0575, 0.05), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Red)));

                BG.addChild(createBox("+greenBox1", new AxisAngle4d(0, -1, 0, Math.PI / 6),
                                new Vector3d(0.385, -0.0575, 0.3), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Green)));
                BG.addChild(createBox("+greenBox2", new AxisAngle4d(0, -1, 0, Math.PI / 4),
                                new Vector3d(0.424, -0.0575, -0.055), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Green)));
                BG.addChild(createBox("+greenBox3", new AxisAngle4d(0, -1, 0, Math.PI / 4),
                                new Vector3d(0.4, -0.0575, -0.085), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Green)));
                BG.addChild(createBox("+greenBox4", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.45, -0.0575, -0.2),
                                0.1f, 0.1f, 0.1f, 0.05f, LoadObject.obj_Appearance(Green)));
                BG.addChild(createBox("+greenBox5", new AxisAngle4d(0, -1, 0, Math.PI / 6),
                                new Vector3d(0.375, -0.0575, 0.5), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Green)));

                BG.addChild(createBox("+blueBox1", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.435, -0.0575, -0.3),
                                0.1f, 0.1f, 0.1f, 0.05f, LoadObject.obj_Appearance(Blue)));
                BG.addChild(createBox("+blueBox2", new AxisAngle4d(0, 1, 0, Math.PI / 4),
                                new Vector3d(0.435, -0.0575, 0.775), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Blue)));
                BG.addChild(createBox("+blueBox3", new AxisAngle4d(0, 1, 0, Math.PI / 6),
                                new Vector3d(0.1, -0.0575, 0.9), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Blue)));

                BG.addChild(createBox("+yellowBox1", new AxisAngle4d(0, 0, 0, 0), new Vector3d(0.425, -0.0575, -0.08),
                                0.1f, 0.1f, 0.1f, 0.05f, LoadObject.obj_Appearance(Yellow)));
                BG.addChild(createBox("+yellowBox2", new AxisAngle4d(0, -1, 0, Math.PI / 3),
                                new Vector3d(0.4, -0.0575, -0.435), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Yellow)));
                BG.addChild(createBox("+yellowBox3", new AxisAngle4d(0, 1, 0, Math.PI / 4),
                                new Vector3d(0.425, -0.0575, -0.34), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Yellow)));
                BG.addChild(createBox("+yellowBox4", new AxisAngle4d(0, -1, 0, Math.PI / 6),
                                new Vector3d(0.08, -0.0575, 0.85), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Yellow)));

                // legend
                BG.addChild(createBox("+redBoxLegend", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.175, 0.04, -0.695),
                                0.1f, 0.1f, 0.1f, 0.05f, LoadObject.obj_Appearance(Red)));
                BG.addChild(createBox("+greenBoxLegend", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.125, 0.04, -0.695), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Green)));
                BG.addChild(createBox("+blueBoxLegend", new AxisAngle4d(0, 0, 0, 0), new Vector3d(-0.075, 0.04, -0.695),
                                0.1f, 0.1f, 0.1f, 0.05f, LoadObject.obj_Appearance(Blue)));
                BG.addChild(createBox("+yellowBoxLegend", new AxisAngle4d(0, 0, 0, 0),
                                new Vector3d(-0.025, 0.04, -0.695), 0.1f, 0.1f, 0.1f, 0.05f,
                                LoadObject.obj_Appearance(Yellow)));

                return BG;
        }

        public static RotationInterpolator rotate_Behavior(int r_num, TransformGroup rotTG, Transform3D yAxis) {
                rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                Alpha rotationAlpha = new Alpha(-1, r_num);
                RotationInterpolator rot_beh = new RotationInterpolator(
                                rotationAlpha, rotTG, yAxis, 0.0f, (float) Math.PI * 2.0f);
                rot_beh.setSchedulingBounds(new BoundingSphere(new Point3d(), 100.0));
                return rot_beh;
        }
}
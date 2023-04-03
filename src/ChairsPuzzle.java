import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Font3D;
import org.jogamp.java3d.FontExtrusion;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Text3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.AxisAngle4d;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import java.awt.Font;

public class ChairsPuzzle {
    private static boolean firstChair = false;
    private static boolean secondChair = false;
    private static boolean thirdChair = false;
    private static boolean unlocked = false;
    private static boolean isUsable = false;

    public static void setUsable(boolean usable) {
        isUsable = usable;
    }

    public static void rotateChair(TransformGroup chairTG) {
        double angle = (double) chairTG.getUserData();

        if (unlocked)
            return;

        if (isUsable) {
            switch (chairTG.getName().charAt(chairTG.getName().length() - 1)) {
                case '1':
                    if (angle == Math.PI) { // North-East
                        System.out.println("Chair 1 is now in the correct position");
                        firstChair = true;
                    } else
                        firstChair = false;
                    break;
                case '2':
                    if (angle == Math.PI * 3 / 2) { // North-West
                        System.out.println("Chair 2 is now in the correct position");
                        secondChair = true;
                    } else
                        secondChair = false;
                    break;
                case '3':
                    if (angle == Math.PI / 2) { // South-East
                        System.out.println("Chair 3 is now in the correct position");
                        thirdChair = true;
                    } else
                        thirdChair = false;
                    break;
                default:
                    break;
            }

            if (firstChair && secondChair && thirdChair) {
                unlocked = true;
                System.out.println("All chairs are in the correct position");
                CreateObjects.roomBG.addChild(createTextObj("54", CreateObjects.White, new Vector3d(-13.8, 2.3, -13.73)));
                CreateObjects.roomBG.addChild(createTextObj("33", CreateObjects.White, new Vector3d(-3.9, 2.3, -13.73)));
                CreateObjects.roomBG.addChild(createTextObj("76", CreateObjects.White, new Vector3d(6.1, 2.3, -13.73)));
                Sounds.playSound(Sounds.successSound);
            }
        }

        angle += Math.PI / 4;
        angle %= 2 * Math.PI;
        chairTG.setUserData(angle);
        Transform3D chairTransform = new Transform3D();
        chairTG.getTransform(chairTransform);
        chairTransform.setRotation(new AxisAngle4d(0, 1, 0, angle));
        chairTG.setTransform(chairTransform);
    }

    protected static BranchGroup createTextObj(String text, Color3f color, Vector3d position) {
        Font textFont = new Font("Arial", Font.PLAIN, 1);
        Font3D font3D = new Font3D(textFont, new FontExtrusion());
        Text3D text3D = new Text3D(font3D, text);
        Shape3D shape3D = new Shape3D(text3D, createApp(color));

        Transform3D translation = new Transform3D();
        translation.setTranslation(position);

        Transform3D scaler = new Transform3D();
        scaler.setScale(0.03);

        Transform3D rotator = new Transform3D();
        rotator.rotY(Math.PI / 2);

        Transform3D transform = new Transform3D();

        transform.mul(scaler, rotator);
        transform.mul(translation);

        TransformGroup textTG = new TransformGroup(transform);
        textTG.addChild(shape3D);

        BranchGroup textBG = new BranchGroup();
        textBG.setCapability(BranchGroup.ALLOW_DETACH);
        textBG.addChild(textTG);
        return textBG;
    }

    public static Appearance createApp(Color3f m_clr) {
        Appearance app = new Appearance();
        Material mat = new Material();
        mat.setDiffuseColor(m_clr);
        mat.setAmbientColor(m_clr);
        mat.setSpecularColor(m_clr);
        mat.setShininess(1.0f);
        app.setMaterial(mat);
        return app;
    }
}

import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.java3d.Font3D;
import org.jogamp.java3d.FontExtrusion;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Text3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import java.awt.Font;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ComputerPuzzle extends Puzzle {
    private EscapeRoom escapeRoom;
    private ChairsPuzzle chairsPuzzle;
    private TransformGroup textTG;
    private int key = 2534;
    private int passcode = 0;
    private String text = "0000";
    private Color3f white = new Color3f(1, 1, 1);
    private Color3f red = new Color3f(1, 0, 0);
    private Color3f green = new Color3f(0, 1, 0);

    public ComputerPuzzle(EscapeRoom escapeRoom, ChairsPuzzle chairsPuzzle) {
        this.escapeRoom = escapeRoom;
        this.chairsPuzzle = chairsPuzzle;
        setUsable(true);
        Transform3D translation = new Transform3D();
        translation.setTranslation(new Vector3d(-26, -2.5, -43.2));

        Transform3D scaler = new Transform3D();
        scaler.setScale(0.01);

        Transform3D rotator = new Transform3D();
        rotator.rotY(-Math.PI / 2);

        Transform3D transform = new Transform3D();

        transform.mul(scaler, rotator);
        transform.mul(translation);

        textTG = new TransformGroup(transform);
        textTG.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        textTG.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        textTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

        textTG.addChild(createTextObj(text, white));
    }

    public Node positionTextObj() {
        return textTG;
    }

    public void addDigit(int digit) {
        if (isUnlocked() || !isUsable())
            return;

        if (escapeRoom.getCamera().distance(getDistance()) < 2) {
            passcode = passcode * 10 + digit;

            if (passcode < 1000) {
                Sounds.playSound(Sounds.keyboardSound);
                setText(formatText(), white);
                return;
            }
            if (passcode == key) {
                Sounds.playSound(Sounds.successSound);
                setText(formatText(), green);
                unlock();
                chairsPuzzle.setUsable(true);
                getCreateObjects().createTVClues();
            } else
                wrongPasscode();
        }
    }

    private Point3d getDistance() {
        Transform3D transform = new Transform3D();
        textTG.getTransform(transform);
        double[] matrix = new double[16];
        transform.get(matrix);
        return new Point3d(matrix[3] + 2, matrix[7], matrix[11] - 2);
    }

    private void setText(String newText, Color3f color) {
        text = newText;
        textTG.removeChild(0);
        textTG.addChild(createTextObj(text, color));
    }

    private void wrongPasscode() {
        setUsable(false);
        setText(formatText(), red);
        Sounds.playSound(Sounds.wrongSound);
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                passcode = 0;
                setText(formatText(), white);
                setUsable(true);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private String formatText() {
        return String.format("%04d", passcode);
    }

    protected BranchGroup createTextObj(String text, Color3f color) {
        Font textFont = new Font("Arial", Font.PLAIN, 1);
        Font3D font3D = new Font3D(textFont, new FontExtrusion());
        Text3D text3D = new Text3D(font3D, text);
        Shape3D shape3D = new Shape3D(text3D, createApp(color));
        BranchGroup textBG = new BranchGroup();
        textBG.setCapability(BranchGroup.ALLOW_DETACH);
        textBG.addChild(shape3D);
        return textBG;
    }

    public Appearance createApp(Color3f m_clr) {
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

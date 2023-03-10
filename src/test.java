
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Box;
import org.jogamp.java3d.utils.universe.SimpleUniverse;

import com.jogamp.newt.event.MouseEvent;

public class test extends JFrame implements MouseMotionListener {

    private Canvas3D canvas;
    private SimpleUniverse universe;
    private TransformGroup viewTransform;

    private int lastX, lastY;

    public test() {
        super("Cube Universe");

        // Create a canvas for 3D rendering
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.addMouseMotionListener((MouseMotionListener) this);
        getContentPane().add(canvas);

        // Create a simple universe with default values
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();

        // Create a cube
        Box box = new Box(0.5f, 0.5f, 0.5f, Box.GENERATE_NORMALS, null);

        // Create a transform group to move the cube
        TransformGroup objTrans = new TransformGroup();
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans.addChild(box);

        // Add the cube to the universe
        BranchGroup objRoot = new BranchGroup();
        objRoot.addChild(objTrans);
        universe.addBranchGraph(objRoot);

        // Create a transform group to control the camera position
        viewTransform = universe.getViewingPlatform().getViewPlatformTransform();

        // Set the window properties
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void mouseDragged(MouseEvent e) {
        // Get the mouse movement since the last event
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;

        // Update the camera position based on the mouse movement
        Transform3D t3d = new Transform3D();
        viewTransform.getTransform(t3d);
        t3d.rotY(-dx * Math.PI / 180);
        t3d.rotX(dy * Math.PI / 180);
        viewTransform.setTransform(t3d);

        // Save the current mouse position for the next event
        lastX = e.getX();
        lastY = e.getY();
    }

    public void mouseMoved(MouseEvent e) {
        // Save the current mouse position for the next event
        lastX = e.getX();
        lastY = e.getY();
    }

    public static void main(String[] args) {
        new test();
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseDragged'");
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
    }
}

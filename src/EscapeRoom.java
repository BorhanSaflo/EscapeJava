import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;
import java.io.IOException;

public class EscapeRoom extends JPanel {

	private static JFrame frame;
	private GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	private Canvas3D canvas = new Canvas3D(config);
	private SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
	private double direction = 0.0;
	private Point3d camera = new Point3d(0, 1.0, 0); // define the point where the eye is
	private Point3d centerPoint = new Point3d(1.0, 1.0, 0.0); // define the point where the eye is looking
	private final BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 1000.0);

	public EscapeRoom() {
		BranchGroup sceneBG = createScene();
		sceneBG.compile(); // optimize the BranchGroup
		su.addBranchGraph(sceneBG); // attach the scene to SimpleUniverse

		// Create a JFrame to contain the Canvas3D and put the Canvas3D into it.
		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(1920, 1080); // set the size of the JFrame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		// Create a new blank cursor.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");
		frame.getContentPane().setCursor(blankCursor); // Set the blank cursor to the JFrame.

		// Add the key and mouse controls
		Controls controls = new Controls(camera, centerPoint, direction, canvas, this);
		su.getCanvas().addKeyListener(controls);
		su.getCanvas().addMouseMotionListener(controls);
		su.getViewer().getView().setFieldOfView(1.5);
		updateViewer();

		Thread thread = new Thread(controls);
		thread.start();
	}

	private TransformGroup viewTransform = su.getViewingPlatform().getViewPlatformTransform();
	private Vector3d upDir = new Vector3d(0, 1, 0); // define camera's up direction
	private Transform3D viewTM = new Transform3D();

	public void updateViewer() {
		viewTM.lookAt(camera, centerPoint, upDir);
		if (Double.isNaN(viewTM.determinant())) {
			camera.x += 0.001f;
			camera.y += 0.001f;
			camera.z += 0.001f;
			viewTM.lookAt(camera, centerPoint, upDir);
		}
		viewTM.invert();
		viewTransform.setTransform(viewTM);
	}

	public BranchGroup addLights(Color3f clr, int p_num) {
		BranchGroup lightBG = new BranchGroup();
		Point3f atn = new Point3f(0.5f, 0.0f, 0.0f);
		PointLight ptLight;
		float adjt = 1f;
		for (int i = 0; (i < p_num) && (i < 2); i++) {
			if (i > 0)
				adjt = -1f;
			ptLight = new PointLight(clr, new Point3f(3.0f * adjt, 1.0f, 3.0f * adjt), atn);
			ptLight.setInfluencingBounds(hundredBS);
			lightBG.addChild(ptLight);
		}
		return lightBG;
	}

	public BranchGroup createScene() {
		BranchGroup roomBG = LoadObject.loadObject("objects/room.obj");
		BranchGroup sceneBG = new BranchGroup();
		Transform3D scale = new Transform3D();
		scale.setScale(10);
		TransformGroup scaleTG = new TransformGroup(scale);
		scaleTG.addChild(roomBG);
		sceneBG.addChild(scaleTG);
		sceneBG.addChild(addLights(new Color3f(1.0f, 1.0f, 1.0f), 1));

		return sceneBG;
	}

	public static void main(String[] args) throws IOException {
		frame = new JFrame("Escape Java");
		frame.getContentPane().add(new EscapeRoom());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

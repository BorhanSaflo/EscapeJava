import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;

public class EscapeRoom extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	Canvas3D canvas = new Canvas3D(config);
	SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
	static double direction = 0.0, speed = 1.0, radius = 1.0;
	// calc based on direction
	static double eyeX = 0, eyeY = 1.0, eyeZ = 0, lookX = radius, lookY = 1, lookZ = radius;
	Point3d eye = new Point3d(eyeX, eyeY, eyeZ); // define the point where the eye is
	static Point3d center = new Point3d(lookX, lookY, lookZ); // define the point where the eye is looking

	public final static Color3f Red = new Color3f(1.0f, 0.0f, 0.0f);
	public final static Color3f Green = new Color3f(0.0f, 1.0f, 0.0f);
	public final static Color3f Blue = new Color3f(0.0f, 0.0f, 1.0f);
	public final static Color3f Yellow = new Color3f(1.0f, 1.0f, 0.0f);
	public final static Color3f Cyan = new Color3f(0.0f, 1.0f, 1.0f);
	public final static Color3f Orange = new Color3f(1.0f, 0.5f, 0.0f);
	public final static Color3f Magenta = new Color3f(1.0f, 0.0f, 1.0f);
	public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
	public final static Color3f Grey = new Color3f(0.35f, 0.35f, 0.35f);
	public final static Color3f Black = new Color3f(0.0f, 0.0f, 0.0f);
	public final static Color3f[] clr_list = { Blue, Green, Red, Yellow,
			Cyan, Orange, Magenta, Grey };
	public final static int clr_num = 8;

	public final static BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 1000.0);

	/* NOTE: Keep the constructor for each of the labs and assignments */
	public EscapeRoom(BranchGroup sceneBG) {

		defineViewer(su, eye); // set the viewer's location

		sceneBG.compile(); // optimize the BranchGroup
		su.addBranchGraph(sceneBG); // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(1920, 1080); // set the size of the JFrame
		frame.setVisible(true);
		su.getCanvas().addKeyListener(this);
		orbitControls(canvas);
	}

	/* a function to add two point lights at the opposite locations of the scene */
	public static BranchGroup addLights(Color3f clr, int p_num) {
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

	private static Scene loadShape(String obj_name) {
		ObjectFile f = new ObjectFile(ObjectFile.RESIZE, (float) (60 * Math.PI /
				180.0));
		Scene s = null;
		try { // load object's definition file to 's'
			s = f.load("objects/" + obj_name + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println(e);
			System.exit(1);
		} catch (ParsingErrorException e) {
			System.err.println(e);
			System.exit(1);
		} catch (IncorrectFormatException e) {
			System.err.println(e);
			System.exit(1);
		}
		return s; // return the object shape in 's'
	}

	/* a function to position viewer to 'eye' location */
	public static void defineViewer(SimpleUniverse simple_U, Point3d eye) {
		TransformGroup viewTransform = simple_U.getViewingPlatform().getViewPlatformTransform();
		Vector3d up = new Vector3d(0, 1, 0); // define camera's up direction
		Transform3D view_TM = new Transform3D();
		view_TM.lookAt(eye, center, up);
		if (Double.compare(Double.NaN, view_TM.determinant()) == 0) {
			eye = new Point3d(eye.x + 0.001F, eye.y + 0.001F, eye.z + 0.001F);
			view_TM.lookAt(eye, center, up);
		}
		view_TM.invert();
		viewTransform.setTransform(view_TM); // set the TransformGroup of ViewingPlatform
	}

	private void orbitControls(Canvas3D c) {
		OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ROTATE);
		BoundingSphere bounds = new BoundingSphere(new Point3d(), 10.0);
		orbit.setSchedulingBounds(bounds);
		ViewingPlatform vp = su.getViewingPlatform();
		vp.setViewPlatformBehavior(orbit);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				lookY += speed * 0.1;
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
			case KeyEvent.VK_DOWN:
				lookY -= speed * 0.1;
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
			case KeyEvent.VK_RIGHT:
				direction += 5;
				if (direction > 360) {
					direction = 0;
				}
				lookX = eye.x + Math.cos(Math.toRadians(direction)) * radius;
				lookZ = eye.z + Math.sin(Math.toRadians(direction)) * radius;
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
			case KeyEvent.VK_LEFT:
				direction -= 5;
				if (direction < 0) {
					direction = 360;
				}
				lookX = eye.x + Math.cos(Math.toRadians(direction)) * radius;
				lookZ = eye.z + Math.sin(Math.toRadians(direction)) * radius;
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
			case KeyEvent.VK_W:
				double directionRadians = Math.toRadians(direction % 360.0);
				double x = Math.cos(directionRadians) * speed;
				double z = Math.sin(directionRadians) * speed;

				eyeX += x;
				eyeZ += z;

				lookX += x;
				lookZ += z;

				eye = new Point3d(eyeX, eyeY, eyeZ);
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
			case KeyEvent.VK_S:
				directionRadians = Math.toRadians(direction % 360.0);
				x = Math.cos(directionRadians) * speed;
				z = Math.sin(directionRadians) * speed;

				eyeX -= x;
				eyeZ -= z;

				lookX -= x;
				lookZ -= z;

				eye = new Point3d(eyeX, eyeY, eyeZ);
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
			case KeyEvent.VK_D:
				directionRadians = Math.toRadians(direction % 360.0);
				x = Math.cos(directionRadians) * speed;
				z = Math.sin(directionRadians) * speed;

				eyeX -= z;
				eyeZ += x;

				lookX -= z;
				lookZ += x;

				eye = new Point3d(eyeX, eyeY, eyeZ);
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
			case KeyEvent.VK_A:
				directionRadians = Math.toRadians(direction % 360.0);
				x = Math.cos(directionRadians) * speed;
				z = Math.sin(directionRadians) * speed;

				eyeX += z;
				eyeZ -= x;

				lookX += z;
				lookZ -= x;

				eye = new Point3d(eyeX, eyeY, eyeZ);
				center = new Point3d(lookX, lookY, lookZ);
				defineViewer(su, eye);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/* a function to build the content branch */
	public static BranchGroup createScene() {
		BranchGroup sceneBG = new BranchGroup();

		BranchGroup objBG = loadShape("room").getSceneGroup(); // load external
		// object to 'objBG'

		Transform3D scale = new Transform3D();
		scale.setScale(7);
		TransformGroup scaleTG = new TransformGroup(scale);
		scaleTG.addChild(objBG);
		sceneBG.addChild(scaleTG);
		sceneBG.addChild(addLights(White, 1));

		return sceneBG;
	}

	public static void main(String[] args) {
		frame = new JFrame("Escape Java"); // NOTE: change XY to student's initials
		frame.getContentPane().add(new EscapeRoom(createScene())); // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

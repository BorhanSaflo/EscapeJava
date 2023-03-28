import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;
import java.io.IOException;

public class EscapeRoom extends JPanel {

	public enum GameState {
		START, PLAYING, PAUSED, GAMEOVER, FOCUSED
	}

	public static GameState gameState = GameState.START;
	public static BranchGroup sceneBG;

	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private GameCanvas canvas = new GameCanvas();
	private SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
	private double direction = 0.0;
	private static Point3d camera = new Point3d(0, 0.5, 0); // define the point where the eye is
	private Point3d centerPoint = new Point3d(1.0, 0.5, 0.0); // define the point where the eye is looking
	private final BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 1000.0);
	private Controls controls = new Controls(camera, centerPoint, direction, canvas, this);
	private TransformGroup viewTransform = su.getViewingPlatform().getViewPlatformTransform();
	private Vector3d upDir = new Vector3d(0, 1, 0); // define camera's up direction
	private Transform3D viewTM = new Transform3D();

	public EscapeRoom() throws IOException {
		sceneBG = createScene();
		sceneBG.compile(); // optimize the BranchGroup
		su.addBranchGraph(sceneBG); // attach the scene to SimpleUniverse

		StartScreen startScreen = new StartScreen(this);

		setLayout(new BorderLayout());
		add("Center", startScreen);

		// Add the key and mouse controls
		su.getCanvas().addKeyListener(controls);
		su.getCanvas().addMouseListener(controls);
		su.getCanvas().addMouseMotionListener(controls);
		su.getViewer().getView().setFieldOfView(1.5);
		Sounds.enableAudio(su);
		updateViewer();
		
		frame.setSize(1920, 1080);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		Thread thread = new Thread(controls);
		thread.start();
	}

	public void startGame() {
		removeAll();
		add("Center", canvas);
		canvas.requestFocus();
		frame.validate();

		// Hide the cursor
		controls.setCursorVisible(frame, false);

		gameState = GameState.PLAYING;
	}

	public boolean isPlaying() {
		return gameState == GameState.PLAYING;
	}

	public void togglePause() {
		if (gameState == GameState.PLAYING) {
			gameState = GameState.PAUSED;
			controls.setCursorVisible(frame, true);
			getCoords();
		} else if (gameState == GameState.PAUSED) {
			controls.resetMouse();
			gameState = GameState.PLAYING;
			controls.setCursorVisible(frame, false);
		}
		canvas.togglePause(gameState);
	}

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

	public BranchGroup addLights(Color3f clr) {
		BranchGroup lightBG = new BranchGroup();
		Point3f atn = new Point3f(0.5f, 0.0f, 0.0f);
		PointLight ptLight;

		for(int i = 0; i < 2; i++)
			for (int j = 0; j < 4; j++) {
				ptLight = new PointLight(clr, new Point3f(-2+i*4, 5, -4+j*2.5f), atn);
				ptLight.setInfluencingBounds(hundredBS);
				lightBG.addChild(ptLight);
			}

		return lightBG;
	}

	public BranchGroup createScene() {
		BranchGroup sceneBG = new BranchGroup();
		Transform3D scale = new Transform3D();
		scale.setScale(10);
		TransformGroup scaleTG = new TransformGroup(scale);

		scaleTG.addChild(createObjects.room());
		sceneBG.addChild(scaleTG);
		sceneBG.addChild(addLights(new Color3f(0.1f, 0.1f, 0.1f)));
		sceneBG.addChild(Sounds.bkgdSound());

		return sceneBG;
	}

	public static Point3d getCamera() {
		return camera;
	}

	public static void getCoords() {
		System.out.println("Camera: " + camera.x + ", " + camera.y + ", " + camera.z);
		
		Point3d temp = new Point3d();

		Vector3d translation = new Vector3d(camera.x, camera.y, camera.z);
		AxisAngle4d rotation = new AxisAngle4d(0, 0, 0, Math.PI / 2);
		Transform3D transform = new Transform3D();
        transform.setScale(0.05);
		transform.setTranslation(translation);
		transform.setRotation(rotation);

		transform.transform(temp);

		System.out.println(temp.toString());

		System.out.println(transform.toString());
	}

	public static void main(String[] args) throws IOException {
		frame = new JFrame("Escape Java");
		frame.getContentPane().add(new EscapeRoom());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;
import java.io.IOException;

public class EscapeRoom extends JPanel {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;

	public enum GameState {
		START, PLAYING, PAUSED, FOCUSED, PICKUP, ESCAPED
	}

	public static GameState gameState = GameState.START;
	public BranchGroup sceneBG;
	private BranchGroup lightBG = new BranchGroup();

	// Puzzle objects
	private ChairsPuzzle chairsPuzzle = new ChairsPuzzle();
	private ComputerPuzzle computerPuzzle = new ComputerPuzzle(this, chairsPuzzle);
	private LockPuzzle lockPuzzle = new LockPuzzle();

	private CreateObjects createObjects = new CreateObjects(computerPuzzle, chairsPuzzle, lockPuzzle);

	private boolean lightsActive = false;
	private GameCanvas canvas = new GameCanvas();
	private SimpleUniverse su = new SimpleUniverse(canvas); // create a SimpleUniverse
	private double direction = 0.0;
	private Point3d camera = new Point3d(0, 0.5, 0); // define the point where the eye is
	private Point3d centerPoint = new Point3d(1.0, 0.5, 0.0); // define the point where the eye is looking
	private final BoundingSphere hundredBS = new BoundingSphere(new Point3d(), 1000.0);
	private Controls controls = new Controls(camera, centerPoint, direction, canvas, this, computerPuzzle, chairsPuzzle,
			lockPuzzle);
	private TransformGroup viewTransform = su.getViewingPlatform().getViewPlatformTransform();
	private Vector3d upDir = new Vector3d(0, 1, 0); // define camera's up direction
	private Transform3D viewTM = new Transform3D();

	public EscapeRoom() throws IOException {
		controls.setCreateObjects(createObjects);
		computerPuzzle.setCreateObjects(createObjects);
		chairsPuzzle.setCreateObjects(createObjects);
		lockPuzzle.setCreateObjects(createObjects);
		lockPuzzle.createPuzzle();

		sceneBG = createScene();
		sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		sceneBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
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

	public void endGame(boolean escaped) {
		if (escaped) {
			gameState = GameState.ESCAPED;
			controls.setCursorVisible(frame, true);
			removeAll();
			add("Center", new WinScreen());
			frame.validate();
		}
	}

	public boolean isPlaying() {
		return gameState == GameState.PLAYING || gameState == GameState.PICKUP;
	}

	public SimpleUniverse getSU() {
		return su;
	}

	public void togglePause() {
		if (gameState == GameState.PLAYING) {
			gameState = GameState.PAUSED;
			controls.setCursorVisible(frame, true);
			// getCoords(); // for debugging
		} else if (gameState == GameState.PAUSED) {
			controls.resetMouse();
			gameState = GameState.PLAYING;
			controls.setCursorVisible(frame, false);
		}
		canvas.togglePause(gameState);
	}

	public void toggleLights() {
		if (lightsActive)
			sceneBG.removeChild(lightBG);
		else
			sceneBG.addChild(lightBG);
		lightsActive = !lightsActive;

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
		lightBG.setCapability(BranchGroup.ALLOW_DETACH);
		Point3f atn = new Point3f(0.5f, 0.0f, 0.0f);
		PointLight ptLight;

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < 3; j++) {
				ptLight = new PointLight(clr, new Point3f(-2 + i * 4, 5, -4 + j * 3.3f), atn);
				ptLight.setInfluencingBounds(hundredBS);
				lightBG.addChild(ptLight);
			}

		ptLight = new PointLight(new Color3f(0.5f, 0.5f, 0.5f), new Point3f(0, -5, 0), atn);
		ptLight.setInfluencingBounds(hundredBS);
		lightBG.addChild(ptLight);

		ptLight = new PointLight(new Color3f(0.3f, 0.3f, 0.3f), new Point3f(0, 2, 7.5f), atn);
		ptLight.setInfluencingBounds(hundredBS);
		lightBG.addChild(ptLight);

		lightsActive = true;

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

	public Point3d getCamera() {
		return camera;
	}

	public void getCoords() {
		System.out.println("Camera: " + camera.x + ", " + camera.y + ", " +
				camera.z);
	}

	public static void main(String[] args) throws IOException {
		frame = new JFrame("Escape Java");
		frame.getContentPane().add(new EscapeRoom());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
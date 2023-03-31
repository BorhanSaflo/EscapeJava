import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.vecmath.*;

public class Controls implements KeyListener, MouseListener, MouseMotionListener, Runnable {

    private TransformGroup focusedGroup;
    private Point3d camera;
    private Point3d centerPoint;
    private static double direction;
    private Canvas3D canvas;
    private EscapeRoom escapeRoom;
    private Point last = null;
    private boolean left = false;
    private boolean up = false;
    private boolean down = false;
    private boolean right = false;
    private Robot robot = null;
    private static final double ROTATION_FACTOR = 0.2;
    private static final double Y_FACTOR = 1.5;
    private static final double MIN_Y = -2;
    private static final double MAX_Y = 2;
    BufferedImage img = ImageIO.read(new File("objects/images/collision.jpg"));

    public static double direction() {
        return direction;
    }

    public Controls(Point3d camera, Point3d centerPoint, double direction,
            Canvas3D canvas, EscapeRoom escapeRoom) throws IOException {
        this.camera = camera;
        this.centerPoint = centerPoint;
        Controls.direction = direction;
        this.canvas = canvas;
        this.escapeRoom = escapeRoom;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            while (true) {
                int dx = left == right ? 0 : left ? -1 : 1;
                int dz = up == down ? 0 : up ? 1 : -1;
                if (dx != 0 || dz != 0)
                    move(dx, dz);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(25));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }

    }

    private void move(int xAxis, int zAxis) {
        if (!escapeRoom.isPlaying()) {
            return;
        }

        double speed = (zAxis == 0 || xAxis == 0) ? 0.15 : 0.1;
        double theta = Math.toRadians(direction % 360.0);
        double dx = Math.cos(theta) * speed;
        double dz = Math.sin(theta) * speed;

        double camDX = zAxis * dx - xAxis * dz;
        double camDZ = zAxis * dz + xAxis * dx;

        double y = (camera.x+camDX+3.7)/7.8, x = 1-(camera.z+camDZ+6.4)/15.2;

        
        if (x>0 && x<1 && y>0 && y<1) {
            int color = img.getRGB((int)(x*img.getWidth()), (int)(y*img.getHeight()));
            if((color&0xff) > 100) {
                camera.x += camDX;
                camera.z += camDZ;
                centerPoint.x += camDX;
                centerPoint.z += camDZ;
            }
        }

        escapeRoom.updateViewer();
        updateFocus();

        /*
        System.out.println("x - "+camera.x);
        System.out.println("y - "+camera.y);
        System.out.println("z - "+camera.z);
        */
    }

    private void turn(boolean verticalAxis, int magnitude) {
        if (!escapeRoom.isPlaying()) {
            return;
        }

        if (verticalAxis) {
            centerPoint.y += magnitude * 0.2;
        } else {
            direction = (direction + magnitude * 5) % 360.0;
            double theta = Math.toRadians(direction);
            centerPoint.x = camera.x + Math.cos(theta);
            centerPoint.z = camera.z + Math.sin(theta);
        }

        escapeRoom.updateViewer();
    }

    public void resetMouse() {
        if (robot != null) {
            robot.mouseMove(canvas.getLocationOnScreen().x + canvas.getWidth() / 2,
                    canvas.getLocationOnScreen().y + canvas.getHeight() / 2);
        }
    }

    public void setCursorVisible(JFrame frame, boolean visible) {
        if (visible) {
            frame.setCursor(null);
        } else {
            frame.setCursor(frame.getToolkit().createCustomCursor(
                    frame.getToolkit().getImage(""), new Point(0, 0), "null"));
        }
    }

    private PickResult generatePT(){
        PickTool pickTool = new PickTool(EscapeRoom.sceneBG);
		pickTool.setMode(PickTool.GEOMETRY);
		
		int x = canvas.getWidth()/2;
        int y = canvas.getHeight()/2;        // mouse coordinates
		Point3d point3d = new Point3d(), center = new Point3d();
		canvas.getPixelLocationInImagePlate(x, y, point3d);// obtain AWT pixel in ImagePlate coordinates
		canvas.getCenterEyeInImagePlate(center);           // obtain eye's position in IP coordinates
		
		Transform3D transform3D = new Transform3D();       // matrix to relate ImagePlate coordinates~
		canvas.getImagePlateToVworld(transform3D);         // to Virtual World coordinates
		transform3D.transform(point3d);                    // transform 'point3d' with 'transform3D'
		transform3D.transform(center);                     // transform 'center' with 'transform3D'
	
		Vector3d mouseVec = new Vector3d();
		mouseVec.sub(point3d, center);
		mouseVec.normalize();
		pickTool.setShapeRay(point3d, mouseVec);           // send a PickRay for intersection

        return pickTool.pickClosest();
    }

    private void pickObject() {
    	PickResult pr = generatePT();
	
		if (pr != null) {
			TransformGroup clickTG = (TransformGroup)pr.getNode(PickResult.SHAPE3D).getParent().getParent();

			switch(clickTG.getName().charAt(0)){
                case '+': focus(clickTG); break;
                case '-': unfocus(clickTG); break;
                case '@': interact(clickTG); break;
            }
			
			System.out.println(clickTG.getName()); // For debug purposes
		}
    }

    private void highlightCursor(){
        PickResult pr = generatePT();
	
		if (pr != null) {
            Shape3D clickObj = (Shape3D)pr.getNode(PickResult.SHAPE3D);
			TransformGroup clickTG = (TransformGroup)clickObj.getParent().getParent();

            if(clickTG.getName().charAt(0) == '!')
                GameCanvas.setCursorColor(Color.WHITE);
            else
                GameCanvas.setCursorColor(Color.YELLOW);
			
			//System.out.println(clickTG.getName()); // For debug purposes
		}
    }

    private void focus(TransformGroup focusTG) {
        focusedGroup = focusTG;
    	Transform3D popup = new Transform3D();
    	popup.setTranslation(new Vector3d(centerPoint.x*0.1, centerPoint.y*0.1, centerPoint.z*0.1));
		popup.setRotation(new AxisAngle4d(0, 1, 0, Math.PI - Math.toRadians(Controls.direction())));
		popup.setScale(((Transform3D)focusTG.getUserData()).getScale());
		
		focusTG.setTransform(popup);
		focusTG.setName("="+focusTG.getName().substring(1));
		EscapeRoom.gameState = EscapeRoom.GameState.FOCUSED;
    }

    private void updateFocus() {
        if(focusedGroup == null) return;

        Transform3D popup = new Transform3D();
    	popup.setTranslation(new Vector3d(centerPoint.x*0.1, centerPoint.y*0.1, centerPoint.z*0.1));
		popup.setRotation(new AxisAngle4d(0, 1, 0, Math.PI - Math.toRadians(Controls.direction())));
        popup.setScale(((Transform3D)focusedGroup.getUserData()).getScale());
		
		focusedGroup.setTransform(popup);
    }

    private void unfocus(TransformGroup focusTG) {
    	focusTG.setTransform((Transform3D)focusTG.getUserData());
    	focusTG.setName("+"+focusTG.getName().substring(1));
        focusedGroup = null;

        resetMouse();
		EscapeRoom.gameState = EscapeRoom.GameState.PLAYING;
    }

    private void interact(TransformGroup clickTG){
        if(clickTG.getName().substring(1, clickTG.getName().length()-1).equals("doorKnob")){
            RotationInterpolator rot = null;
            Transform3D t3d = new Transform3D();
            t3d.rotZ(-Math.PI/2);

            // TODO: fix coordinates and change alpha + angles

            if (clickTG.getName().equals("@doorKnob1")) {
                rot = createObjects.door1Rot;
                t3d.setTranslation(new Vector3d(-3, 0.5, -6.5));
            }
            else if (clickTG.getName().equals("@doorKnob2")){
                rot = createObjects.door2Rot;
                t3d.setTranslation(new Vector3d(-3, 0.5, -6.5));
            }

            rot.setAlpha(new Alpha(-1, 1000));
            rot.setTransformAxis(t3d);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!escapeRoom.isPlaying()) {
            return;
        }

        highlightCursor();

        Point mousePosition = e.getPoint();
        if (last == null) {
            last = mousePosition;
            return;
        }

        int dx = mousePosition.x - canvas.getWidth() / 2;
        int dy = mousePosition.y - canvas.getHeight() / 2;
        if (dx == 0 && dy == 0) {
            return;
        }

        double normalizedY = dy / (canvas.getHeight() / 2.0);
        double yFactor = Math.sin(normalizedY * Math.PI / 2.0) * Y_FACTOR;
        direction += dx * ROTATION_FACTOR;
        centerPoint.x = camera.x + Math.cos(Math.toRadians(direction));
        centerPoint.z = camera.z + Math.sin(Math.toRadians(direction));
        centerPoint.y -= yFactor;

        if (centerPoint.y > MAX_Y) {
            centerPoint.y = MAX_Y;
        } else if (centerPoint.y < MIN_Y) {
            centerPoint.y = MIN_Y;
        }

        escapeRoom.updateViewer();
        resetMouse();
        updateFocus();
        last = mousePosition;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Movement
            case KeyEvent.VK_A:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_W:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_E:
                if(EscapeRoom.gameState == EscapeRoom.GameState.FOCUSED)
                    unfocus(focusedGroup);
                else if(EscapeRoom.gameState == EscapeRoom.GameState.PLAYING)
                    pickObject();
            break;
            case KeyEvent.VK_UP:
                turn(true, 1);
                break;
            case KeyEvent.VK_DOWN:
                turn(true, -1);
                break;
            case KeyEvent.VK_RIGHT:
                turn(false, 1);
                break;
            case KeyEvent.VK_LEFT:
                turn(false, -1);
                break;

            // Pause
            case KeyEvent.VK_P:
            case KeyEvent.VK_ESCAPE:
                if(EscapeRoom.gameState == EscapeRoom.GameState.FOCUSED)
                    unfocus(focusedGroup);
                else
                    escapeRoom.togglePause();
                break;

            // temporary
            case KeyEvent.VK_C:
                camera.y -= 0.2;
                break;
            case KeyEvent.VK_SPACE:
                camera.y += 0.2;
                break;
            case KeyEvent.VK_G:
                camera.y += 0.2;
                break;

            // Numbers
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
            case KeyEvent.VK_5:
            case KeyEvent.VK_6:
            case KeyEvent.VK_7:
            case KeyEvent.VK_8:
            case KeyEvent.VK_9:
            case KeyEvent.VK_0:
                int digit = e.getKeyCode() - KeyEvent.VK_0;
                computerPuzzle.addDigit(digit);
                break;

            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_W:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
	@Override
	public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

}

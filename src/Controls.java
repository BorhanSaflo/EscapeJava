import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import javax.swing.JFrame;

import java.awt.event.MouseEvent;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.*;

public class Controls implements KeyListener, MouseMotionListener, Runnable {

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
    
    public static double direction() { return direction; }

    public Controls(Point3d camera, Point3d centerPoint, double direction,
            Canvas3D canvas, EscapeRoom escapeRoom) {
        this.camera = camera;
        this.centerPoint = centerPoint;
        this.direction = direction;
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

        camera.x += camDX;
        camera.z += camDZ;
        centerPoint.x += camDX;
        centerPoint.z += camDZ;

        escapeRoom.updateViewer();
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

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!escapeRoom.isPlaying()) {
            return;
        }

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
        last = mousePosition;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
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
            case KeyEvent.VK_P:
            case KeyEvent.VK_ESCAPE:
                escapeRoom.togglePause();
                break;

            // temporary
            case KeyEvent.VK_C:
                camera.y -= 0.2;
                break;
            case KeyEvent.VK_SPACE:
                camera.y += 0.2;
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
    
}

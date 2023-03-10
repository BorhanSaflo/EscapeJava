import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;

public class Controls implements KeyListener, MouseMotionListener, Runnable {

    private SimpleUniverse su;
    private Point3d eye;
    private Point3d center;
    private double direction;
    private double speed;
    private double radius;
    private Canvas3D canvas;
    private EscapeRoom escapeRoom;
    private boolean replace = false;
    private Point last = null;

    public Controls(SimpleUniverse su, Point3d eye, Point3d center, double direction, double speed, double radius,
            Canvas3D canvas, EscapeRoom escapeRoom) {
        this.su = su;
        this.eye = eye;
        this.center = center;
        this.direction = direction;
        this.speed = speed;
        this.radius = radius;
        this.canvas = canvas;
        this.escapeRoom = escapeRoom;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        if (last != null && replace == false) {
            int dx = p.x - canvas.getWidth() / 2;
            int dy = p.y - canvas.getHeight() / 2;

            if (dx != 0 || dy != 0) {
                direction += dx * 0.2;
                if (direction > 360) {
                    direction = 0;
                } else if (direction < 0) {
                    direction = 360;
                }
                center.x = eye.x + Math.cos(Math.toRadians(direction)) * radius;
                center.z = eye.z + Math.sin(Math.toRadians(direction)) * radius;
                center.y -= dy * 0.01;
                escapeRoom.updateViewer(su, eye);
                replace = true;
            }
            replaceCursor();
        }
        last = p;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    private synchronized void replaceCursor() {
        Robot robot;
        try {
            robot = new Robot();
            robot.mouseMove(canvas.getLocationOnScreen().x + canvas.getWidth() / 2,
                    canvas.getLocationOnScreen().y + canvas.getHeight() / 2);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        replace = false;
    }

    private boolean left = false;
    private boolean up = false;
    private boolean down = false;
    private boolean right = false;

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
                center.y += speed * 0.2;
                escapeRoom.updateViewer(su, eye);
                break;
            case KeyEvent.VK_DOWN:
                center.y -= speed * 0.2;
                escapeRoom.updateViewer(su, eye);
                break;
            case KeyEvent.VK_RIGHT:
                direction += 5;
                if (direction > 360) {
                    direction = 0;
                }
                center.x = eye.x + Math.cos(Math.toRadians(direction)) * radius;
                center.z = eye.z + Math.sin(Math.toRadians(direction)) * radius;
                escapeRoom.updateViewer(su, eye);
                break;
            case KeyEvent.VK_LEFT:
                direction -= 5;
                if (direction < 0) {
                    direction = 360;
                }
                center.x = eye.x + Math.cos(Math.toRadians(direction)) * radius;
                center.z = eye.z + Math.sin(Math.toRadians(direction)) * radius;
                escapeRoom.updateViewer(su, eye);
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
    public void keyTyped(KeyEvent e) {
    }

    double dx, dz;

    void calcDirection(float speed) {
        double theta = Math.toRadians(direction % 360.0);
        dx = Math.cos(theta) * speed;
        dz = Math.sin(theta) * speed;
    }

    void moveRight() {
        eye.x -= dz;
        eye.z += dx;
        center.x -= dz;
        center.z += dx;
        escapeRoom.updateViewer(su, eye);
    }

    void moveLeft() {
        eye.x += dz;
        eye.z -= dx;
        center.x += dz;
        center.z -= dx;
        escapeRoom.updateViewer(su, eye);
    }

    void moveForward() {
        eye.x += dx;
        eye.z += dz;
        center.x += dx;
        center.z += dz;
        escapeRoom.updateViewer(su, eye);
    }

    void moveBackward() {
        eye.x -= dx;
        eye.z -= dz;
        center.x -= dx;
        center.z -= dz;
        escapeRoom.updateViewer(su, eye);
    }

    @Override
    public void run() {
        try {
            while (true) {
                int directionCode = (left ? 1 : 0) + (right ? 2 : 0) + (up ? 4 : 0) + (down ? 8 : 0);
                switch (directionCode) {
                    case 1 + 4: // left and up
                        calcDirection(0.1f);
                        moveLeft();
                        moveForward();
                        break;
                    case 1 + 8: // left and down
                        calcDirection(0.1f);
                        moveLeft();
                        moveBackward();
                        break;
                    case 2 + 4: // right and up
                        calcDirection(0.1f);
                        moveRight();
                        moveForward();
                        break;
                    case 2 + 8: // right and down
                        calcDirection(0.1f);
                        moveRight();
                        moveBackward();
                        break;
                    case 1: // left
                        calcDirection(0.15f);
                        moveLeft();
                        break;
                    case 2: // right
                        calcDirection(0.15f);
                        moveRight();
                        break;
                    case 4: // up
                        calcDirection(0.2f);
                        moveForward();
                        break;
                    case 8: // down
                        calcDirection(0.2f);
                        moveBackward();
                        break;
                    default: // no direction pressed, do nothing
                        break;
                }

                Thread.sleep(25);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}

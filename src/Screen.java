import java.awt.Graphics;
import javax.swing.JPanel;

public abstract class Screen extends JPanel {
    private static final long serialVersionUID = 1L;

    public Screen() {
        setFocusable(true);
        requestFocusInWindow();
    }

    public abstract void paintComponent(Graphics g);
}

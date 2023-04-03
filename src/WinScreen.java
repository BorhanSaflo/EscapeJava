import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
public class WinScreen extends JPanel {

	private static final long serialVersionUID = 1L;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("You did it!!", getWidth() / 2 - 150, getHeight() / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("You escaped the Java Lounge!", getWidth() / 2 - 150, getHeight() / 2 + 50);
    }
}
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
public class WinScreen extends Screen {

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("You did it!!", getWidth() / 2 - 150, getHeight() / 2);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("You escaped the Java Lounge!", getWidth() / 2 - 150, getHeight() / 2 + 50);
    }
}
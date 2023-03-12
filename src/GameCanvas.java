import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.utils.universe.SimpleUniverse;

import java.awt.Font;

import java.awt.Color;

public class GameCanvas extends Canvas3D {

    public GameCanvas() {
        super(SimpleUniverse.getPreferredConfiguration());
    }

    public void postRender() {
        this.getGraphics2D().setColor(new Color(0, 0, 0, 0.0f));
        this.getGraphics2D().fillRect(0, 0, getWidth(), getHeight());
        this.getGraphics2D().flush(false);
    }

    public void togglePause(EscapeRoom.GameState gameState) {
        if (gameState == EscapeRoom.GameState.PAUSED) {
            this.getGraphics2D().setColor(new Color(0, 0, 0, 0.9f));
            this.getGraphics2D().fillRect(0, 0, getWidth(), getHeight());
            this.getGraphics2D().setColor(Color.white);
            this.getGraphics2D().setFont(new Font("Arial", Font.PLAIN, 40));
            this.getGraphics2D().setFont(this.getGraphics2D().getFont().deriveFont(40f));
            this.getGraphics2D().drawString("Paused", getWidth() / 2 - 50, getHeight() / 2);
        } else {
            this.getGraphics2D().setColor(new Color(0, 0, 0, 0.0f));
            this.getGraphics2D().fillRect(0, 0, getWidth(), getHeight());
        }
        this.getGraphics2D().flush(false);
    }
}

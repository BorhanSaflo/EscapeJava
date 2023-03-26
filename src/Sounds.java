import java.net.URL;
import org.jdesktop.j3d.examples.sound.BackgroundSoundBehavior;
import org.jdesktop.j3d.examples.sound.audio.JOALMixer;
import org.jogamp.java3d.BackgroundSound;
import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.MediaContainer;
import org.jogamp.java3d.PointSound;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.Viewer;
import org.jogamp.vecmath.Point3d;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Sounds {
    public static PointSound keyboardSound = createSound("keyboard");
    public static PointSound wrongSound = createSound("wrong");
    public static PointSound successSound = createSound("success");

    public static void playSound(PointSound sound) {
        sound.setEnable(true);
        Timer timer = new Timer((int) sound.getDuration() / 4, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sound.setEnable(false);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private static URL locateSound(String fn) {
        URL url = null;
        String filename = "sounds/" + fn + ".wav";
        try {
            url = new URL("file", "localhost", filename);
        } catch (Exception e) {
            System.out.println("Can't open " + filename);
        }
        return url;
    }

    public static PointSound createSound(String name) {
        MediaContainer pointMedia = new MediaContainer(locateSound(name));
        pointMedia.setCacheEnable(true);
        PointSound pointSound = new PointSound();
        pointSound.setSoundData(pointMedia);
        pointSound.setEnable(false);
        pointSound.setInitialGain(0.5f);
        pointSound.setLoop(-1);
        pointSound.setCapability(PointSound.ALLOW_ENABLE_WRITE);
        pointSound.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
        return pointSound;
    }

    public static BackgroundSound bkgdSound() {
        BackgroundSound bgSound = new BackgroundSound();
        bgSound.setInitialGain(0.02f);
        BackgroundSoundBehavior player = new BackgroundSoundBehavior(bgSound, locateSound("background"));
        player.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
        return bgSound;
    }

    public static void enableAudio(SimpleUniverse simple_U) {
        JOALMixer mixer = null;
        Viewer viewer = simple_U.getViewer();
        viewer.getView().setBackClipDistance(20.0f);
        if (mixer == null && viewer.getView().getUserHeadToVworldEnable()) {
            mixer = new JOALMixer(viewer.getPhysicalEnvironment());
            if (!mixer.initialize()) {
                System.out.println("Open AL failed to init");
                viewer.getPhysicalEnvironment().setAudioDevice(null);
            }
        }
    }
}

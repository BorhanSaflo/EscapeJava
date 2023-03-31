import org.jogamp.java3d.TransformGroup;

public class ChairsPuzzle {
    private static double chairAngle = 0;

    public static void rotateChair(TransformGroup chairTG) {
        chairAngle += Math.PI / 4;
        chairAngle %= 2 * Math.PI;
        createObjects.rotateChair(chairAngle, chairTG);

    }

}

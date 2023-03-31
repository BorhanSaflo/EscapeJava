import org.jogamp.java3d.TransformGroup;

public class ChairsPuzzle {
    private static double chairAngle1 = 0;
    private static double chairAngle2 = 0;
    private static double chairAngle3 = 0;

    public static void rotateChair(int chair, TransformGroup chairTG) {
        switch (chair) {
            case 1:
                chairAngle1 += Math.PI / 4;
                chairAngle1 = chairAngle1 % (2 * Math.PI);
                createObjects.rotateChair(chairAngle1, chairTG);
                break;
            case 2:
                chairAngle2 += Math.PI / 4;
                chairAngle2 = chairAngle2 % (2 * Math.PI);
                createObjects.rotateChair(chairAngle2, chairTG);
                break;
            case 3:
                chairAngle3 += Math.PI / 4;
                chairAngle3 = chairAngle3 % (2 * Math.PI);
                createObjects.rotateChair(chairAngle3, chairTG);
                break;
        }
    }

}

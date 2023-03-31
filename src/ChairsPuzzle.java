import org.jogamp.java3d.TransformGroup;

public class ChairsPuzzle {
    private static boolean firstChair = false;
    private static boolean secondChair = false;
    private static boolean thirdChair = false;
    private static boolean unlocked = false;
    private static boolean isUsable = false;

    public static void setUsable(boolean usable) {
        isUsable = usable;
    }

    public static void rotateChair(TransformGroup chairTG) {
        if (unlocked || !isUsable)
            return;

        double angle = (double) chairTG.getUserData();

        switch (chairTG.getName().charAt(chairTG.getName().length() - 1)) {
            case '1':
                if (angle == Math.PI) { //North-East
                    System.out.println("Chair 1 is now in the correct position");
                    firstChair = true;
                } else
                    firstChair = false;
                break;
            case '2':
                if (angle == Math.PI * 3 / 2) { // North-West
                    System.out.println("Chair 2 is now in the correct position");
                    secondChair = true;
                } else
                    secondChair = false;
                break;
            case '3':
                if (angle == Math.PI / 2) { // South-East
                    System.out.println("Chair 3 is now in the correct position");
                    thirdChair = true;
                } else
                    thirdChair = false;
                break;
            default:
                break;
        }

        if (firstChair && secondChair && thirdChair) {
            unlocked = true;
            System.out.println("All chairs are in the correct position");
            Sounds.playSound(Sounds.successSound);
        }

        angle += Math.PI / 4;
        angle %= 2 * Math.PI;
        chairTG.setUserData(angle);
        createObjects.rotateChair(angle, chairTG);
    }

}

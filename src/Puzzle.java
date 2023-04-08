public abstract class Puzzle {
    private CreateObjects createObjects;
    private boolean isUnlocked = false;
    private boolean isUsable = false;

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void unlock() {
        isUnlocked = true;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }

    public CreateObjects getCreateObjects() {
        return createObjects;
    }

    public void setCreateObjects(CreateObjects createObjects) {
        this.createObjects = createObjects;
    }
}
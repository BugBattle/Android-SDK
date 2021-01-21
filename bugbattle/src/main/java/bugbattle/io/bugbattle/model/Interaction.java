package bugbattle.io.bugbattle.model;


public class Interaction {
    public enum INTERACTION_TYPE {
        TOUCH
    }

    private INTERACTION_TYPE interaction;
    private float x;
    private float y;
    private float offset;

    public Interaction(INTERACTION_TYPE interaction, float x, float y, float offset) {
        this.interaction = interaction;
        this.x = x;
        this.y = y;
        this.offset = offset;
    }

    public INTERACTION_TYPE getInteraction() {
        return interaction;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getOffset() {
        return offset;
    }
}

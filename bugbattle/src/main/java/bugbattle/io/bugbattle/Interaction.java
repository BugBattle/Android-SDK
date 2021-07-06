package bugbattle.io.bugbattle;

import java.util.Date;

class Interaction {
    private final float x;
    private final float y;
    private final Date offset;
    private final INTERACTIONTYPE interactiontype;

    public Interaction(float x, float y, Date offset, INTERACTIONTYPE interactiontype) {
        this.x = x;
        this.y = y;
        this.offset = offset;
        this.interactiontype = interactiontype;
    }

    public INTERACTIONTYPE getInteractiontype() {
        return interactiontype;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Date getOffset() {
        return offset;
    }
}

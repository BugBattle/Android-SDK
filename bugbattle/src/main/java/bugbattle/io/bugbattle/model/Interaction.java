package bugbattle.io.bugbattle.model;

import java.util.Date;

public class Interaction {
    private float x;
    private float y;
    private Date offset;
    private INTERACTIONTYPE interactiontype;

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

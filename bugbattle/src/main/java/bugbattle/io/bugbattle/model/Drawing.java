package bugbattle.io.bugbattle.model;

import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

/**
 * Represents a line
 */
public class Drawing {
    private List<Paint> paint;
    private List<Path> path;

    public Drawing(List<Paint> paint, List<Path> path) {
        this.paint = paint;
        this.path = path;
    }

    public List<Path> getPath() {
        return path;
    }

    public List<Paint> getPaint() {
        return paint;
    }
}

package bugbattle.io.bugbattle;

import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

/**
 * Represents a line
 */
class Drawing {
    private final List<Paint> paint;
    private final List<Path> path;

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

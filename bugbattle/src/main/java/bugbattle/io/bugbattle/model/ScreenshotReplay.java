package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;

import java.util.Date;

public class ScreenshotReplay {
    private Bitmap screenshot;
    private Date date;

    public ScreenshotReplay(Bitmap screenshot, Date date) {
        this.screenshot = screenshot;
        this.date = date;
    }

    public Bitmap getScreenshot() {
        return screenshot;
    }

    public Date getDate() {
        return date;
    }
}

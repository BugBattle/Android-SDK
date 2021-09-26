package gleap.io.gleap;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.LinkedList;

class ScreenshotReplay {
    private final Bitmap screenshot;
    private final String screenName;
    private final LinkedList<Interaction> interactions;
    private final Date date;

    public ScreenshotReplay(Bitmap screenshot, String screenName, Date date) {
        this.screenshot = screenshot;
        this.screenName = screenName;
        this.date = date;
        this.interactions = new LinkedList<>();
    }

    public void addInteraction(Interaction interaction) {
        this.interactions.add(interaction);
    }

    public LinkedList<Interaction> getInteractions() {
        return interactions;
    }

    public Bitmap getScreenshot() {
        return screenshot;
    }

    public Date getDate() {
        return date;
    }

    public String getScreenName() {
        return screenName;
    }
}

package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.LinkedList;

public class ScreenshotReplay {
    private Bitmap screenshot;
    private String screenName;
    private LinkedList<Interaction> interactions;
    private Date date;

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

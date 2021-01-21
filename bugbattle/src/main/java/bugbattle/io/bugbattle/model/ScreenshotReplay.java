package bugbattle.io.bugbattle.model;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ScreenshotReplay {
    private Bitmap screenshot;
    private String screenName;
    private Date date;
    private List<Interaction> interactions;

    public ScreenshotReplay(Bitmap screenshot, String screenName, Date date) {
        this.screenshot = screenshot;
        this.screenName = screenName;
        this.date = date;
        interactions = new LinkedList<>();
    }

    public Bitmap getScreenshot() {
        return screenshot;
    }

    public String getScreenName() {
        return screenName;
    }

    public Date getDate() {
        return date;
    }

    public void addInteraction(Interaction interaction) {
        interactions.add(interaction);
    }

    public List<Interaction> getInteractions() {
        return interactions;
    }
}

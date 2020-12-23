package bugbattle.io.bugbattle.service;

import android.app.Activity;
import android.app.Application;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

public class ScreenshotGestureDetector extends BBDetector {
    private Activity activity;

    public ScreenshotGestureDetector(Application application) {
        super(application);
    }

    public ScreenshotGestureDetector(Application application, Activity activity) {
        super(application);
        this.activity = activity;
    }

    @Override
    public void initialize() {
        resume();
    }

    @Override
    public void resume() {
        application.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver);
    }

    @Override
    public void pause() {
        application.getContentResolver().unregisterContentObserver(contentObserver);
    }

    private void startBugReporting() {
        this.takeScreenshot();
    }

    private ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            startBugReporting();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
    };
}

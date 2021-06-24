package bugbattle.io.bugbattle.service.detectors;

import android.app.Application;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import bugbattle.io.bugbattle.service.BBDetector;

public class ScreenshotGestureDetector extends BBDetector {

    public ScreenshotGestureDetector(Application application) {
        super(application);
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

package bugbattle.io.bugbattle.util;

import android.content.Context;
import android.graphics.Bitmap;

import bugbattle.io.bugbattle.model.BugBattleBug;
import bugbattle.io.bugbattle.service.http.HttpHelper;

public class SilentBugReportUtil {
    public static void createSilentBugReport(Context context, String email, String description, String severity) {
        BugBattleBug model = BugBattleBug.getInstance();
        Bitmap bitmap = ScreenshotUtil.takeScreenshot();
        model.setEmail(email);
        model.setDescription(description);

        model.setSeverity(severity);
        if (bitmap != null) {
            model.setScreenshot(bitmap);
            try {
                new HttpHelper(new SilentBugReportHTTPListener(), context).execute(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

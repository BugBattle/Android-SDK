package bugbattle.io.bugbattle;

import android.content.Context;
import android.graphics.Bitmap;

class SilentBugReportUtil {
    public static void createSilentBugReport(Context context, String email, String description, String severity) {
        BugBattleBug model = BugBattleBug.getInstance();
        Bitmap bitmap = ScreenshotUtil.takeScreenshot();
        model.setSilentBugreportEmail(email);
        model.setDescription(description);
        model.setSeverity(severity);
        if (bitmap != null) {
            model.setScreenshot(bitmap);
            try {
                new HttpHelper(new SilentBugReportHTTPListener(), context, true).execute(model);
                BugBattleBug.getInstance().setSilentBugreportEmail("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

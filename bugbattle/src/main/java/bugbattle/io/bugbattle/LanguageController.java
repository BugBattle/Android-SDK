package bugbattle.io.bugbattle;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

class LanguageController {
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode.toLowerCase());
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}

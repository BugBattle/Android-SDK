package gleap.io.gleap;

import java.net.URLEncoder;

public class GleapURLGenerator {
    public static String generateURL() {
        GleapBug bug = GleapBug.getInstance();
        GleapConfig config = GleapConfig.getInstance();
        String postfixUrl = "";
        try {
            if (bug.getEmail() != null) {
                postfixUrl += "?email=" + URLEncoder.encode(bug.getEmail(), "utf-8");
            }

            if (config.getLanguage() != null) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&lang=" + URLEncoder.encode(config.getLanguage(), "utf-8");
                } else {
                    postfixUrl += "?lang=" + URLEncoder.encode(config.getLanguage(), "utf-8");
                }
            }

            if (config.isPrivacyPolicyEnabled()) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&enableprivacypolicy=" + config.isPrivacyPolicyEnabled();
                } else {
                    postfixUrl += "?enableprivacypolicy=" + config.isPrivacyPolicyEnabled();
                }
            }

            if (!config.getPrivacyPolicyUrl().equals("")) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&privacyplicyurl=" + URLEncoder.encode(config.getPrivacyPolicyUrl(), "utf-8");
                } else {
                    postfixUrl += "?privacyplicyurl=" + URLEncoder.encode(config.getPrivacyPolicyUrl(), "utf-8");
                }
            }

            if (!config.getColor().equals("")) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&color=" + URLEncoder.encode(config.getColor().replace("#", ""), "utf-8");
                } else {
                    postfixUrl += "?color=" + URLEncoder.encode(config.getColor().replace("#", ""), "utf-8");
                }
            }

            if (config.getLogoUrl() != null && !config.getLogoUrl().equals("")) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&logourl=" + URLEncoder.encode(config.getLogoUrl(), "utf-8");
                } else {
                    postfixUrl += "?logourl=" + URLEncoder.encode(config.getLogoUrl(), "utf-8");
                }
            }

            if (!bug.getCustomerName().equals("")) {
                if (postfixUrl.length() > 0) {
                    postfixUrl += "&name=" + bug.getCustomerName();
                } else {
                    postfixUrl += "?name=" + bug.getCustomerName();
                }
            }

            if (postfixUrl.length() > 0) {
                postfixUrl += "&showpoweredby=" + config.isShowPoweredBy();
            } else {
                postfixUrl += "?showpoweredby=" + config.isShowPoweredBy();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return postfixUrl;
    }
}

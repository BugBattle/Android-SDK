package bugbattle.io.bugbattle.model;

import org.json.JSONException;
import org.json.JSONObject;

public class LanguagePack {
    private String imageview_cancel = "Cancle";
    private String imageview_next = "Next";
    private String imageview_title = "Report a bug";
    private String report_cancel = "Cancle";
    private String report_next = "Send";
    private String report_title = "Report a bug";
    private String report_email_placeholder = "Enter your Email";
    private String report_description_placeholder = "Describe your problem";
    private String report_priority_low = "low";
    private String report_priority_medium = "medium";
    private String report_priority_high = "high";

    public LanguagePack() {
    }

    public LanguagePack(JSONObject object) throws JSONException {
       imageview_cancel =  object.getString("imageview_cancel");
       imageview_next = object.getString("imageview_next");
       imageview_title =  object.getString("imageview_title");
       report_cancel  = object.getString("report_cancel");
       report_next =  object.getString("report_next");
       report_title =  object.getString("report_title");
       report_email_placeholder =  object.getString("report_email_placeholder");
       report_description_placeholder =  object.getString("report_description_placeholder");
       report_priority_low =  object.getString("report_priority_low");
       report_priority_medium =  object.getString("report_priority_medium");
       report_priority_high =  object.getString("report_priority_high");
    }

    public String getImageview_cancel() {
        return imageview_cancel;
    }

    public void setImageview_cancel(String imageview_cancel) {
        this.imageview_cancel = imageview_cancel;
    }

    public String getImageview_next() {
        return imageview_next;
    }

    public void setImageview_next(String imageview_next) {
        this.imageview_next = imageview_next;
    }

    public String getImageview_title() {
        return imageview_title;
    }

    public void setImageview_title(String imageview_title) {
        this.imageview_title = imageview_title;
    }

    public String getReport_cancel() {
        return report_cancel;
    }

    public void setReport_cancel(String report_cancel) {
        this.report_cancel = report_cancel;
    }

    public String getReport_next() {
        return report_next;
    }

    public void setReport_next(String report_next) {
        this.report_next = report_next;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
    }

    public String getReport_email_placeholder() {
        return report_email_placeholder;
    }

    public void setReport_email_placeholder(String report_email_placeholder) {
        this.report_email_placeholder = report_email_placeholder;
    }

    public String getReport_description_placeholder() {
        return report_description_placeholder;
    }

    public void setReport_description_placeholder(String report_description_placeholder) {
        this.report_description_placeholder = report_description_placeholder;
    }

    public String getReport_priority_low() {
        return report_priority_low;
    }

    public void setReport_priority_low(String report_priority_low) {
        this.report_priority_low = report_priority_low;
    }

    public String getReport_priority_medium() {
        return report_priority_medium;
    }

    public void setReport_priority_medium(String report_priority_medium) {
        this.report_priority_medium = report_priority_medium;
    }

    public String getReport_priority_high() {
        return report_priority_high;
    }

    public void setReport_priority_high(String report_priority_high) {
        this.report_priority_high = report_priority_high;
    }
}

package bugbattle.io.bugbattle.model;

import org.json.JSONObject;

import java.util.Date;

import bugbattle.io.bugbattle.util.DateUtil;

public class Networklog {
    private String url;
    private RequestType requestType;
    private JSONObject request;
    private JSONObject response;
    private int status;
    private boolean success = true;
    private int duration = 0;
    private Date date;

    public Networklog(String url, RequestType requestType, int status, int duration, JSONObject request, JSONObject response) {
        this.url = url;
        this.requestType = requestType;
        this.request = request;
        this.response = response;
        this.status = status;
        this.duration = duration;
        date = new Date();
        try {
            if (this.response == null) {
                this.response = new JSONObject();
            }
            this.response.put("status", status);
        } catch (Exception err) {
        }
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        try {
            object.put("date", DateUtil.dateToString(date));
            object.put("type", requestType.name());
            object.put("status", status);
            object.put("url", url);
            object.put("duration", duration);
            object.put("success", success);
            if (request != null) {
                object.put("request", request);
            }
            if (response != null) {
                object.put("response", response);
            }
        } catch (Exception err) {

        }
        return object;
    }
}

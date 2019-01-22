package bugbattle.io.bugbattle.helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StepsToReproduce {
    private static StepsToReproduce instance;
    private JSONArray steps = new JSONArray();
    private StepsToReproduce() {

    }

    public static StepsToReproduce getInstance() {
        if(instance == null) {
            instance = new StepsToReproduce();
        }
        return instance;
    }


    public void setStep(String type, String description) throws JSONException{
        JSONObject step = new JSONObject();
        step.put("type",type);
        step.put("description", description);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
        step.put("date", format.format(new Date()));
        steps.put(step);
    }


    public JSONArray getSteps() {
        return steps;
    }
}

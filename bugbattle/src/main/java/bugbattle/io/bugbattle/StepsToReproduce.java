package bugbattle.io.bugbattle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

 class StepsToReproduce {
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


    public void setStep(String type, String data) {
        try {
            JSONObject step = new JSONObject();
            step.put("type", type);
            step.put("data", data);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
            step.put("date", format.format(new Date()));
            steps.put(step);
        }catch (Exception e) {
            System.out.println(e);
        }
    }


    public JSONArray getSteps() {
        return steps;
    }
}

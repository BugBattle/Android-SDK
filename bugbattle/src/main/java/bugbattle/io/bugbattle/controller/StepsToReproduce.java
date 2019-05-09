package bugbattle.io.bugbattle.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import bugbattle.io.bugbattle.model.STEPTYPE;

/**
 * Add steps to add more detailed steps, beside the already collected data.
 */
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

    /**
     * Add a custom step.
     * @param type {@link STEPTYPE} or any custom tag
     * @param data additonal information
     */
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

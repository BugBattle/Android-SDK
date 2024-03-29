package gleap.io.gleap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

/**
 * Add steps to add more detailed steps, beside the already collected data.
 */
class StepsToReproduce {
    private static StepsToReproduce instance;
    private final JSONArray steps = new JSONArray();

    private StepsToReproduce() {
    }

    public static StepsToReproduce getInstance() {
        if (instance == null) {
            instance = new StepsToReproduce();
        }
        return instance;
    }

    /**
     * Add a custom step.
     *
     * @param type any custom tag
     * @param data additonal information
     */
    public void setStep(String type, String data) {
        try {
            JSONObject step = new JSONObject();
            step.put("type", type);
            step.put("data", data);
            step.put("date", new Date().toString());
            steps.put(step);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getSteps() {
        return steps;
    }
}

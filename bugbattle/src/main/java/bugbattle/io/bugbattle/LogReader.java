package bugbattle.io.bugbattle;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


 class LogReader {

    private String formatDate(String time, String date) {
        String result = "";
        String[] splittedDate = date.split("-");

        result += Calendar.getInstance().get(Calendar.YEAR)+ "-"+ splittedDate[1] + "-" + splittedDate[0] + " " + time;
        return result;
    }
    public JSONArray readLog(){
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            JSONArray log = new JSONArray();
            String line = "";
            Pattern pattern = Pattern.compile("^\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3}");
            while ((line = bufferedReader.readLine()) != null) {
                Matcher mt = pattern.matcher(line);
                if(mt.lookingAt()) {
                    String[] splittedLine = line.split(" ");
                    JSONObject object = new JSONObject();
                    object.put("date", formatDate(splittedLine[1], splittedLine[0]));
                    String text = "";
                    for(int i = 7; i < splittedLine.length; i++) {
                        text += splittedLine[i] +" ";
                    }
                    object.put("log", text);
                    log.put(object);
                }
            }
            return log;

        }
        catch (IOException e) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

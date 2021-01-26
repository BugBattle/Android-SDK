package bugbattle.io.bugbattle.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.util.DateUtil;

import static bugbattle.io.bugbattle.util.DateUtil.formatDate;

/**
 * Read the log of the application.
 */
public class LogReader {
    private enum CONSOLELOGTYPE {
        INFO, WARNING, ERROR
    }


    /**
     * Reads the stacktrace, formats the string
     *
     * @return {@link JSONArray} formatted log
     */
    public JSONArray readLog() throws ParseException {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            JSONArray log = new JSONArray();
            String line;
            Pattern pattern = Pattern.compile("^\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}.\\d{1,3}");
            TimeZone tz = TimeZone.getTimeZone("UTC");

            while ((line = bufferedReader.readLine()) != null) {
                Matcher mt = pattern.matcher(line);
                if (mt.lookingAt()) {
                    String[] splittedLine = line.split(" ");
                    String formattedDate = formatDate(splittedLine[1], splittedLine[0]);
                    if (FeedbackModel.getInstance().getStartUpDate().before(DateUtil.stringToDate(formattedDate))) {
                        JSONObject object = new JSONObject();
                        object.put("date", formattedDate);
                        object.put("priority", getConsoleLineType(splittedLine[4]));

                        StringBuilder text = new StringBuilder();
                        for (int i = 5; i < splittedLine.length; i++) {
                            text.append(splittedLine[i]).append(" ");
                        }
                        object.put("log", text.toString());
                        log.put(object);
                    }
                }
            }
            Runtime.getRuntime().exec("logcat - c");

            return log;
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getConsoleLineType(String input) {
        if (input.toLowerCase().equals("e")) {
            return "ERROR";
        }
        if (input.toLowerCase().equals("w")) {
            return "WARNING";
        }
        return "INFO";
    }
}

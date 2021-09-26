package gleap.io.gleap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Sends the report to the bugbattle dashboard.
 */
class HttpHelper extends AsyncTask<GleapBug, Void, Integer> {
    private static final String UPLOAD_IMAGE_BACKEND_URL_POSTFIX = "/uploads/sdk";
    private static final String UPLOAD_IMAGE_MULTI_BACKEND_URL_POSTFIX = "/uploads/sdksteps";
    private static final String REPORT_BUG_URL_POSTFIX = "/bugs";
    private final Context context;
    private boolean isSilentBugReport = false;
    GleapConfig bbConfig = GleapConfig.getInstance();

    private final OnHttpResponseListener listener;

    public HttpHelper(OnHttpResponseListener listener, Context context, boolean isSilentBugReport) {
        this.listener = listener;
        this.context = context;
        this.isSilentBugReport = isSilentBugReport;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Integer doInBackground(GleapBug... gleapBugs) {
        GleapBug gleapBug = gleapBugs[0];
        int httpResult = 0;
        try {
            httpResult = postFeedback(gleapBug);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return httpResult;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (GleapConfig.getInstance().getBugSentCallback() != null && !isSilentBugReport) {
            GleapConfig.getInstance().getBugSentCallback().close();
        }
        try {
            listener.onTaskComplete(result);
        } catch (GleapAlreadyInitialisedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private JSONObject uploadImage (Bitmap image) throws IOException, JSONException {
        FormDataHttpsHelper multipart = new FormDataHttpsHelper(bbConfig.getApiUrl() + UPLOAD_IMAGE_BACKEND_URL_POSTFIX, bbConfig.getSdkKey());
        File file = bitmapToFile(image);
        if (file != null) {
            multipart.addFilePart(file);
        }
        String response = multipart.finishAndUpload();
        return new JSONObject(response);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private JSONObject uploadImages (Bitmap[] images) throws IOException, JSONException {
        FormDataHttpsHelper multipart = new FormDataHttpsHelper(bbConfig.getApiUrl() + UPLOAD_IMAGE_MULTI_BACKEND_URL_POSTFIX, bbConfig.getSdkKey());
        for(Bitmap bitmap : images) {
            File file = bitmapToFile(bitmap);
            if (file != null) {
                multipart.addFilePart(file);
            }
        }
        String response = multipart.finishAndUpload();
        return new JSONObject(response);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Integer postFeedback(GleapBug bbBug) throws JSONException, IOException, ParseException {
        JSONObject responseUploadImage = uploadImage(bbBug.getScreenshot());
        URL url = new URL(bbConfig.getApiUrl() + REPORT_BUG_URL_POSTFIX);
        HttpURLConnection conn;
        if (bbConfig.getApiUrl().contains("https")) {
            conn = (HttpsURLConnection) url.openConnection();
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        conn.setRequestProperty("api-token", bbConfig.getSdkKey());
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");

        JSONObject body = new JSONObject();
        body.put("screenshotUrl", responseUploadImage.get("fileUrl"));
        body.put("replay", generateFrames());
        body.put("description", bbBug.getDescription());
        String email = "";
        if (bbBug.getSilentBugreportEmail() != null && !bbBug.getSilentBugreportEmail().equals("")) {
            email = bbBug.getSilentBugreportEmail();
        }
        if (bbBug.getEmail() != null) {
            email = bbBug.getEmail();
        }

        body.put("reportedBy", email);

        body.put("networkLogs", bbBug.getNetworklogs());
        body.put("customEventLog", bbBug.getCustomEventLog());
        PhoneMeta phoneMeta = bbBug.getPhoneMeta();

        if (phoneMeta != null) {
            body.put("metaData", phoneMeta.getJSONObj());
        }

        body.put("customData", bbBug.getCustomData());
        body.put("priority", bbBug.getSeverity());

        if (GleapConfig.getInstance().isEnableConsoleLogs()) {
            body.put("consoleLog", bbBug.getLogs());
        }

        if (bbBug.getData() != null) {
            body = concatJSONS(body, bbBug.getData());
        }

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        bbBug.setSeverity("MEDIUM");

        return conn.getResponseCode();
    }

    /**
     * Convert the Bitmap to a File in the cache
     *
     * @param bitmap image which is uploaded
     * @return return the file
     */
    private File bitmapToFile(Bitmap bitmap) {
        try {
            File outputDir = context.getCacheDir();
            File outputFile = File.createTempFile("file", ".png", outputDir);
            OutputStream
                    os
                    = new FileOutputStream(outputFile);

            os.write(getBytes(bitmap));
            os.close();
            return outputFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getBytes(Bitmap input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        input.compress(Bitmap.CompressFormat.PNG, 90, baos);
        return baos.toByteArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private JSONObject generateFrames() throws IOException, JSONException {
        JSONObject replay = new JSONObject();
        replay.put("interval", GleapBug.getInstance().getReplay().getInterval());
        JSONArray frames = generateReplayImageUrls();
        replay.put("frames", frames);
        return replay;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private JSONArray generateReplayImageUrls() throws IOException, JSONException {
        JSONArray result = new JSONArray();
        ScreenshotReplay[] replays = GleapBug.getInstance().getReplay().getScreenshots();
        List<Bitmap> bitmapList = new LinkedList<>();

        for (ScreenshotReplay replay : replays) {
            if (replay != null) {
                bitmapList.add(replay.getScreenshot());
            }
        }

        JSONObject obj = uploadImages(bitmapList.toArray(new Bitmap[bitmapList.size()]));
        JSONArray fileUrls = (JSONArray) obj.get("fileUrls");
        for (int i = 0; i < fileUrls.length(); i++) {
            JSONObject entry = new JSONObject();
            entry.put("url", fileUrls.get(i));
            entry.put("screenname", replays[i].getScreenName());
            entry.put("date", DateUtil.dateToString(replays[i].getDate()));
            entry.put("interactions", generateInteractions(replays[i]));
            result.put(entry);
        }
        GleapBug.getInstance().getReplay().reset();
        return result;
    }

    public JSONArray generateInteractions(ScreenshotReplay replay) throws JSONException {
        JSONArray result = new JSONArray();
        for (Interaction interaction : replay.getInteractions()) {
            JSONObject obj = new JSONObject();
            obj.put("x", interaction.getX());
            obj.put("y", interaction.getY());
            obj.put("date", DateUtil.dateToString(interaction.getOffset()));
            obj.put("type", interaction.getInteractiontype());
            result.put(obj);
        }
        return result;
    }

    private static JSONObject concatJSONS(JSONObject json, JSONObject obj) {
        JSONObject result = new JSONObject();

        try {
            Iterator<String> iteratorJson = json.keys();
            while (iteratorJson.hasNext()) {
                String key = iteratorJson.next();
                result.put(key, json.get(key));
            }
            Iterator<String> iteratorObj = obj.keys();
            while (iteratorObj.hasNext()) {
                String key = iteratorObj.next();
                result.put(key, obj.get(key));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        return result;
    }
}



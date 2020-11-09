package bugbattle.io.bugbattle.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import bugbattle.io.bugbattle.controller.OnHttpResponseListener;
import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.model.PhoneMeta;

/**
 * Sends the report to the bugbattle dashboard.
 */
public class HttpHelper extends AsyncTask<FeedbackModel, Void, Integer> {
    private static final String UPLOAD_IMAGE_BACKEND_URL_POSTFIX = "/uploads/sdk";
    private static final String REPORT_BUG_URL_POSTFIX = "/bugs";
    private Context context;

    private OnHttpResponseListener listener;

    public HttpHelper(OnHttpResponseListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Integer doInBackground(FeedbackModel... feedbackModels) {
        FeedbackModel feedbackModel = feedbackModels[0];
        int httpResult = 0;
        try {
            httpResult = postFeedback(feedbackModel);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResult;
    }

    @Override
    protected void onPostExecute(Integer result) {
        listener.onTaskComplete(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private JSONObject uploadImage (FeedbackModel service) throws IOException, JSONException {
        Bitmap image = service.getScreenshot();
        FormDataHttpsHelper multipart = new FormDataHttpsHelper(service.getApiUrl() + UPLOAD_IMAGE_BACKEND_URL_POSTFIX, service.getSdkKey());
        multipart.addFilePart(bitmapToFile(image));
        String response = multipart.finishAndUpload();
        return new JSONObject(response);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Integer postFeedback(FeedbackModel service) throws JSONException, IOException {
        JSONObject responseUploadImage = uploadImage(service);

        URL url = new URL(service.getApiUrl() + REPORT_BUG_URL_POSTFIX);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestProperty("api-token", service.getSdkKey());
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");

        JSONObject body = new JSONObject();
        body.put("screenshotUrl", responseUploadImage.get("fileUrl"));
        body.put("description", service.getDescription());
        body.put("reportedBy", service.getEmail());
        PhoneMeta phoneMeta = service.getPhoneMeta();

        if (phoneMeta != null) {
            body.put("metaData", phoneMeta.getJSONObj());
        }
        body.put("actionLog", service.getStepsToReproduce());
        body.put("customData", service.getCustomData());
        body.put("priority", service.getSeverity());
        body.put("consoleLog", service.getLogs());
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return conn.getResponseCode();
    }

    /**
     * Convert the Bitmap to a File in the cache
     * @param bitmap image which is uploaded
     * @return return the file
     */
    private File bitmapToFile(Bitmap bitmap) throws IOException {
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
            System.out.println(e);
        }
        return null;
    }

    private byte[] getBytes(Bitmap input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        input.compress(Bitmap.CompressFormat.PNG, 90, baos);
        return baos.toByteArray();
    }

}



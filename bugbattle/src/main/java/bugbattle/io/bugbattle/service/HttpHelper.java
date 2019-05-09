package bugbattle.io.bugbattle.service;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import bugbattle.io.bugbattle.controller.OnHttpResponseListener;
import bugbattle.io.bugbattle.model.Feedback;

/**
 * Sends the report to the bugbattle dashboard.
 */
public class HttpHelper extends AsyncTask<Feedback, Void, Integer> {
    private static final String GET_SIGNED_URL = "https://ii5xbrdd27.execute-api.eu-central-1.amazonaws.com/default/getSignedBugBattleUploadUrl";
    private static final String MONGO_STICH = "https://webhooks.mongodb-stitch.com/api/client/v2.0/app/bugbattle-xfblb/service/reportBug/incoming_webhook/reportBugWebhook?token=";

    private String imageURL;
    private String s3URL;
    private OnHttpResponseListener listener;
    public HttpHelper(OnHttpResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Feedback... feedbacks) {
        Feedback service = feedbacks[0];
        Integer httpResult = 0;
            try {
                if ((httpResult = postS3Bucket(service.getSdkKey())) == HttpURLConnection.HTTP_OK) {
                    if ((httpResult = putImage(service.getImage())) == HttpURLConnection.HTTP_OK) {

                        return postFeedback(service);
                    }
                }
            }catch (JSONException e) {

            } catch (MalformedURLException e) {

            } catch (IOException e) {

            }

        return httpResult;
    }

    @Override
     protected void onPostExecute(Integer result) {
        listener.onTaskComplete(result);
    }


    private Integer postFeedback(Feedback service) throws JSONException, MalformedURLException, IOException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(MONGO_STICH+service.getSdkKey()).openConnection();
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Type", "image/jpeg");
        conn.setRequestMethod("POST");
        JSONObject result = new JSONObject();
        result.put("screenshot", imageURL);
        result.put("description", service.getDescription());
        result.put("reportedBy", service.getEmail());
        result.put("meta", service.getPhoneMeta().getJSONObj());
        result.put("consoleLog", service.getLogs());
        result.put("actionLog", service.getStepsToReproduce());
        result.put("customData", service.getCustomData());
        result.put("severity", service.getSeverity());
        OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
        System.out.println(result.toString());
        wr.write(result.toString());
        wr.flush();

        int HttpResult = conn.getResponseCode();


        return HttpResult;
    }

    private int putImage(Bitmap imagePath) throws MalformedURLException, IOException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(s3URL).openConnection();
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Type", "image/jpeg");
        conn.setRequestMethod("PUT");

        OutputStream wr = conn.getOutputStream();

        wr.write(getBytes(imagePath));
        wr.flush();

        int HttpResult = conn.getResponseCode();

        return HttpResult;
    }

    private int postS3Bucket(String sdkKey) throws MalformedURLException, IOException, JSONException {
        HttpsURLConnection conn = (HttpsURLConnection) new URL(GET_SIGNED_URL).openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        JSONObject obj = new JSONObject();
        obj.put("apiKey", sdkKey);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(obj.toString());
        wr.flush();

        StringBuilder sb = new StringBuilder();
        int HttpResult = conn.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            JSONObject response = new JSONObject(sb.toString());
            s3URL = response.getString("url");
            imageURL = response.getString("path");
            return HttpResult;
        } else {
            return HttpResult;
        }
    }

    private  byte[] getBytes(Bitmap input) throws IOException {
        Bitmap immagex = input;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;
    }


}



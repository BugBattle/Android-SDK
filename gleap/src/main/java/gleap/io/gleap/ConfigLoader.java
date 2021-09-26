package gleap.io.gleap;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Loads the configuration from the server.
 */
class ConfigLoader extends AsyncTask<GleapBug, Void, Integer> {
    private final String httpsUrl = "https://widget.bugbattle.io/appwidget/" + GleapConfig.getInstance().getSdkKey() + "/config?s=android";
    private final OnHttpResponseListener listener;

    public ConfigLoader(OnHttpResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Integer result) {
        try {
            listener.onTaskComplete(result);
        } catch (GleapAlreadyInitialisedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Integer doInBackground(GleapBug... gleapBugs) {
        URL url;
        try {
            url = new URL(httpsUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.connect();
            readResponse(con);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 200;
    }

    private void readResponse(HttpsURLConnection con) {
        if (con != null) {

            try {
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                String input;
                JSONObject result = null;
                while ((input = br.readLine()) != null) {
                    result = new JSONObject(input);
                }
                br.close();

                if (result != null) {
                    GleapConfig.getInstance().initConfig(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

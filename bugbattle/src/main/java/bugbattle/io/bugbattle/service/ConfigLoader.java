package bugbattle.io.bugbattle.service;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import bugbattle.io.bugbattle.controller.OnHttpResponseListener;
import bugbattle.io.bugbattle.exceptions.BugBattleAlreadyInitialisedException;
import bugbattle.io.bugbattle.model.BugBattleBug;
import bugbattle.io.bugbattle.model.BugBattleConfig;

/**
 * Loads the configuration from the server.
 */
public class ConfigLoader extends AsyncTask<BugBattleBug, Void, Integer> {
    private String httpsUrl = "https://widget.bugbattle.io/appwidget/" + BugBattleConfig.getInstance().getSdkKey() + "/config?s=android";
    private OnHttpResponseListener listener;

    public ConfigLoader(OnHttpResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Integer result) {
        try {
            listener.onTaskComplete(result);
        } catch (BugBattleAlreadyInitialisedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Integer doInBackground(BugBattleBug... bugBattleBugs) {
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
                    BugBattleConfig.getInstance().initConfig(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

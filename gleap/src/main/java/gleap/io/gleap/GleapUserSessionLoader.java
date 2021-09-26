package gleap.io.gleap;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;

public class GleapUserSessionLoader  extends AsyncTask<Void, Void, Integer> {
    private static final String httpsUrl = "https://api.gleap.dev/sessions/start/";

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            URL url = new URL(httpsUrl);
            HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Api-Token", GleapConfig.getInstance().getSdkKey());
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            UserSession userSession = UserSessionController.getInstance().getUserSession();
            if(userSession.getType().equals("GUEST")){
                conn.setRequestProperty("Guest-Id", userSession.getId());
                conn.setRequestProperty("Guest-Hash", userSession.getHash());
            }

            GleapUserSession gleapUserSession = UserSessionController.getInstance().getGleapUserSession();
            JSONObject jsonObject = new JSONObject();
            if(gleapUserSession != null) {
                if (gleapUserSession.getUserHash() != null) {
                    conn.setRequestProperty("User-Hash", gleapUserSession.getUserHash());
                }
                if (gleapUserSession.getUserId() != null) {
                    conn.setRequestProperty("User-Id", gleapUserSession.getUserId());
                }
                try {

                    jsonObject.put("email", gleapUserSession.getEmail());
                    jsonObject.put("name", gleapUserSession.getName());
                }catch (Exception ex){

                }
            }

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                JSONObject result = null;
                String input;
                while ((input = br.readLine()) != null) {
                    result = new JSONObject(input);
                }

                String id = null;
                String hash = null;
                String type = null;

                if(result.has("id")) {
                    id = result.getString("id");
                }

                if(result.has("hash")) {
                    hash = result.getString("hash");
                }

                if(result.has("type")) {
                    type = result.getString("type");
                }

                if(id != null && hash != null && type != null) {
                    UserSessionController.getInstance().mergeUserSession(id, hash, type);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

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

package bugbattle.io.bugbattle;

import org.json.JSONObject;

class BugBattleHttpInterceptor {

    /**
     * Log Http calls sent from the device. Call this function at the end of your request.
     * Request and Response can be null
     *
     * @param urlConnection url the request sent to
     * @param requestType   type of request. (GET, POST, PUT, DELETE, PATCH)
     * @param status        http status code
     * @param duration      duration in milliseconds
     * @param request       JSON  Object including important informations of the request. Recommanded:
     *                      headers, payload
     * @param response      JSON  Object including important informations of the response. Recommanded:
     *                      headers, payload, body
     */
    public static void log(String urlConnection, RequestType requestType, int status, int duration, JSONObject request, JSONObject response) {
        Networklog networklog = new Networklog(urlConnection, requestType, status, duration, request, response);
        BugBattleBug.getInstance().addRequest(networklog);
    }
}

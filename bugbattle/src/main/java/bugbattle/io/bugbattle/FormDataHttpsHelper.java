package bugbattle.io.bugbattle;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Upload the image as form-data
 */
class FormDataHttpsHelper {
    private final HttpURLConnection httpConn;
    private final DataOutputStream request;
    private final String boundary = "BBBOUNDARY";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    /**
     * This constructor initializes a new HTTPS POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL bugbattle url
     * @param apiToken token for the project
     */
    public FormDataHttpsHelper(String requestURL, String apiToken)
            throws IOException {
        URL url = new URL(requestURL);
        if (requestURL.contains("https")) {
            httpConn = (HttpsURLConnection) url.openConnection();
        } else {
            httpConn = (HttpURLConnection) url.openConnection();
        }
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);

        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Cache-Control", "no-cache");
        httpConn.setRequestProperty("api-token", apiToken);
        httpConn.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + this.boundary);

        request =  new DataOutputStream(httpConn.getOutputStream());
    }

    /**
     * Adds a upload file section to the request
     * default name is file
     * @param uploadFile a File to be uploaded
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addFilePart(File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" +
                fileName + "\"" + this.crlf);
        request.writeBytes("Content-Type: " +
                "" + URLConnection.guessContentTypeFromName(fileName) + this.crlf + this.crlf);
        int size = (int) uploadFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(uploadFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.write(bytes);
        request.writeBytes(this.crlf);

    }


    public void addMultipleFiles(File[] uploadFiles) {

    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return the response of the server
     * @throws IOException error when the file cant be uploaded
     */
    public String finishAndUpload() throws IOException {
        String response;
        request.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);


        request.flush();
        request.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new
                    BufferedInputStream(httpConn.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response;
    }
}
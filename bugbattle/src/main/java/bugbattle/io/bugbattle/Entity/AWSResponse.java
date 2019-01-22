package bugbattle.io.bugbattle.Entity;

public class AWSResponse {
    private String url = "";
    private String path = "";

    public AWSResponse(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }
}

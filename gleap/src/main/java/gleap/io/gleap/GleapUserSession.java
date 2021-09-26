package gleap.io.gleap;

public class GleapUserSession {
    private String userId;
    private String userHash;
    private String name;
    private String email;

    /**
     * 
     * @param userId
     * @param userHash
     * @param name
     * @param email
     */
    public GleapUserSession(String userId, String userHash, String name, String email) {
        this.userId = userId;
        this.userHash = userHash;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserHash() {
        return userHash;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

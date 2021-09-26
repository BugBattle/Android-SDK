package gleap.io.gleap;

class UserSession {
    private String id;
    private String type;
    private String hash;


    public  UserSession(String id, String type, String hash){
        this.id = id;
        this.type = type;
        this.hash = hash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

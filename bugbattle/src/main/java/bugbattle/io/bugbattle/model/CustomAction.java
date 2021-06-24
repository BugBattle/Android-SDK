package bugbattle.io.bugbattle.model;

/**
 * Can be called from the webview
 */
public class CustomAction {
    private String name;
    private CustomActionCallback customActionCallback;

    public CustomAction(String name, CustomActionCallback customActionCallback) {
        this.name = name;
        this.customActionCallback = customActionCallback;
    }

    public void callCustomFunction() {
        if (customActionCallback != null) {
            customActionCallback.invoke();
        }
    }

    public interface CustomActionCallback {
        void invoke();
    }

    public String getName() {
        return name;
    }
}

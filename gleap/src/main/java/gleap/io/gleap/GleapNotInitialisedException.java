package gleap.io.gleap;


/**
 * Called if BugBattle is not initialised but used.
 */
public class GleapNotInitialisedException extends Exception {
    public GleapNotInitialisedException(String s) {
        super(s);
    }
}

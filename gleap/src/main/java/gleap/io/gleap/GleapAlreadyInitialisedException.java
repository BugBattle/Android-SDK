package gleap.io.gleap;


/**
 * Called if BugBattle is not initialised but used.
 */
public class GleapAlreadyInitialisedException extends Exception {
    public GleapAlreadyInitialisedException(String s) {
        super(s);
    }
}

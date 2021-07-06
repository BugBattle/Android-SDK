package bugbattle.io.bugbattle;


/**
 * Called if BugBattle is not initialised but used.
 */
public class BugBattleAlreadyInitialisedException extends Exception {
    public BugBattleAlreadyInitialisedException(String s) {
        super(s);
    }
}

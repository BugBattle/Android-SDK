package bugbattle.io.bugbattle.controller;


/**
 * Called if BugBattle is not initialised but used.
 */
public class BugBattleNotInitialisedException extends Exception {
    public BugBattleNotInitialisedException(String s) {
        super(s);
    }
}

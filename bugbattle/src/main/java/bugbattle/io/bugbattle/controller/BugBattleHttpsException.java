package bugbattle.io.bugbattle.controller;

public class BugBattleHttpsException extends Exception {
    public BugBattleHttpsException() {
        super("Only https urls are allowed.");
    }

}

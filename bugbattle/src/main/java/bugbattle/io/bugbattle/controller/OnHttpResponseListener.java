package bugbattle.io.bugbattle.controller;

import bugbattle.io.bugbattle.exceptions.BugBattleAlreadyInitialisedException;

public interface OnHttpResponseListener {
    void onTaskComplete(int httpResponse) throws BugBattleAlreadyInitialisedException;
}

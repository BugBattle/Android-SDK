package bugbattle.io.bugbattle;

interface OnHttpResponseListener {
    void onTaskComplete(int httpResponse) throws BugBattleAlreadyInitialisedException;
}

package gleap.io.gleap;

interface OnHttpResponseListener {
    void onTaskComplete(int httpResponse) throws GleapAlreadyInitialisedException;
}

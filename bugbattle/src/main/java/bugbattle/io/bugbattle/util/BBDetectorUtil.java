package bugbattle.io.bugbattle.util;

import bugbattle.io.bugbattle.model.FeedbackModel;
import bugbattle.io.bugbattle.service.BBDetector;

public class BBDetectorUtil {

    public static void resumeAllDetectors() {
        for (BBDetector detector : FeedbackModel.getInstance().getGestureDetectors()) {
            detector.resume();
        }
    }

    public static void stopAllDetectors() {
        for (BBDetector detector : FeedbackModel.getInstance().getGestureDetectors()) {
            detector.pause();
        }
    }
}

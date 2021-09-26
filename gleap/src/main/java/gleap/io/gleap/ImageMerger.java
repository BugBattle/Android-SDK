package gleap.io.gleap;

import android.graphics.Bitmap;
import android.graphics.Canvas;


/**
 * Merges the image and the drawn layer to one bitmap
 */
class ImageMerger {

    /**
     * Merges two layers
     *
     * @param background layer in the back of the merged image
     * @param overlay    layer in the front of the merged image
     * @return merged bitmap
     */
    public static Bitmap mergeImages(Bitmap background, Bitmap overlay) {
        try {
            int maxWidth = Math.max(background.getWidth(), overlay.getWidth());
            int maxHeight = Math.max(background.getHeight(), overlay.getHeight());
            Bitmap bmOverlay = Bitmap.createBitmap(maxWidth, maxHeight, background.getConfig());
            Canvas canvas = new Canvas(bmOverlay);
            canvas.drawBitmap(background, 0, 0, null);
            canvas.drawBitmap(overlay, 0, 0, null);
            return bmOverlay;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

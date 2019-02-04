package bugbattle.io.bugbattle;

import android.graphics.Bitmap;
import android.graphics.Canvas;

 class ImageProcessing {

    public static Bitmap mergeImages(Bitmap background, Bitmap overlay) {
            try
            {
                int maxWidth = (background.getWidth() > overlay.getWidth() ? background.getWidth() : overlay.getWidth());
                int maxHeight = (background.getHeight() > overlay.getHeight() ? background.getHeight() : overlay.getHeight());
                Bitmap bmOverlay = Bitmap.createBitmap(maxWidth, maxHeight,  background.getConfig());
                Canvas canvas = new Canvas(bmOverlay);
                canvas.drawBitmap(background, 0, 0, null);
                canvas.drawBitmap(overlay, 0, 0, null);
                return bmOverlay;

            } catch (Exception e)
            {
                // TODO: handle exception
                e.printStackTrace();
                return null;
            }
    }
}

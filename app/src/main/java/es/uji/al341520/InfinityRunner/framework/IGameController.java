package es.uji.al341520.InfinityRunner.framework;

import android.graphics.Bitmap;


import java.util.List;

/**
 * Created by jvilar on 29/03/16.
 * Modified by jcamen on 15/01/17.
 */
public interface IGameController {
    void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents);
    Bitmap onDrawingRequested();
}

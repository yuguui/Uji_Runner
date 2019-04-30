package es.uji.al341520.breakthewall.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by jvilar on 29/03/16.
 * Modified by jcamen on 15/01/17.
 */
public class GameView extends SurfaceView implements Runnable {
    IGameController gameController;
    SurfaceHolder holder;
    volatile boolean running = false;
    Thread renderThread = null;
    TouchHandler touchHandler;

    public GameView(Context context, IGameController gameController) {
        super(context);
        this.gameController = gameController;
        holder = getHolder();
        this.touchHandler = new TouchHandler(this);
        running = false;
    }

    public void onResume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void onPause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {}
        }
    }

    @Override
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();

        while (running) {
            if (!holder.getSurface().isValid())
                continue;

            long now = System.nanoTime();
            float deltaTime = (now - startTime) / 1000_000_000f;
            startTime = now;

            gameController.onUpdate(deltaTime, touchHandler.getTouchEvents());
            Bitmap frameBuffer = gameController.onDrawingRequested();

            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(frameBuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}

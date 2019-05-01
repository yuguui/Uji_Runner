package es.uji.al341520.breakthewall.model;

import android.graphics.Rect;
import android.os.Debug;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

public class Animation {
    private int startFrame;
    private int endFrame;
    private int frameWidth;
    private int frameHeight;
    private int maxWidth;

    private Rect sourceRect;
    private float framePeriod;
    private float frameTicker;
    private int currentFrame;
    private boolean hasRunOnce;

    public Animation(int startFrame, int endFrame, int frameWidth, int frameHeight, int maxWidth, int fps) {
        this.startFrame = startFrame;
        this.endFrame = endFrame;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.maxWidth = maxWidth;

        sourceRect = new Rect(0, 0, frameWidth, frameHeight);
        framePeriod = 1.0f / fps;
        frameTicker = 0f;
        currentFrame = -1;
        hasRunOnce = false;
    }

    public boolean hasRun() {
        return hasRunOnce;
    }

    public Rect getCurrentFrame(float deltaTime) {
        // Change the frame of the spritesheet to be shown if required
        frameTicker += deltaTime;
        if (frameTicker >= framePeriod) {
            frameTicker -= framePeriod;
            currentFrame++;
            if (currentFrame > endFrame) {
                currentFrame = startFrame;
                hasRunOnce = true;
            }
        }
        if (currentFrame < startFrame) currentFrame = startFrame;
        //update the left and right values of the source of
        //the next frame on the spritesheet
        sourceRect.left = (currentFrame - 1) * frameWidth;
        AtomicInteger count = new AtomicInteger();
        while (sourceRect.left >= maxWidth) {
            count.getAndIncrement();
            sourceRect.left -= (maxWidth);

        }
        sourceRect.top = count.get() * frameHeight;
        sourceRect.right = sourceRect.left + frameWidth - 1;
        sourceRect.bottom = sourceRect.top + frameHeight - 1;

        return sourceRect;
    }

    public void resetAnimation() {
        currentFrame = -1;
        frameTicker = 0f;
        hasRunOnce = false;
    }
}

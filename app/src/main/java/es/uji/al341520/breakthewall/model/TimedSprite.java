package es.uji.al341520.breakthewall.model;

import android.graphics.Bitmap;

public class TimedSprite extends Sprite
{
    private float timer;
    public TimedSprite(Bitmap bitmapToRender, Boolean hFlip, float x, float y, int speedX, int speedY, int sizeX, int sizeY, float timer) {
        super(bitmapToRender, hFlip, x, y, speedX, speedY, sizeX, sizeY);
        this.timer = timer;
    }

    public void SetTimer(float newValue)
    {
        this.timer = newValue;
    }

    public void UpdateTimer(float time)
    {
        this.timer -= time;
    }

    public boolean TimedOut()
    {
        if(this.timer <= 0)
        {
            return true;
        }
        return false;
    }
}

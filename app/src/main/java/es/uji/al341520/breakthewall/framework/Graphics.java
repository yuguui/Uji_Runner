package es.uji.al341520.breakthewall.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import static android.graphics.Bitmap.Config;

/**
 * Created by jvilar on 29/03/16.
 * Modified by jcamen on 15/01/17 and 09/03/19
 */
public class Graphics {
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;

    public Graphics(Context context, int width, int height) {
        this.frameBuffer = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        canvas = new Canvas(frameBuffer);
        paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(20);
        // Replace the last statement with the following ones if you want
        // to use a custom font
        /* Typeface typeface = ResourcesCompat.getFont(context, R.font.CUSTOM_FONT_HERE);
        Typeface typefaceBold = Typeface.create(typeface, Typeface.BOLD);
        paint.setTypeface(typefaceBold); */
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public Bitmap getFrameBuffer() {
        return frameBuffer;
    }

    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0xff);
    }

    public void drawRect(float x, float y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    public void drawBitmap(Bitmap bitmap, float x, float y, boolean flip) {
        if (flip) {
            Matrix flipHorizontal = new Matrix();
            flipHorizontal.setScale(-1.0f, 1.0f);
            flipHorizontal.postTranslate(bitmap.getWidth(), 0);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), flipHorizontal, true);
            canvas.drawBitmap(bmp, x, y, null);
        }
        else
            canvas.drawBitmap(bitmap, x, y, null);
    }

    public void drawAnimatedBitmap(Bitmap bitmap, Rect frameToDraw, RectF whereToDraw, boolean flip) {
        if (flip) {
            Matrix flipHorizontal = new Matrix();
            flipHorizontal.setScale(-1.0f, 1.0f);
            flipHorizontal.postTranslate(bitmap.getWidth(), 0);
            Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), flipHorizontal, true);
            canvas.drawBitmap(bmp, frameToDraw, whereToDraw, null);
        }
        else
            canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, null);
    }

    public void drawLine(float startX, float startY, float stopX, float stopY, int color, float width) {
        paint.setColor(color);
        paint.setStrokeWidth(width);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    public void drawText(String s, float x, float y, int color, int fontSize) {
        paint.setColor(color);
        paint.setTextSize(fontSize);
        canvas.drawText(s, x, y, paint);
    }

    public int getWidth() {
        return frameBuffer.getWidth();
    }

    public int getHeight() {
        return frameBuffer.getHeight();
    }
}

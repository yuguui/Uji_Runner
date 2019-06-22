package es.uji.al341520.breakthewall.model;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Sprite {



    private Bitmap bitmapToRender;
    private Boolean hFlip;
    private float x,y;
    private int speedX, speedY, sizeX, sizeY;
    private List<Animation> animations;
    private Rect frame;
    private boolean animated;

    public Sprite(Bitmap bitmapToRender, Boolean hFlip, float x, float y, int speedX, int speedY, int sizeX, int sizeY){
        this.bitmapToRender = bitmapToRender;
        this.hFlip = hFlip;
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        animations = null;
        frame = null;
        animated = false;
    }


    public Bitmap getBitmapToRender() {
        return bitmapToRender;
    }

    public void setBitmapToRender(Bitmap bitmapToRender) {
        this.bitmapToRender = bitmapToRender;
    }

    public Boolean gethFlip() {
        return hFlip;
    }

    public void sethFlip(Boolean hFlip) {
        this.hFlip = hFlip;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public Rect getFrame() {
        return frame;
    }

    public void setFrame(Rect frame) {
        this.frame = frame;
    }

    public void move (float time){
        x += speedX*time;
        y += speedY*time;
    }


    public void addAnimation (Animation animation){
        if (animations == null){
            animations = new ArrayList<>();
        }
        animations.add(animation);
        animated = true;
    }

    public Animation getAnimation (int i){
        return animations.get(i);
    }
    public Animation getAnimation(){
        return animations.get(0);
    }
    public boolean isAnimated(){
        return animated;
    }


    public boolean overlapBoundingBox (Sprite collider){

        float xCollider = collider.getX(), yCollider = collider.getY(), colliderSizeX = collider.sizeX, colliderSizeY = collider.sizeY;

        if ( x <= xCollider &&  x + sizeX >= xCollider) {
            if (yCollider + colliderSizeY >= y && yCollider < y) {
                return true;
            }
            if (yCollider <= y + sizeY && yCollider > y) {
                return true;
            }
            //return true;

        }
        if ( y <= yCollider && y + sizeY >= yCollider ) {
            if (xCollider + colliderSizeX >= x && xCollider < x) {
                return true;
            }
            if (xCollider <= x + sizeX && xCollider > x + sizeX) {
                return true;
            }
        }
        return false;
    }


}


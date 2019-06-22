package es.uji.al341520.InfinityRunner.framework;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jvilar on 13/01/16.
 * Modified by jcamen on 15/01/17.
 */
public class TouchHandler implements View.OnTouchListener {
    public enum TouchType {
        TOUCH_DOWN,
        TOUCH_UP,
        TOUCH_DRAGGED
    }

    public static class TouchEvent {
        public TouchType type;
        public int x, y;
        public int pointer;
    }

    private boolean isTouched;
    private int touchX, touchY;

    private static final int MAX_POOLSIZE = 100;

    private Pool<TouchEvent> touchEventPool;
    private List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();

    public TouchHandler(View view) {
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };

        touchEventPool = new Pool<>(factory, MAX_POOLSIZE);
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    registerEvent(event, TouchType.TOUCH_DOWN);
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    registerEvent(event, TouchType.TOUCH_DRAGGED);
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    registerEvent(event, TouchType.TOUCH_UP);
                    isTouched = false;
                    break;
            }
            return true;
        }
    }

    private void registerEvent(MotionEvent event, TouchType type) {
        TouchEvent touchEvent = touchEventPool.newObject();
        touchEvent.x = touchX = (int) event.getX();
        touchEvent.y = touchY = (int) event.getY();
        touchEvent.type = type;
        touchEvent.pointer = event.getPointerId(event.getActionIndex());
        touchEventsBuffer.add(touchEvent);
    }

    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            return pointer == 0 && isTouched;
        }
    }

    public int getTouchX(int pointer) {
        synchronized (this) {
            return touchX;
        }
    }

    public int getTouchY(int pointer) {
        synchronized (this) {
            return touchY;
        }
    }

    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            for (TouchEvent touchEvent : touchEvents)
                touchEventPool.free(touchEvent);
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

}

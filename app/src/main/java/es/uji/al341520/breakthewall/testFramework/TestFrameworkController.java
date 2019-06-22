package es.uji.al341520.breakthewall.testFramework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;

import es.uji.al341520.breakthewall.framework.Graphics;
import es.uji.al341520.breakthewall.framework.IGameController;
import es.uji.al341520.breakthewall.framework.TouchHandler;

/**
 * Created by al341520 on 20/04/18.
 */

public class TestFrameworkController implements IGameController{

    int screenWidth;
    int screenHeight;
    int squareSize;

    Graphics graphics;

    private static final int BASELINE = 300;
    private static final int TOPLINE = BASELINE-100;
    private static final int THRESHOLD = 50;

    int squareX;
    int squareY;

    float targetX;
    float targetY;


    public TestFrameworkController( Context context, int screenWidth, int screenHeight, int squareSize) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.squareSize = squareSize;
        squareX = this.screenWidth/5;
        squareY = BASELINE - this.squareSize;

        graphics = new Graphics(context, screenWidth, screenHeight);


    }
    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents){

        for (int i = 0; i< touchEvents.size(); i++){
            switch (touchEvents.get(i).type){
                case TOUCH_DOWN:
                    targetX = touchEvents.get(i).x;
                    targetY = touchEvents.get(i).y;
                    UpdatePosition(targetX, targetY);
                    System.out.println("El target de X es : " + targetX);
                    break;
            }
        }
    }
    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(0xFF00FF00);
        graphics.drawLine(0,BASELINE,screenWidth,BASELINE, Color.RED,5);
        graphics.drawLine(0,TOPLINE,screenWidth,TOPLINE, Color.BLUE,5);
        graphics.drawRect(squareX, squareY, squareSize, squareSize, 0xFFFF0000);
        return (graphics.getFrameBuffer());

    }

    private void UpdatePosition(float targetX, float targetY){
        if (targetY < squareY && squareY > TOPLINE - squareSize){
            System.out.print("la posición en Y es : " +squareY);
            if (squareY == TOPLINE){
                squareY = TOPLINE-squareSize;
            }
            else{
                squareY = BASELINE-squareSize;
            }

        }
        else if (targetY > squareY+squareSize && squareY < BASELINE){
            System.out.print("la posición en Y2 es : " +squareY);

            if (squareY == BASELINE-squareSize){
                squareY = BASELINE;
            }
            else{
                squareY = BASELINE-squareSize;
            }
        }

        if (targetX > squareX + squareSize + THRESHOLD && squareX < screenWidth*3/5){
            squareX= screenWidth*3/5;

        }
        else if (targetX < squareX  - THRESHOLD && squareX > screenWidth/5){
            squareX= screenWidth/5;
        }

    }

}

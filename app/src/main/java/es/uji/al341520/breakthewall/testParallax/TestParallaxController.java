package es.uji.al341520.breakthewall.testParallax;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;

import es.uji.al341520.breakthewall.Assets;
import es.uji.al341520.breakthewall.framework.Graphics;
import es.uji.al341520.breakthewall.framework.IGameController;
import es.uji.al341520.breakthewall.framework.TouchHandler;
import es.uji.al341520.breakthewall.model.Sprite;

import static es.uji.al341520.breakthewall.testParallax.TestParallaxModel.STAGE_HEIGHT;
import static es.uji.al341520.breakthewall.testParallax.TestParallaxModel.STAGE_WIDTH;

/**
 * Created by al341520 on 20/04/18.
 */

public class TestParallaxController implements IGameController{


    Graphics graphics;

    private static final int BASELINE = 275;
    private static final int TOPLINE = BASELINE-55;
    private static final int THRESHOLD = 45;
    private static final int SQUARE_SIZE = 40;

    float scaleX;
    float scaleY;

    TestParallaxModel model;


    public TestParallaxController( Context context,int screenWidth, int screenHeight) {

        graphics = new Graphics(context, STAGE_WIDTH, STAGE_HEIGHT);

        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        Assets.createAssets(context,SQUARE_SIZE,STAGE_HEIGHT,STAGE_WIDTH);
        model = new TestParallaxModel(SQUARE_SIZE,BASELINE,TOPLINE,THRESHOLD);

    }
    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents){
        model.update(deltaTime);
        for (int i = 0; i< touchEvents.size(); i++){
            switch (touchEvents.get(i).type){
                case TOUCH_DOWN:
                    model.onTouch(touchEvents.get(i).x  * scaleX, touchEvents.get(i).y * scaleY);
                    break;
            }
        }
    }
    @Override
    public Bitmap onDrawingRequested() {
        graphics.clear(0xFF00FF00);
        Sprite[] bgParallax = model.getBgParallax();
        Sprite[] shiftedBgParallax = model.getShiftedBgParallax();

        for (int i = 0; i < bgParallax.length; i++){
            graphics.drawBitmap(bgParallax[i].getBitmapToRender(),bgParallax[i].getX(),bgParallax[i].getY(),false);
            graphics.drawBitmap(shiftedBgParallax[i].getBitmapToRender(),shiftedBgParallax[i].getX(),shiftedBgParallax[i].getY(),false);
        }


        graphics.drawLine(0,BASELINE,STAGE_WIDTH,BASELINE, Color.RED,5);
        graphics.drawLine(0,TOPLINE,STAGE_WIDTH,TOPLINE, Color.BLUE,5);
        graphics.drawRect(model.getSquareX(), model.getSquareY(), SQUARE_SIZE, SQUARE_SIZE, 0xFFFF0000);
        return (graphics.getFrameBuffer());

    }


}

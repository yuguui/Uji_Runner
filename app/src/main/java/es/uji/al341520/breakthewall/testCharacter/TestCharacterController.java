package es.uji.al341520.breakthewall.testCharacter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import java.util.List;

import es.uji.al341520.breakthewall.Assets;
import es.uji.al341520.breakthewall.framework.Graphics;
import es.uji.al341520.breakthewall.framework.IGameController;
import es.uji.al341520.breakthewall.framework.TouchHandler;
import es.uji.al341520.breakthewall.model.Sprite;

import static es.uji.al341520.breakthewall.testCharacter.TestCharacterModel.START_X;
import static es.uji.al341520.breakthewall.testCharacter.TestCharacterModel.UNIT_TIME;
import static es.uji.al341520.breakthewall.testParallax.TestParallaxModel.STAGE_HEIGHT;
import static es.uji.al341520.breakthewall.testParallax.TestParallaxModel.STAGE_WIDTH;

/**
 * Created by al341520 on 20/04/18.
 */

public class TestCharacterController implements IGameController{


    Graphics graphics;

    private static final int BASELINE = 275;
    private static final float PLAYER_WIDTH_PERCENT = 0.15f;
    private static final int TOPLINE = BASELINE-100;
    private static final int THRESHOLD = 45;

    private int playerWidth;

    float scaleX;
    float scaleY;

    TestCharacterModel model;


    public TestCharacterController(Context context, int screenWidth, int screenHeight) {

        graphics = new Graphics(context, STAGE_WIDTH, STAGE_HEIGHT);

        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        playerWidth =(int) (STAGE_WIDTH * PLAYER_WIDTH_PERCENT);

        Assets.createAssets(context,playerWidth,STAGE_HEIGHT,STAGE_WIDTH);
        model = new TestCharacterModel(playerWidth,BASELINE,TOPLINE,THRESHOLD);

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

        Sprite runner = model.getRunner();


        RectF rect = new RectF(runner.getX(),runner.getY(),runner.getX()+runner.getSizeX(),runner.getY()+runner.getSizeY());

        Log.e("ANIMACIONES","El sprite es el de corriendo: "+ (runner.getBitmapToRender() == Assets.characterRunning));
        Log.e("ANIMACIONES","Las esquinas arriba y abajo son: "+ runner.getFrame().top +", "+ runner.getFrame().bottom+ " las esquinas laterales son: " + runner.getFrame().right +", " + runner.getFrame().left);
        Rect rect1 = new Rect(runner.getFrame().top,runner.getFrame().right,runner.getFrame().bottom,runner.getFrame().left);

        graphics.drawAnimatedBitmap(runner.getBitmapToRender(),runner.getFrame(),rect,false);
        //(model.getRunner().getBitmapToRender(),model.getRunner().getX(), model.getRunner().getY(),false);
        return (graphics.getFrameBuffer());

    }


}

package es.uji.al341520.breakthewall.InfinityRunner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;

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

public class UjiRunnerController implements IGameController{

    Graphics graphics;

    private static final int BASELINE = 275;
    private static final float PLAYER_WIDTH_PERCENT = 0.15f;

    private static final int TOPLINE = BASELINE-100;
    private static final int THRESHOLD = 45;

    private int playerWidth;

    float scaleX;
    float scaleY;

    float xPlayAgain;
    float yPlayAgain;
    float xEndPlayAgain;
    float yEndPlayAgain;

    UjiRunnerModel model;


    public UjiRunnerController(Context context, int screenWidth, int screenHeight) {

        graphics = new Graphics(context, STAGE_WIDTH, STAGE_HEIGHT);

        scaleX = (float) STAGE_WIDTH / screenWidth;
        scaleY = (float) STAGE_HEIGHT / screenHeight;
        playerWidth =(int) (STAGE_WIDTH * PLAYER_WIDTH_PERCENT);

        xPlayAgain = 20;
        yPlayAgain = 20;

        xEndPlayAgain = 320;
        yEndPlayAgain = 220;

        Assets.createAssets(context,playerWidth,STAGE_HEIGHT,STAGE_WIDTH);
        model = new UjiRunnerModel(playerWidth,BASELINE,TOPLINE,THRESHOLD);

    }
    @Override
    public void onUpdate(float deltaTime, List<TouchHandler.TouchEvent> touchEvents){
        model.update(deltaTime);

        for (int i = 0; i< touchEvents.size(); i++){
            float xScaled = touchEvents.get(i).x * scaleX;
            float yScaled = touchEvents.get(i).y * scaleY;
            switch (touchEvents.get(i).type){
                case TOUCH_DOWN:
                    model.onTouch(touchEvents.get(i).x  * scaleX, touchEvents.get(i).y * scaleY);
                    break;
                case TOUCH_UP:
                    if(model.isOver())
                    {
                        Log.d("ALERTA:", "SASAFRASSSS");
                    }
                    if (model.isOver() && xPlayAgain <= xScaled && xScaled <=
                            xEndPlayAgain && yPlayAgain <= yScaled && yScaled <=
                            yEndPlayAgain)
                    {
                        Log.d("ALERTA:", "wawawiwa");
                        model.restartGame();
                    }
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


        //graphics.drawLine(0,BASELINE,STAGE_WIDTH,BASELINE, Color.RED,5);
        //graphics.drawLine(0,TOPLINE,STAGE_WIDTH,TOPLINE, Color.BLUE,5);

        if(model.isWaiting() || model.isPlaying())
        {
            graphics.drawText(Integer.toString(model.getMeters()), Assets.METERS_POSITION_X, Assets.METERS_POSITION_Y, Color.WHITE, 10);
            graphics.drawText(Integer.toString(model.getCollectedCoins()), Assets.COINS_POSITION_X, Assets.COINS_POSITION_Y, Color.WHITE, 10);
            float lifeSize = (Assets.HEALTH_WIDTH / model.MAXLIFE) * model.getHealth();
            graphics.drawRect(Assets.HEALTH_POSITION_X, Assets.HEALTH_POSITION_Y, (int) lifeSize, Assets.HEALTH_HEIGHT, Color.GREEN);
            Sprite runner = model.getRunner();


            RectF rect = new RectF(runner.getX(), runner.getY(), runner.getX() + runner.getSizeX(), runner.getY() + runner.getSizeY());

            for (int i = 0; i < model.getGroundObstacles().size(); i++) {
                Sprite obstacle = model.getGroundObstacles().get(i);
                if (obstacle.isAnimated()) {
                    RectF rectObstacle = new RectF(obstacle.getX(), obstacle.getY(), obstacle.getX() + obstacle.getSizeX(), obstacle.getY() + obstacle.getSizeY());
                    graphics.drawAnimatedBitmap(obstacle.getBitmapToRender(), obstacle.getFrame(), rectObstacle, true);
                } else {
                    graphics.drawBitmap(model.getGroundObstacles().get(i).getBitmapToRender(), model.getGroundObstacles().get(i).getX(), model.getGroundObstacles().get(i).getY(), true);
                }


            }

            for (int i = 0; i < model.getFlyingObstacles().size(); i++) {
                Sprite obstacle = model.getFlyingObstacles().get(i);
                if (obstacle.isAnimated()) {
                    RectF rectObstacle = new RectF(obstacle.getX(), obstacle.getY(), obstacle.getX() + obstacle.getSizeX(), obstacle.getY() + obstacle.getSizeY());
                    graphics.drawAnimatedBitmap(obstacle.getBitmapToRender(), obstacle.getFrame(), rectObstacle, true);
                } else {
                    graphics.drawBitmap(model.getFlyingObstacles().get(i).getBitmapToRender(), model.getFlyingObstacles().get(i).getX(), model.getFlyingObstacles().get(i).getY(), true);
                }
            }
            for (int i = 0; i < model.getActiveSprites().size();i++){
                Sprite obstacle = model.getActiveSprites().get(i);
                RectF rectObstacle = new RectF(obstacle.getX(),obstacle.getY(),obstacle.getX()+obstacle.getSizeX(),obstacle.getY()+obstacle.getSizeY());
                graphics.drawAnimatedBitmap(obstacle.getBitmapToRender(),obstacle.getFrame(),rectObstacle,true);
            }

            for (int i = 0; i < model.getCoins().size(); i++) {
                graphics.drawBitmap(model.getCoins().get(i).getBitmapToRender(), model.getCoins().get(i).getX(), model.getCoins().get(i).getY(), false);
            }


            graphics.drawAnimatedBitmap(runner.getBitmapToRender(), runner.getFrame(), rect, false);
        }
        if(model.isOver())
        {
            graphics.drawBitmap(Assets.gameOverImage,xPlayAgain,yPlayAgain,false);
        }
        if(model.isDying())
        {
            Sprite runner = model.getRunner();
            RectF rect = new RectF(runner.getX(), runner.getY(), runner.getX() + runner.getSizeX(), runner.getY() + runner.getSizeY());
            graphics.drawAnimatedBitmap(runner.getBitmapToRender(), runner.getFrame(), rect, false);

            if(runner.getAnimation(3).hasRun()){
                Log.d("DEATH","GAME OVER");
                model.setGameOver();
            }
        }
        return (graphics.getFrameBuffer());

    }


}

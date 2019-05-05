package es.uji.al341520.breakthewall;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by al341371 on 23/04/18.
 */



public class Assets {

    public static Bitmap[] bgLayers = new Bitmap[5];


    public static Bitmap characterRunning;
    public static Bitmap characterCrouching;
    public static Bitmap characterJumping;

    public static int playerHeight;
    public static int runnerJumpsWidth;
    public static int runnerJumpsHeight;
    public static int runnerCrouchesWidth;
    public static int runnerCrouchesHeight;

    public static Bitmap groundObstacle1, groundObstacle2,groundObstacle3,groundObstacle4;
    public static int heightForGroundObstacles, heightForFlyingObstacles;
    public static int groundObstacle1Width, groundObstacle2Width,groundObstacle3Width,groundObstacle4Width;

    public static Bitmap flyingObstacle1, flyingObstacle2;
    public static int flyingObstacle1Width, flyingObstacle2Width;

    public static Bitmap coin;
    public static int coinWidth;

    public static Bitmap gameOverImage;
    public static int GAME_OVER_HEIGHT = 200;
    public static int GAME_OVER_WIDTH = 300;

    public static final int CHARACTER_RUN_FRAME_WIDTH = 120;
    public static final int CHARACTER_RUN_FRAME_HEIGHT = 150;

    public static final int CHARACTER_JUMP_FRAME_WIDTH = 72;
    public static final int CHARACTER_JUMP_FRAME_HEIGHT = 150;

    public static final int CHARACTER_CROUCH_FRAME_WIDTH = 70;
    public static final int CHARACTER_CROUCH_FRAME_HEIGHT = 105;

    private static final int FACE_FRAME_HEIGHT = 120;
    private static final int FACE_FRAME_WIDTH = 120;

    private static final int CHICKEN_FRAME_HEIGHT = 100;
    private static final int CHICKEN_FRAME_WIDTH = 100;

    public static final int CHARACTER_RUN_NUMBER_OF_FRAMES  = 4;
    public static final int CHARACTER_JUMP_NUMBER_OF_FRAMES  = 60;
    public static final int CHARACTER_CROUCH_NUMBER_OF_FRAMES  = 30;


    public static final int GROUNDED_OBSTACLE_NUMBER_OF_FRAMES  = 30;
    public static final int FLYING_OBSTACLE_NUMBER_OF_FRAMES  = 30;



    public static final float HEIGHT_RATIO_GROUND_OBSTACLE = 0.6f;
    public static final float HEIGHT_RATIO_FLYING_OBSTACLE = 0.85f;
    public static final float HEIGHT_RATIO_EXPLOSION = 0.85f;

    public static final float METERS_POSITION_X = 20;
    public static final float METERS_POSITION_Y = 20;

    public static final float COINS_POSITION_X = 320;
    public static final float COINS_POSITION_Y = 20;

    public static final float HEALTH_POSITION_X = 100;
    public static final float HEALTH_POSITION_Y = 20;
    public static final float HEALTH_WIDTH = 200;
    public static final int HEALTH_HEIGHT = 10;




    public static void createAssets(Context context, int playerWidth, int stageHeight,
                                    int parallaxWidth) {
        Resources resources = context.getResources();

        for (Bitmap bitmap: bgLayers) {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        if (characterRunning != null)              characterRunning.recycle();
        if (characterCrouching != null)            characterCrouching.recycle();
        if (characterJumping != null)              characterJumping.recycle();
        if (groundObstacle1 != null)               groundObstacle1.recycle();
        if (groundObstacle2 != null)               groundObstacle2.recycle();
        if (groundObstacle3 != null)               groundObstacle3.recycle();
        if (groundObstacle4 != null)               groundObstacle4.recycle();
        if (flyingObstacle1 != null)               flyingObstacle1.recycle();
        if (flyingObstacle2 != null)               flyingObstacle2.recycle();
        if (gameOverImage != null)                 gameOverImage.recycle();
        if (coin != null)                          coin.recycle();


        int[] bgLayersResources = {
                R.drawable.parallax0,
                R.drawable.parallax1,                                                                                  //ACUERDATE DE CAMBIAR ESTOS BRICKS POR EL FONDO PUTO MONO
                R.drawable.parallax2,
                R.drawable.parallax3,
                R.drawable.parallax4
        };

        bgLayers = new Bitmap[bgLayersResources.length];
        for (int i = 0; i < bgLayers.length; i++) {
            bgLayers[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    resources, bgLayersResources[i]), parallaxWidth, stageHeight, true);
        }

        playerHeight = (CHARACTER_RUN_FRAME_HEIGHT * playerWidth) / CHARACTER_RUN_FRAME_WIDTH;
        characterRunning = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.spritesheetcorrer), playerWidth*CHARACTER_RUN_NUMBER_OF_FRAMES
                , playerHeight, true);

        runnerJumpsWidth = (playerWidth * CHARACTER_JUMP_FRAME_WIDTH) /
                CHARACTER_RUN_FRAME_WIDTH;
        runnerJumpsHeight = (CHARACTER_JUMP_FRAME_HEIGHT * runnerJumpsWidth) /
                CHARACTER_JUMP_FRAME_WIDTH;

        characterJumping = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.ball), runnerJumpsWidth*
                CHARACTER_JUMP_NUMBER_OF_FRAMES, runnerJumpsHeight , true);

        runnerCrouchesWidth = (playerWidth * CHARACTER_CROUCH_FRAME_WIDTH) /
                CHARACTER_RUN_FRAME_WIDTH;
        runnerCrouchesHeight = (CHARACTER_CROUCH_FRAME_HEIGHT*runnerCrouchesWidth)
                / CHARACTER_CROUCH_FRAME_WIDTH;

        characterCrouching = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.boton), runnerCrouchesWidth *
                CHARACTER_CROUCH_NUMBER_OF_FRAMES, runnerCrouchesHeight , true);




        heightForGroundObstacles = (int) (playerHeight * HEIGHT_RATIO_GROUND_OBSTACLE);
        groundObstacle1Width = (heightForGroundObstacles * FACE_FRAME_WIDTH) /
                FACE_FRAME_HEIGHT;
        groundObstacle1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                R.drawable.paddle), groundObstacle1Width*GROUNDED_OBSTACLE_NUMBER_OF_FRAMES, heightForGroundObstacles, true);

        groundObstacle2Width = (heightForGroundObstacles * CHICKEN_FRAME_WIDTH) /
                CHICKEN_FRAME_HEIGHT;
        groundObstacle2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.boton), groundObstacle2Width,
                heightForGroundObstacles, true);

        groundObstacle3Width = (heightForGroundObstacles * FACE_FRAME_WIDTH) /
                FACE_FRAME_HEIGHT;
        groundObstacle3 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                R.drawable.brick0), groundObstacle3Width, heightForGroundObstacles, true);

        groundObstacle4Width = (heightForGroundObstacles * CHICKEN_FRAME_WIDTH) /
                CHICKEN_FRAME_HEIGHT;
        groundObstacle4 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.brick3), groundObstacle4Width,
                heightForGroundObstacles, true);

        coinWidth = groundObstacle2.getWidth();
        coin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.brick0), coinWidth, heightForGroundObstacles, true);

        gameOverImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,R.drawable.boton), GAME_OVER_WIDTH, GAME_OVER_HEIGHT, true);

        heightForFlyingObstacles = (int) (playerHeight * HEIGHT_RATIO_FLYING_OBSTACLE);
        flyingObstacle1Width = (heightForFlyingObstacles * FACE_FRAME_WIDTH) /
                FACE_FRAME_HEIGHT;
        flyingObstacle1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                R.drawable.brick0), flyingObstacle1Width*FLYING_OBSTACLE_NUMBER_OF_FRAMES, heightForFlyingObstacles, true);

        flyingObstacle2Width = (heightForFlyingObstacles * FACE_FRAME_WIDTH) /
                FACE_FRAME_HEIGHT;
        flyingObstacle2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources,
                R.drawable.brick1), flyingObstacle2Width, heightForFlyingObstacles, true);






    }

}

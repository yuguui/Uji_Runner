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


    public static final int CHARACTER_RUN_FRAME_WIDTH = 120;
    public static final int CHARACTER_RUN_FRAME_HEIGHT = 150;
    public static final int CHARACTER_JUMP_FRAME_WIDTH = 72;
    public static final int CHARACTER_JUMP_FRAME_HEIGHT = 150;
    public static final int CHARACTER_CROUCH_FRAME_WIDTH = 70;
    public static final int CHARACTER_CROUCH_FRAME_HEIGHT = 105;

    public static final int CHARACTER_RUN_NUMBER_OF_FRAMES  = 8;
    public static final int CHARACTER_JUMP_NUMBER_OF_FRAMES  = 6;
    public static final int CHARACTER_CROUCH_NUMBER_OF_FRAMES  = 5;




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




        int[] bgLayersResources = {
                R.drawable.brick0,
                R.drawable.brick1,                                                                                  //ACUERDATE DE CAMBIAR ESTOS BRICKS POR EL FONDO PUTO MONO
                R.drawable.brick2,
                R.drawable.brick3,
                R.drawable.brick4
        };
        bgLayers = new Bitmap[bgLayersResources.length];
        for (int i = 0; i < bgLayers.length; i++) {
            bgLayers[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                    resources, bgLayersResources[i]), parallaxWidth, stageHeight, true);
        }

        playerHeight = (CHARACTER_RUN_FRAME_HEIGHT * playerWidth) / CHARACTER_RUN_FRAME_WIDTH;
        characterRunning = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.paddle), playerWidth
                , playerHeight*CHARACTER_RUN_NUMBER_OF_FRAMES, true);

        runnerJumpsWidth = (playerWidth * CHARACTER_JUMP_FRAME_WIDTH) /
                CHARACTER_RUN_FRAME_WIDTH;
        runnerJumpsHeight = (CHARACTER_JUMP_FRAME_HEIGHT * runnerJumpsWidth) /
                CHARACTER_JUMP_FRAME_WIDTH;

        characterJumping = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.ball), runnerJumpsWidth , runnerJumpsHeight *
                CHARACTER_JUMP_NUMBER_OF_FRAMES, true);

        runnerCrouchesWidth = (playerWidth * CHARACTER_CROUCH_FRAME_WIDTH) /
                CHARACTER_RUN_FRAME_WIDTH;
        runnerCrouchesHeight = (CHARACTER_CROUCH_FRAME_HEIGHT*runnerCrouchesWidth)
                / CHARACTER_CROUCH_FRAME_WIDTH;

        characterCrouching = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                resources, R.drawable.boton), runnerCrouchesWidth , runnerCrouchesHeight *
                CHARACTER_CROUCH_NUMBER_OF_FRAMES, true);
    }

}

package es.uji.al341520.InfinityRunner.testParallax;


import es.uji.al341520.InfinityRunner.Assets;
import es.uji.al341520.InfinityRunner.model.Sprite;

public class TestParallaxModel {

    private static final float UNIT_TIME = 1f/30;

    public static final int STAGE_WIDTH = 480;
    public static final int STAGE_HEIGHT = 320;

    public static final int PARALLAX_LAYERS = 5;


    private int playerWidht;
    private int baseline;
    private int topline;
    private int threshold;



    private int squareX;
    private int squareY;

    public int getSquareX() {
        return squareX;
    }

    public int getSquareY() {
        return squareY;
    }


    private Sprite[] bgParallax;
    private Sprite[] shiftedBgParallax;

    public Sprite[] getBgParallax() {
        return bgParallax;
    }

    public Sprite[] getShiftedBgParallax() {
        return shiftedBgParallax;
    }



    private float tickTime;

    public TestParallaxModel(int platerWidth, int baseline, int topline, int threshold){
        this.playerWidht = platerWidth;
        this.baseline = baseline;
        this.topline = topline;
        this.threshold = threshold;
        tickTime = 0;
        squareX = STAGE_WIDTH/5;
        squareY = baseline - platerWidth;


        // Set speeds and initial pos X for parallax layers
        bgParallax = new Sprite[PARALLAX_LAYERS];
        shiftedBgParallax = new Sprite[PARALLAX_LAYERS];
        for (int i = 0; i < PARALLAX_LAYERS; i++) {
            int speed = (i+1)*5;
            bgParallax[i] = new Sprite(Assets.bgLayers[i], false, 0f, 0f, -speed,
                    0, STAGE_WIDTH , STAGE_HEIGHT);
            shiftedBgParallax[i] = new Sprite(Assets.bgLayers[i], false, STAGE_WIDTH,
                    0f, -speed, 0, STAGE_WIDTH, STAGE_HEIGHT);
        }

    }


    public void update(float deltaTime) {
        tickTime += deltaTime;
        while (tickTime >= UNIT_TIME) {
            tickTime -= UNIT_TIME;
            updateParallaxBg();
        }
    }

    private void updateParallaxBg() {
        for (int i = 0; i < PARALLAX_LAYERS; i++) {
            shiftedBgParallax[i].move(UNIT_TIME);
            if(shiftedBgParallax[i].getX() < -STAGE_WIDTH){
                shiftedBgParallax[i].setX(STAGE_WIDTH);
            }
            bgParallax[i].move(UNIT_TIME);
            if(bgParallax[i].getX() < -STAGE_WIDTH){
                bgParallax[i].setX(STAGE_WIDTH);
            }

        }
    }

    public void onTouch(float x, float y){
        UpdatePosition(x,y);
    }
    private void UpdatePosition(float targetX, float targetY){
        if (targetY < squareY && squareY > topline - playerWidht){
            System.out.print("la posición en Y es : " +squareY);
            if (squareY == baseline - playerWidht){
                squareY = topline-playerWidht;
            }
            else{
                squareY = baseline-playerWidht;
            }

        }
        else if (targetY > squareY+playerWidht && squareY < baseline){
            System.out.print("la posición en Y2 es : " +squareY);

            if (squareY == baseline-playerWidht){
                squareY = baseline;
            }
            else{
                squareY = baseline-playerWidht;
            }
        }

        if (targetX > squareX + playerWidht + threshold && squareX < STAGE_WIDTH*3/5){
            squareX= STAGE_WIDTH*3/5;

        }
        else if (targetX < squareX  - threshold && squareX > STAGE_WIDTH/5){
            squareX= STAGE_WIDTH/5;
        }

    }
}

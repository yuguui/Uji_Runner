package es.uji.al341520.InfinityRunner.testCharacter;


import es.uji.al341520.InfinityRunner.Assets;
import es.uji.al341520.InfinityRunner.model.Animation;
import es.uji.al341520.InfinityRunner.model.Sprite;

import static es.uji.al341520.InfinityRunner.Assets.CHARACTER_CROUCH_NUMBER_OF_FRAMES;
import static es.uji.al341520.InfinityRunner.Assets.CHARACTER_JUMP_NUMBER_OF_FRAMES;
import static es.uji.al341520.InfinityRunner.Assets.CHARACTER_RUN_NUMBER_OF_FRAMES;
import static es.uji.al341520.InfinityRunner.Assets.playerHeight;

public class TestCharacterModel {

    public static final float UNIT_TIME = 1f/30;

    public static final int STAGE_WIDTH = 480;
    public static final int STAGE_HEIGHT = 320;

    public static final int PARALLAX_LAYERS = 5;

    public static final int START_X = STAGE_WIDTH/8;
    public static final int END_X =(STAGE_WIDTH * 5) / 8;


    public static final int RUNNER_SPEED = 10;

    public static final int JUMP_OFFSET = 50;





    private int playerWidth;
    private int baseline;
    private int topline;
    private int threshold;


    private float tickTime;

    private enum RunnerState {RUNNING, CROUCHING, JUMPING}

    private Sprite[] bgParallax;
    private Sprite[] shiftedBgParallax;

    public Sprite[] getBgParallax() {
        return bgParallax;
    }

    public Sprite[] getShiftedBgParallax() {
        return shiftedBgParallax;
    }




    private Sprite runner;

    public Sprite getRunner() {
        return runner;
    }

    private RunnerState runnerState;

    private int[] runnerWidths;
    private int[] runnerHeights;

    Animation running;
    Animation crouching;
    Animation jumping;



    public TestCharacterModel(int playerWidth, int baseline, int topline, int threshold){
        this.playerWidth = playerWidth;
        this.baseline = baseline;
        this.topline = topline;
        this.threshold = threshold;
        tickTime = 0;



        // Set speeds and initial pos X for parallax layers
        bgParallax = new Sprite[PARALLAX_LAYERS];
        shiftedBgParallax = new Sprite[PARALLAX_LAYERS];
        for (int i = 0; i < PARALLAX_LAYERS; i++) {
            int speed = (i+1)*2;
            bgParallax[i] = new Sprite(Assets.bgLayers[i], false, 0f, 0f, -speed,
                    0, STAGE_WIDTH , STAGE_HEIGHT);
            shiftedBgParallax[i] = new Sprite(Assets.bgLayers[i], false, STAGE_WIDTH,
                    0f, -speed, 0, STAGE_WIDTH, STAGE_HEIGHT);
        }


        // Creation of the arrays
        runnerWidths = new int[RunnerState.JUMPING.ordinal() + 1];
        runnerHeights = new int[RunnerState.JUMPING.ordinal() + 1];
        // Creation of the runner. Set initial position and dimensions
        runner = new Sprite(Assets.characterRunning, false, START_X, this.baseline -
                Assets.playerHeight, 0, 0, this.playerWidth, Assets.playerHeight);
        // set runner state: initially running
        runnerState = RunnerState.RUNNING;
        // store dimensions when running
        runnerWidths[runnerState.ordinal()] = this.playerWidth;
        runnerHeights[runnerState.ordinal()] = Assets.playerHeight;
        // get and store dimensions when crouching
        runnerWidths[RunnerState.CROUCHING.ordinal()] = Assets.runnerCrouchesWidth;
        runnerHeights[RunnerState.CROUCHING.ordinal()] = Assets.runnerCrouchesHeight;
        // get and store dimensions when jumping
        runnerWidths[RunnerState.JUMPING.ordinal()] = Assets.runnerJumpsWidth;
        runnerHeights[RunnerState.JUMPING.ordinal()] = Assets.runnerJumpsHeight;

        running = new Animation(1, CHARACTER_RUN_NUMBER_OF_FRAMES,runnerWidths[0],runnerHeights[0],runnerWidths[0]*(CHARACTER_RUN_NUMBER_OF_FRAMES),30);
        crouching = new Animation(1, CHARACTER_CROUCH_NUMBER_OF_FRAMES,runnerWidths[1],runnerHeights[1],runnerWidths[1]*CHARACTER_CROUCH_NUMBER_OF_FRAMES ,30);
        jumping = new Animation(1, CHARACTER_JUMP_NUMBER_OF_FRAMES,runnerWidths[2],runnerHeights[2],runnerWidths[2]*CHARACTER_JUMP_NUMBER_OF_FRAMES ,30);

        runner.addAnimation(running);
        runner.addAnimation(crouching);
        runner.addAnimation(jumping);


    }


    public void update(float deltaTime) {
        tickTime += deltaTime;
        while (tickTime >= UNIT_TIME) {
            tickTime -= UNIT_TIME;
            updateParallaxBg();

            if(runnerState == RunnerState.JUMPING){
                if(jumping.hasRun()){
                    runnerState = RunnerState.RUNNING;
                    UpdateValues();
                }
            }
            updateRunner();
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
    private void updateRunner(){
        if (runner.getX()<=START_X && runner.getSpeedX() < 0|| runner.getX()+runner.getSizeX() > END_X && runner.getSpeedX() > 0)
        {
            runner.setSpeedX(0);
        }
        runner.setFrame(runner.getAnimation(runnerState.ordinal()).getCurrentFrame(UNIT_TIME));

        runner.move(UNIT_TIME);


    }



    public void onTouch(float x, float y){
        UpdatePosition(x,y);
    }
    private void UpdatePosition(float targetX, float targetY){

        if (targetY < topline && runnerState != RunnerState.JUMPING){
            if (runnerState == RunnerState.RUNNING){
                runnerState = RunnerState.JUMPING;
                UpdateValues();
            }
            else{
                runnerState = RunnerState.RUNNING;
                UpdateValues();
            }
        }
        else if (targetY > baseline && runnerState != RunnerState.CROUCHING){
            if(runnerState == RunnerState.RUNNING){
                runnerState = RunnerState.CROUCHING;
                UpdateValues();
            }
        }
        else if(targetY> topline && targetY< baseline && runnerState != RunnerState.RUNNING){
            if(runnerState == RunnerState.CROUCHING){
                runnerState = RunnerState.RUNNING;
                UpdateValues();
            }
        }
        else if (targetX > runner.getX() + playerWidth + threshold && runnerState == RunnerState.RUNNING){
            if(runner.getSpeedX()>=0){
                runner.setSpeedX(RUNNER_SPEED);
            }
            else{
                runner.setSpeedX(0);
            }
        }
        else if (targetX < runner.getX()  - threshold && runnerState == RunnerState.RUNNING){
            if(runner.getSpeedX()<=0){
                runner.setSpeedX(-RUNNER_SPEED);
            }
            else{
                runner.setSpeedX(0);
            }
        }

    }


    private void UpdateValues(){
        runner.setSpeedX(0);
        runner.setSizeX(runnerWidths[runnerState.ordinal()]);
        runner.setSizeY(runnerHeights[runnerState.ordinal()]);
        runner.getAnimation(runnerState.ordinal()).resetAnimation();
        if(runnerState == RunnerState.RUNNING){
            runner.setBitmapToRender(Assets.characterRunning);
            runner.setY(baseline-playerHeight);
        }
        else if (runnerState == RunnerState.CROUCHING){
            runner.setBitmapToRender(Assets.characterCrouching);
        }
        else {
            runner.setBitmapToRender(Assets.characterJumping);
            runner.setY(topline-JUMP_OFFSET);
        }


    }

}

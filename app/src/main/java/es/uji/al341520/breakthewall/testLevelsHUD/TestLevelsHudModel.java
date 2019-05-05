package es.uji.al341520.breakthewall.testLevelsHUD;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

import es.uji.al341520.breakthewall.Assets;
import es.uji.al341520.breakthewall.framework.Level;
import es.uji.al341520.breakthewall.model.Animation;
import es.uji.al341520.breakthewall.model.Sprite;
import es.uji.al341520.breakthewall.model.TimedSprite;

import static es.uji.al341520.breakthewall.Assets.CHARACTER_CROUCH_NUMBER_OF_FRAMES;
import static es.uji.al341520.breakthewall.Assets.CHARACTER_JUMP_NUMBER_OF_FRAMES;
import static es.uji.al341520.breakthewall.Assets.CHARACTER_RUN_NUMBER_OF_FRAMES;
import static es.uji.al341520.breakthewall.Assets.FLYING_OBSTACLE_NUMBER_OF_FRAMES;
import static es.uji.al341520.breakthewall.Assets.GROUNDED_OBSTACLE_NUMBER_OF_FRAMES;
import static es.uji.al341520.breakthewall.Assets.characterRunning;
import static es.uji.al341520.breakthewall.Assets.coin;

public class TestLevelsHudModel {

    public static final float UNIT_TIME = 1f/30;

    public static final int STAGE_WIDTH = 480;
    public static final int STAGE_HEIGHT = 320;

    public static final int PARALLAX_LAYERS = 5;

    public static final int START_X = STAGE_WIDTH/8;
    public static final int END_X =(STAGE_WIDTH * 5) / 8;


    public static final int RUNNER_SPEED = 100;
    public static final int OBSTACLES_SPEED = 100;

    public static final int JUMP_OFFSET = 50;

    public static final int MAXLIFE = 100;


    private int playerWidth;
    private int baseline;
    private int topline;
    private int threshold;

    private int currentLife;
    private int currentMeters;
    private int coinsCollected;

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

    Animation grounded;
    Animation flying;


    private static final int POOL_OBSTACLES_SIZE = 12;
    private static final int POOL_COINS_SIZE = 5;

    private static final float GROUNDED1_SPAWN_CHANCE = 0.75f;
    private static final float GROUNDED2_SPAWN_CHANCE = 0.85f;
    private static final float GROUNDED3_SPAWN_CHANCE = 0.9f;
    private static final float GROUNDED4_SPAWN_CHANCE = 1f;


    private static final float FLYING1_SPAWN_CHANCE = 0.8f;
    private static final float FLYING2_SPAWN_CHANCE = 1f;


    private static final float DELAY_OBSTACLE = 2.0f;



    private static final float TIME_BETWEEN_GROUND_OBSTACLES = 3f;
    private static final double PROB_ACTIVATION_GROUND_OBSTACLE = 0.5;

    private static final float TIME_BETWEEN_FLYING_OBSTACLES = 3f;
    private static final double PROB_ACTIVATION_FLYING_OBSTACLE = 0.5;

    private static final float TIME_BETWEEN_COINS = 10.0f;
    private static final float MIN_COIN_DURATION = 3.0f;
    private static final float MAX_COIN_DURATION = 8.0f;
    private static final float TIME_BETWEEN_METERS = 2.0f;
    private static final double PROB_ACTIVATION_COIN = 0.5;
    private static final int HEALTH_INCREASE = 5;


    private static final int GROUND_EASY_DAMAGE = 2;
    private static final int GROUND_MEDIUM_DAMAGE =4;
    private static final int GROUND_HARD_DAMAGE = 6;

    private static final int AIR_EASY_DAMAGE = 4;
    private static final int AIR_MEDIUM_DAMAGE = 6;
    private static final int AIR_HARD_DAMAGE = 8;


    private float timeSinceLastGroundObstacle;
    private float timeSinceLastFlyingObstacle;

    private float timeSinceLastMetersCheck;
    private float timeSinceLastCoin;


    private Level currentLevel;

    private Sprite[] poolGroundObstacles;
    private Sprite[] poolFlyingObstacles;
    private TimedSprite[] poolCoins;


    private int poolGroundObstaclesIndex;
    private int poolFlyingObstaclesIndex;
    private int poolCoinsIndex;




    private ArrayList<Sprite> activeSprites;
    private ArrayList<Sprite> groundObstacles;
    private ArrayList<Sprite> flyingObstacles;
    private ArrayList<TimedSprite> coins;

    public ArrayList<Sprite> getActiveSprites() { return activeSprites; }
    public ArrayList<Sprite> getGroundObstacles() { return groundObstacles; }
    public ArrayList<Sprite> getFlyingObstacles() { return flyingObstacles; }
    public ArrayList<TimedSprite> getCoins() {return coins;}





    public TestLevelsHudModel(int playerWidth, int baseline, int topline, int threshold){
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

        running = new Animation(1, CHARACTER_RUN_NUMBER_OF_FRAMES,runnerWidths[0],runnerHeights[0],runnerWidths[0]*(CHARACTER_RUN_NUMBER_OF_FRAMES),5);
        crouching = new Animation(1, CHARACTER_CROUCH_NUMBER_OF_FRAMES,runnerWidths[1],runnerHeights[1],runnerWidths[1]*CHARACTER_CROUCH_NUMBER_OF_FRAMES ,30);
        jumping = new Animation(1, CHARACTER_JUMP_NUMBER_OF_FRAMES,runnerWidths[2],runnerHeights[2],runnerWidths[2]*CHARACTER_JUMP_NUMBER_OF_FRAMES ,30);


        grounded = new Animation(1,GROUNDED_OBSTACLE_NUMBER_OF_FRAMES,Assets.groundObstacle1Width,Assets.heightForGroundObstacles,Assets.groundObstacle1Width * GROUNDED_OBSTACLE_NUMBER_OF_FRAMES,30);
        flying =  new Animation(1,FLYING_OBSTACLE_NUMBER_OF_FRAMES,Assets.flyingObstacle1Width,Assets.heightForFlyingObstacles,Assets.flyingObstacle1Width * FLYING_OBSTACLE_NUMBER_OF_FRAMES,30);


        runner.addAnimation(running);
        runner.addAnimation(crouching);
        runner.addAnimation(jumping);

        //OBSTACLES



        groundObstacles = new ArrayList<>();
        flyingObstacles = new ArrayList<>();
        coins = new ArrayList<>();
        activeSprites = new ArrayList<>();

        poolFlyingObstacles = new Sprite[POOL_OBSTACLES_SIZE];
        poolGroundObstacles = new Sprite[POOL_OBSTACLES_SIZE];
        poolCoins = new TimedSprite[POOL_COINS_SIZE];

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){
            double random = Math.random();
            if(random<=GROUNDED1_SPAWN_CHANCE){
                poolGroundObstacles[i]= new Sprite(Assets.groundObstacle1,false,STAGE_WIDTH,baseline-Assets.heightForGroundObstacles,0,0,Assets.groundObstacle1Width,Assets.heightForGroundObstacles);
                poolGroundObstacles[i].addAnimation(grounded);
            }
            else if(random<GROUNDED2_SPAWN_CHANCE){
                poolGroundObstacles[i]= new Sprite(Assets.groundObstacle2,false,STAGE_WIDTH,baseline-Assets.heightForGroundObstacles,0,0,Assets.groundObstacle2Width,Assets.heightForGroundObstacles);
            }
            else if (random< GROUNDED3_SPAWN_CHANCE){
                poolGroundObstacles[i]= new Sprite(Assets.groundObstacle3,false,STAGE_WIDTH,baseline-Assets.heightForGroundObstacles,0,0,Assets.groundObstacle3Width,Assets.heightForGroundObstacles);
            }
            else {
                poolGroundObstacles[i]= new Sprite(Assets.groundObstacle4,false,STAGE_WIDTH,baseline-Assets.heightForGroundObstacles,0,0,Assets.groundObstacle4Width,Assets.heightForGroundObstacles);
            }

        }

        for (int i = 0; i < POOL_OBSTACLES_SIZE; i++){
            double random = Math.random();
            if(random<=FLYING1_SPAWN_CHANCE){
                poolFlyingObstacles[i]= new Sprite(Assets.flyingObstacle1,false,STAGE_WIDTH,topline-(2*Assets.heightForFlyingObstacles/3),0,0,Assets.flyingObstacle1Width,Assets.heightForFlyingObstacles);
                poolFlyingObstacles[i].addAnimation(flying);
            }
            else{
                poolFlyingObstacles[i]= new Sprite(Assets.flyingObstacle2,false,STAGE_WIDTH,topline-(2*Assets.heightForFlyingObstacles/3),0,0,Assets.flyingObstacle2Width,Assets.heightForFlyingObstacles);
            }


        }

        for(int i= 0; i< POOL_COINS_SIZE; i++)
        {
            float random = (float) Math.random();
            random = (random * (MIN_COIN_DURATION - MAX_COIN_DURATION)) + MAX_COIN_DURATION;

            poolCoins[i] = new TimedSprite(Assets.coin, false, STAGE_WIDTH, baseline-Assets.heightForGroundObstacles, 0,0, Assets.coinWidth, Assets.heightForFlyingObstacles, random);
        }

        poolGroundObstaclesIndex= 0;
        poolFlyingObstaclesIndex= 0;
        poolCoinsIndex = 0;

        currentLife = MAXLIFE;
        currentMeters = 0;
        coinsCollected = 0;
        currentLevel = Level.EASY;
    }


    public void update(float deltaTime) {
        tickTime += deltaTime;
        while (tickTime >= UNIT_TIME) {
            tickTime -= UNIT_TIME;

            if(runnerState == RunnerState.JUMPING){
                if(jumping.hasRun()){
                    runnerState = RunnerState.RUNNING;
                    UpdateValues();
                }
            }

            updateParallaxBg();
            activateFlyingObstacle();
            activateGroundObstacle();
            activateCoin();
            updateObstacles();
            UpdateCoins();
            updateRunner();
            updateMeters();
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
        for(int i = 0; i < groundObstacles.size(); i++){
            if(runner.overlapBoundingBox(groundObstacles.get(i))){

                groundObstacles.get(i).setX(STAGE_WIDTH);
                groundObstacles.get(i).setSpeedX(0);
                groundObstacles.remove(i);

                Bitmap image = flyingObstacles.get(i).getBitmapToRender();
                if(image == Assets.groundObstacle1)
                {
                    currentLife -= GROUND_EASY_DAMAGE;
                }
                else if (image == Assets.groundObstacle2)
                {
                    currentLife -= GROUND_MEDIUM_DAMAGE;
                }
                else
                {
                    currentLife -= GROUND_HARD_DAMAGE;
                }
            }
        }

        for(int i = 0; i < flyingObstacles.size(); i++){
            if(runner.overlapBoundingBox(flyingObstacles.get(i))){

                flyingObstacles.get(i).setX(STAGE_WIDTH);
                flyingObstacles.get(i).setSpeedX(0);
                flyingObstacles.remove(i);

                Bitmap image = flyingObstacles.get(i).getBitmapToRender();
                if(image == Assets.flyingObstacle1)
                {
                    currentLife -= AIR_EASY_DAMAGE;
                }
                else
                {
                    currentLife -= AIR_MEDIUM_DAMAGE;
                }
            }
        }

        for(int i = 0; i < coins.size(); i++){
            if(runner.overlapBoundingBox(coins.get(i))){

                coins.get(i).setX(STAGE_WIDTH);
                coins.get(i).setSpeedX(0);
                coins.remove(i);

                coinsCollected++;
            }
        }
        runner.move(UNIT_TIME);

    }

    private void updateObstacles(){
        for (int i = 0; i < groundObstacles.size();i++){
            if(groundObstacles.get(i).isAnimated()){
                groundObstacles.get(i).setFrame(groundObstacles.get(i).getAnimation().getCurrentFrame(UNIT_TIME));
            }
            groundObstacles.get(i).move(UNIT_TIME);
            if(groundObstacles.get(i).getX() < -groundObstacles.get(i).getSizeX()){
                groundObstacles.get(i).setX(STAGE_WIDTH);
                groundObstacles.get(i).setSpeedX(0);
                groundObstacles.remove(i);
            }
        }
        for (int i = 0; i < flyingObstacles.size();i++){
            if(flyingObstacles.get(i).isAnimated()){
                flyingObstacles.get(i).setFrame(flyingObstacles.get(i).getAnimation().getCurrentFrame(UNIT_TIME));
            }
            flyingObstacles.get(i).move(UNIT_TIME);
            if(flyingObstacles.get(i).getX() < -flyingObstacles.get(i).getSizeX()){
                flyingObstacles.get(i).setX(STAGE_WIDTH);
                flyingObstacles.get(i).setSpeedX(0);
                flyingObstacles.remove(i);
            }
        }

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
            runner.setY(baseline-characterRunning.getHeight());
        }
        else if (runnerState == RunnerState.CROUCHING){
            runner.setBitmapToRender(Assets.characterCrouching);
            runner.setY(baseline-Assets.characterCrouching.getHeight());
        }
        else {
            runner.setBitmapToRender(Assets.characterJumping);
            runner.setY(topline-JUMP_OFFSET);
        }


    }


    private void activateGroundObstacle() {
        double r;
        timeSinceLastGroundObstacle += UNIT_TIME;

        if (timeSinceLastGroundObstacle >= TIME_BETWEEN_GROUND_OBSTACLES) {
            r = Math.random();
            if (poolGroundObstacles[poolGroundObstaclesIndex].isAnimated()) {
                poolGroundObstacles[poolGroundObstaclesIndex].getAnimation().resetAnimation();
            }

            Log.wtf("RANDOM", "EL RANDOM DE GROUND HA SIDO : "+ r);
            if(currentLevel.isEasy())
            {
                if(poolGroundObstacles[poolGroundObstaclesIndex].getBitmapToRender() != Assets.groundObstacle1)
                {
                    poolGroundObstaclesIndex++;
                    if(poolGroundObstaclesIndex >= POOL_OBSTACLES_SIZE)
                    {
                        poolGroundObstaclesIndex = 0;
                    }
                    return;
                }
            }
            else if(currentLevel.isMedium())
            {
                Bitmap image = poolGroundObstacles[poolGroundObstaclesIndex].getBitmapToRender();
                if(image != Assets.groundObstacle1 && image != Assets.groundObstacle2)
                {
                    poolGroundObstaclesIndex++;
                    if(poolGroundObstaclesIndex >= POOL_OBSTACLES_SIZE)
                    {
                        poolGroundObstaclesIndex = 0;
                    }
                    return;
                }
            }

            if (r < PROB_ACTIVATION_GROUND_OBSTACLE) {


                poolGroundObstacles[poolGroundObstaclesIndex].setSpeedX(-OBSTACLES_SPEED);
                if (poolGroundObstacles[poolGroundObstaclesIndex].isAnimated()) {
                    poolGroundObstacles[poolGroundObstaclesIndex].getAnimation().resetAnimation();
                }
                groundObstacles.add(poolGroundObstacles[poolGroundObstaclesIndex]);

                poolGroundObstaclesIndex++;
                if (poolGroundObstaclesIndex == POOL_OBSTACLES_SIZE) {
                    poolGroundObstaclesIndex = 0;
                }



                if (TIME_BETWEEN_FLYING_OBSTACLES - timeSinceLastFlyingObstacle - UNIT_TIME <= DELAY_OBSTACLE) {
                    timeSinceLastFlyingObstacle = TIME_BETWEEN_FLYING_OBSTACLES - UNIT_TIME - DELAY_OBSTACLE;
                }
                timeSinceLastGroundObstacle -= TIME_BETWEEN_GROUND_OBSTACLES;

            }

        }
    }



    private void activateFlyingObstacle(){
        double r;
        timeSinceLastFlyingObstacle += UNIT_TIME;

        if (timeSinceLastFlyingObstacle >= TIME_BETWEEN_FLYING_OBSTACLES) {
            r = Math.random();
            Log.wtf("RANDOM", "EL RANDOM DE FLYING HA SIDO : "+ r);

            if(currentLevel.isEasy())
            {
                if(poolFlyingObstacles[poolFlyingObstaclesIndex].getBitmapToRender() != Assets.flyingObstacle1)
                {
                    poolFlyingObstaclesIndex++;
                    if(poolFlyingObstaclesIndex >= POOL_OBSTACLES_SIZE)
                    {
                        poolFlyingObstaclesIndex = 0;
                    }
                    return;
                }
            }
            if (r < PROB_ACTIVATION_FLYING_OBSTACLE) {
                Log.wtf("POOL", "el indice de los obstaculos voladores es : " +poolFlyingObstaclesIndex);
                poolFlyingObstacles[poolFlyingObstaclesIndex].setSpeedX(-OBSTACLES_SPEED);

                if(poolFlyingObstacles[poolFlyingObstaclesIndex].isAnimated()){
                    poolFlyingObstacles[poolFlyingObstaclesIndex].getAnimation().resetAnimation();
                }
                flyingObstacles.add(poolFlyingObstacles[poolFlyingObstaclesIndex]);


                poolFlyingObstaclesIndex++;
                if(poolFlyingObstaclesIndex == POOL_OBSTACLES_SIZE){
                    poolFlyingObstaclesIndex = 0;
                }

            }

            if (TIME_BETWEEN_GROUND_OBSTACLES - timeSinceLastGroundObstacle - UNIT_TIME <= DELAY_OBSTACLE) {
                timeSinceLastGroundObstacle = TIME_BETWEEN_GROUND_OBSTACLES - UNIT_TIME - DELAY_OBSTACLE;
            }
            timeSinceLastFlyingObstacle -= TIME_BETWEEN_FLYING_OBSTACLES;

        }

    }

    private void activateCoin()
    {
        double r;
        timeSinceLastCoin += UNIT_TIME;

        if(timeSinceLastCoin > TIME_BETWEEN_COINS)
        {
            r = Math.random();

            if(r < PROB_ACTIVATION_COIN)
            {
                //DO STUFF TO SPAWN COIN\\
                poolCoins[poolCoinsIndex].setSpeedX(-OBSTACLES_SPEED);

                float random = (float) Math.random();
                random = (random * (MIN_COIN_DURATION - MAX_COIN_DURATION)) + MAX_COIN_DURATION;
                poolCoins[poolCoinsIndex].SetTimer(random);

                coins.add(poolCoins[poolCoinsIndex]);
                poolCoinsIndex++;
                if(poolCoinsIndex >= POOL_COINS_SIZE)
                {
                    poolCoinsIndex = 0;
                }
            }
            timeSinceLastCoin -= TIME_BETWEEN_COINS;
        }
    }

    private void updateMeters()
    {
        timeSinceLastMetersCheck += UNIT_TIME;
        if(timeSinceLastMetersCheck > TIME_BETWEEN_METERS)
        {
            currentMeters += 5;

            if(currentLevel.isEasy())
            {
                if(currentMeters % 250 == 0)
                {
                    currentLife += HEALTH_INCREASE;
                    if(currentLife > MAXLIFE)
                    {
                        currentLife = MAXLIFE;
                    }
                }

                if(currentMeters >= 600)
                {
                    currentLevel = Level.MEDIUM;
                }
            }
            else if(currentLevel.isMedium())
            {
                if(currentMeters % 350 == 0)
                {
                    currentLife += HEALTH_INCREASE;
                    if(currentLife > MAXLIFE)
                    {
                        currentLife = MAXLIFE;
                    }
                }
                if(currentMeters >= 1500)
                {
                    currentLevel = Level.HARD;
                }
            }
            else
            {
                if(currentMeters % 500 == 0)
                {
                    currentLife += HEALTH_INCREASE;
                    if(currentLife > MAXLIFE)
                    {
                        currentLife = MAXLIFE;
                    }
                }
            }
        }
        timeSinceLastMetersCheck -= TIME_BETWEEN_METERS;
    }

    public void UpdateCoins()
    {
        for (int i = 0; i < coins.size();i++){
            coins.get(i).move(UNIT_TIME);
            coins.get(i).UpdateTimer(UNIT_TIME);
            if(coins.get(i).getX() < -coins.get(i).getSizeX() || coins.get(i).TimedOut()){
                coins.get(i).setX(STAGE_WIDTH);
                coins.get(i).setSpeedX(0);
                coins.remove(i);
            }
        }
    }

    public int getHealth()
    {
        return currentLife;
    }

    public int getCollectedCoins()
    {
        return coinsCollected;
    }

    public int getMeters()
    {
        return currentMeters;
    }
}

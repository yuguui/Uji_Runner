package es.uji.al341520.InfinityRunner.InfinityRunner;

import android.util.DisplayMetrics;

import es.uji.al341520.InfinityRunner.framework.GameActivity;
import es.uji.al341520.InfinityRunner.framework.IGameController;

/**
 * Created by al341520 on 20/04/18.
 */

public class InfinityRunner extends GameActivity {

    public IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return (new UjiRunnerController(this, displayMetrics.widthPixels,displayMetrics.heightPixels));
    }
}

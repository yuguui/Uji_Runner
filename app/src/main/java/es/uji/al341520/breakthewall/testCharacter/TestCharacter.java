package es.uji.al341520.breakthewall.testCharacter;

import android.util.DisplayMetrics;

import es.uji.al341520.breakthewall.framework.GameActivity;
import es.uji.al341520.breakthewall.framework.IGameController;

/**
 * Created by al341520 on 20/04/18.
 */

public class TestCharacter extends GameActivity {




    public IGameController buildGameController() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return (new TestCharacterController(this, displayMetrics.widthPixels,displayMetrics.heightPixels));
    }
}

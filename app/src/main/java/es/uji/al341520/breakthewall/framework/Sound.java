package es.uji.al341520.breakthewall.framework;

import android.media.SoundPool;

/**
 * Created by jcamen on 24/03/17.
 */
public class Sound {
    int soundId;
    SoundPool soundPool;

    public Sound(int soundId, SoundPool soundPool) {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }
}

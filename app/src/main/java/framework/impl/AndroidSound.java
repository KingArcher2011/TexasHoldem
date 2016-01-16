package framework.impl;

import android.media.SoundPool;

import framework.Sound;

/**
 * Created by xtrao on 2015/12/30.
 */
public class AndroidSound implements Sound {

    private SoundPool mSoundPool;
    private int mSoundID;

    public AndroidSound(SoundPool soundPool, int soundID) {
        mSoundPool = soundPool;
        mSoundID = soundID;
    }

    @Override
    public void play(float volume) {
        mSoundPool.play(mSoundID, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    @Override
    public void dispose() {
        mSoundPool.unload(mSoundID);
    }
}

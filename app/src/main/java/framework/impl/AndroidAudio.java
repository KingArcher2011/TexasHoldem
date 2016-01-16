package framework.impl;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import framework.Audio;
import framework.Music;
import framework.Sound;

/**
 * Created by xtrao on 2015/12/30.
 */
public class AndroidAudio implements Audio {

    Context mContext;
    SoundPool mSoundPool;

    public AndroidAudio(Context context) {
        mContext = context;
        mSoundPool = new SoundPool.Builder().
                setAudioAttributes(new AudioAttributes.Builder().
                        setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                        setUsage(AudioAttributes.USAGE_GAME).
                        build()).
                setMaxStreams(10).
                build();
    }

    @Override
    public Sound newSound(int resID) {
        int soundID = mSoundPool.load(mContext, resID, 1);
        return new AndroidSound(mSoundPool, soundID);
    }

    @Override
    public Music newMusic(int resID) {
        return new AndroidMusic(mContext, resID);
    }
}

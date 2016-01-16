package framework.impl;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

import framework.Music;

/**
 * Created by xtrao on 2015/12/30.
 */
public class AndroidMusic implements Music, MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private boolean isPrepared = false;

    public AndroidMusic(Context context, int resID) {
        mMediaPlayer = MediaPlayer.create(context, resID);
        isPrepared = true;
    }

    @Override
    public void play() {
        if(isPlaying())
            return;
        try {
            if (!isPrepared)
                mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if(isPlaying())
            mMediaPlayer.pause();
    }

    @Override
    public void stop() {
        mMediaPlayer.stop();
        isPrepared = false;
    }

    @Override
    public void setVolume(float volume) {
        mMediaPlayer.setVolume(volume, volume);
    }

    @Override
    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    @Override
    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public boolean isStopped() {
        return !isPrepared;
    }

    @Override
    public void dispose() {
        if(isPrepared)
            mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        isPrepared = false;
    }
}

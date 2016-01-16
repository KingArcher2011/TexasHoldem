package framework;

/**
 * Created by xtrao on 2015/12/30.
 */
public interface Music {

    void play();

    void stop();

    void pause();

    void setLooping(boolean looping);

    void setVolume(float volume);

    boolean isPlaying();

    boolean isStopped();

    boolean isLooping();

    void dispose();
}

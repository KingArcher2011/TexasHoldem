package framework.impl;

import android.view.View.OnTouchListener;

import java.util.List;

import framework.Input.TouchEvent;

/**
 * Created by xtrao on 2015/12/31.
 */
public interface TouchHandler extends OnTouchListener{

    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<TouchEvent> getTouchEvents();
}

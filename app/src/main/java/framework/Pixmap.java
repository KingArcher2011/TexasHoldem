package framework;

import framework.Graphics.PixmapFormat;

/**
 * Created by xtrao on 2015/12/30.
 */
public interface Pixmap {

    int getWidth();

    int getHeight();

    PixmapFormat getFormat();

    void dispose();
}

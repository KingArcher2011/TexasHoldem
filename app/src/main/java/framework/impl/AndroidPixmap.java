package framework.impl;

import android.graphics.Bitmap;

import framework.Graphics;
import framework.Graphics.PixmapFormat;
import framework.Pixmap;

/**
 * Created by xtrao on 2015/12/30.
 */

public class AndroidPixmap implements Pixmap {

    Bitmap mBitmap;
    private Graphics.PixmapFormat mFormat;

    public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
        mBitmap = bitmap;
        mFormat = format;
    }
    @Override
    public int getWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public Graphics.PixmapFormat getFormat() {
        return mFormat;
    }

    @Override
    public void dispose() {
        mBitmap.recycle();
    }
}



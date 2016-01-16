package framework.impl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import framework.Graphics;
import framework.Pixmap;

/**
 * Created by xtrao on 2015/12/30.
 */
public class AndroidGraphics implements Graphics {

    private Bitmap mFrameBuffer;
    private Resources mResources;
    private Canvas mCanvas;
    private Paint mPaint;
    private Rect srcRect;
    private Rect dstRect;

    public AndroidGraphics(Resources resources, Bitmap frameBuffer) {
        mResources = resources;
        mFrameBuffer = frameBuffer;
        mCanvas = new Canvas(mFrameBuffer);
        mPaint = new Paint();
        srcRect = new Rect();
        dstRect = new Rect();
    }

    @Override
    public Pixmap newPixmap(int resID, PixmapFormat format) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        switch(format) {
            case RGB565:
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                break;
            case ARGB4444:
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                break;
            case ARGB8888:
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }
        options.inDensity = mResources.getDisplayMetrics().densityDpi;
        options.inTargetDensity = mResources.getDisplayMetrics().densityDpi;
        Bitmap bitmap = BitmapFactory.decodeResource(mResources, resID, options);
        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        mCanvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        mPaint.setColor(color);
        mCanvas.drawPoint(x, y, mPaint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        mPaint.setColor(color);
        mCanvas.drawLine(x, y, x2, y2, mPaint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Style.FILL);
        mCanvas.drawRect(x, y, x + width - 1, y + height - 1, mPaint);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int rx, int ry, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Style.FILL);
        mCanvas.drawRoundRect(x, y, x + width - 1, y + height - 1, rx, ry, mPaint);
    }

    @Override
    public void drawCycle(int x, int y, int radius, int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Style.FILL);
        mCanvas.drawCircle(x, y, radius, mPaint);
    }

    @Override
    public void drawText(String string, int x, int y, int color, float size) {
        mPaint.setColor(color);
        mPaint.setTextSize(size);
        mPaint.setAntiAlias(true);
        mCanvas.drawText(string, x, y, mPaint);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;
        mCanvas.drawBitmap(((AndroidPixmap)pixmap).mBitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        mCanvas.drawBitmap(((AndroidPixmap)pixmap).mBitmap, x, y, null);
    }

    @Override
    public int getWidth() {
        return mFrameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return mFrameBuffer.getHeight();
    }
}

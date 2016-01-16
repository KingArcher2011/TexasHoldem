package framework.impl;

import android.content.Context;
import android.content.SharedPreferences;

import framework.FileIO;

/**
 * Created by xtrao on 2015/12/31.
 */
public class AndroidFileIO implements FileIO {

    private Context mContext;

    public AndroidFileIO(Context context) {
        mContext = context;
    }

    @Override
    public SharedPreferences getPreferences() {
        return mContext.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }
}

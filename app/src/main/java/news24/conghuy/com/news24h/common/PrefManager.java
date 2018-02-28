package news24.conghuy.com.news24h.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by maidinh on 22/7/2016.
 */
public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private  Context _context;

    // shared pref mode
    private  int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "news_welcome";

    private static final String ENABLE_JAVASCRIPT = "ENABLE_JAVASCRIPT";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(ENABLE_JAVASCRIPT, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(ENABLE_JAVASCRIPT, true);
    }
}
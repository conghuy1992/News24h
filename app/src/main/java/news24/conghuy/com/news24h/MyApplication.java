package news24.conghuy.com.news24h;

import android.app.Application;

/**
 * Created by maidinh on 13/7/2016.
 */
public class MyApplication extends Application {
    public static MyApplication instance = null;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}

package news24.conghuy.com.news24h;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


public class RootActivity extends AppCompatActivity {
    int onStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.color_bar));
//        }
//        onStartCount = 1;
//        if (savedInstanceState == null) // 1st time
//        {
//            this.overridePendingTransition(R.anim.anim_slide_in_left,
//                    R.anim.anim_slide_out_left);
//        } else // already created so reverse animation
//        {
//            onStartCount = 2;
//        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
//        if (onStartCount > 1) {
//            this.overridePendingTransition(R.anim.anim_slide_in_right,
//                    R.anim.anim_slide_out_right);
//
//        } else if (onStartCount == 1) {
//            onStartCount++;
//        }

    }

}
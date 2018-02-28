package news24.conghuy.com.news24h.common.interfaces;

import android.view.View;

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
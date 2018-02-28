package news24.conghuy.com.news24h._class;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import news24.conghuy.com.news24h.R;


public class RobotoTextView extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public RobotoTextView(Context context) {
        super(context);
        init(null);
    }

    public void setTypeface(Typeface tf, int style) {
        Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SFUIText-Light.ttf");
        Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SFUIText-Light.ttf");

        if (style == Typeface.BOLD) {
            super.setTypeface(boldTypeface/*, -1*/);
        } else {
            super.setTypeface(normalTypeface/*, -1*/);
        }
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            int style = getTypeface().getStyle();
            try {
                Typeface myTypeface;
                switch (style) {
                    case Typeface.BOLD:
                        myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SF-UI-Display-Regular.otf");
                        break;
                    case Typeface.ITALIC:
                        myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SF-UI-Display-Regular.otf");
                        break;
                    case Typeface.NORMAL:
                        myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SF-UI-Display-Regular.otf");
                        break;
                    default:
                        myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SF-UI-Display-Regular.otf");
                        break;
                }
                setTypeface(myTypeface);
            } catch (Exception e) {
                e.printStackTrace();
            }

            a.recycle();
        }
    }
}